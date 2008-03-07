package com.idega.block.survey.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.FinderException;

import com.idega.block.survey.business.SurveyBusinessBean;
import com.idega.block.survey.data.SurveyAnswer;
import com.idega.block.survey.data.SurveyQuestion;
import com.idega.core.builder.data.ICPage;
import com.idega.core.localisation.business.ICLocaleBusiness;
import com.idega.core.localisation.data.ICLocale;
import com.idega.data.IDOLookupException;
import com.idega.data.IDORelationshipException;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Layer;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Span;
import com.idega.presentation.Table;
import com.idega.presentation.text.Heading1;
import com.idega.presentation.text.Heading2;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.ListItem;
import com.idega.presentation.text.Lists;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.Parameter;

public class SurveyCSS extends Survey {

	private String id = null;
	private ICPage iBackPage;

	public SurveyCSS() {
		super();
	}

	protected Table getAdminPart() {
		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);

		Image createImage = this._iwb.getImage("shared/create.gif");
		Link createLink = new Link(createImage);
		createLink.setWindowToOpen(SurveyEditorWindow.class);
		createLink.addParameter(SurveyEditorWindow.PRM_INSTANCE_ID, super.getICObjectInstanceID());
		createLink.addParameter(PRM_SWITCHTO_MODE, MODE_EDIT);
		createLink.addParameter(SurveyEditor.PRM_SURVEY_SELECTED, "false");

		table.add(createLink);

		if (this._currentSurvey != null) {
			Image editImage = this._iwb.getImage("shared/edit.gif");
			Link adminLink = new Link(editImage);
			adminLink.setWindowToOpen(SurveyEditorWindow.class);
			adminLink.addParameter(SurveyEditorWindow.PRM_INSTANCE_ID, super.getICObjectInstanceID());
			adminLink.addParameter(PRM_SWITCHTO_MODE, MODE_EDIT);
			adminLink.addParameter(SurveyEditor.PRM_SURVEY_ID, this._currentSurvey.getPrimaryKey().toString());
			table.add(adminLink);

			Image resultImage = this._iwb.getImage("shared/info.gif");
			Link resultLink = new Link(resultImage);
			resultLink.setWindowToOpen(SurveyEditorWindow.class);
			resultLink.addParameter(SurveyEditorWindow.PRM_INSTANCE_ID, super.getICObjectInstanceID());
			resultLink.addParameter(PRM_SWITCHTO_MODE, MODE_RESULTS);
			resultLink.addParameter(SurveyResultEditor.PARAMETER_SURVEY_ID, this._currentSurvey.getPrimaryKey().toString());
			table.add(resultLink);
		}

		return table;
	}

	public void main(IWContext iwc) throws Exception {

		if (this._mode.equals(MODE_EDIT)) {
			SurveyEditor editor = new SurveyEditor(this.getICObjectInstanceID());
			add(editor);
		}
		else if (this._mode.equals(MODE_RESULTS)) {
			SurveyResultEditor editor = new SurveyResultEditor();
			add(editor);
		}
		else {
			if (this.hasEditPermission()) {
				// TODO ATH CSS
				add(getAdminPart());
				if (this.showHelp) {
					add(getHelp("su_help_survey"));
				}
			}

			if (this._action == ACTION_NO_ACTION && this.showIdentificationState) {
				// TODO ATH CSS
				add(getOpenPresentation(iwc));

			}
			else {
				if (this._action == ACTION_SURVEYREPLY && this._surveyAnswerDifference.isEmpty()) {
					storeReply(iwc);
					if (this._currentSurvey != null && this._currentSurvey.getSurveyType().getShowResults()) {
						add(getSurveyResults(iwc));
					}
					else {
						Layer l = new Layer();
						l.setStyleClass("survey");
						add(l);

						Heading1 h1 = new Heading1(this._currentSurvey.getName());
						l.add(h1);

						Layer layer = new Layer();
						layer.setStyleClass("surveySuccess");
						layer.add(new Heading1(this._iwrb.getLocalizedString("survey_has_been_replied", "Thank you for participating")));
						l.add(layer);

						if (getBackPage() != null) {
							Layer buttons = new Layer(Layer.DIV);
							buttons.setStyleClass("survey_buttons");
							l.add(buttons);

							Link back = new Link(new Span(new Text(this._iwrb.getLocalizedString("back", "Back"))));
							back.setPage(getBackPage());
							buttons.add(back);
						}
					}

				}
				else {
					add(getSurveyPresentation(iwc));
				}
			}
		}
	}

	public void setId(String id) {
		//		this.id = id;
	}

	protected PresentationObject getSurveyResults(IWContext iwc) {
		Layer l = new Layer();
		l.setStyleClass("survey");

		if (this._currentSurvey != null) {

			Heading1 h1 = new Heading1(this._currentSurvey.getName());
			l.add(h1);

			ICLocale locale = ICLocaleBusiness.getICLocale(this._iLocaleID);
			try {
				Collection questions = this._currentSurvey.getSurveyQuestions();
				int questionNumber = 1;
				for (Iterator iter = questions.iterator(); iter.hasNext(); questionNumber++) {
					SurveyQuestion question = (SurveyQuestion) iter.next();

					boolean highlight = false;
					String qPK = question.getPrimaryKey().toString();
					highlight = this._surveyAnswerDifference.contains(qPK);

					Layer qL = new Layer(Layer.DIV);
					qL.setStyleClass("survey_question");
					qL.add(new HiddenInput(PRM_QUESTIONS, qPK));
					l.add(qL);

					Heading2 h2 = new Heading2(questionNumber + ". ");
					try {
						h2 = new Heading2(questionNumber + ". " + question.getQuestion(locale));
					}
					catch (IDOLookupException e1) {
						e1.printStackTrace();
					}
					catch (FinderException e1) {
						e1.printStackTrace();
					}
					if (highlight) {
						h2.setStyleClass("highlight");
					}
					qL.add(h2);

					qL.add(getAnswerLayer(iwc, question, locale, true));

				}

			}
			catch (IDORelationshipException e) {
				e.printStackTrace();
			}

			Layer legend = new Layer(Layer.DIV);
			legend.setStyleClass("legend");

			Layer corr = new Layer(Layer.SPAN);
			corr.setStyleClass("selected_correct");
			corr.add(this._iwrb.getLocalizedString("correct_answer", "Correct answer"));
			Layer inc = new Layer(Layer.SPAN);
			inc.setStyleClass("selected_incorrect");
			inc.add(this._iwrb.getLocalizedString("incorrect_answer", "Incorrect answer"));
			Layer right = new Layer(Layer.SPAN);
			right.setStyleClass("correct");
			right.add(this._iwrb.getLocalizedString("correct_option", "Correct option"));

			legend.add(corr);
			legend.add(inc);
			legend.add(right);

			l.add(legend);
			//myForm.add(surveyTable);		
		}

		return l;
	}

	protected PresentationObject getSurveyPresentation(IWContext iwc) {
		Form myForm = new Form();
		if (this.resultPage != null) {
			myForm.setPageToSubmitTo(this.resultPage);
			myForm.addParameter(PRM_SURVEY_ID, this._currentSurvey.getPrimaryKey().toString());
		}
		else {
			myForm.maintainParameter(PRM_SURVEY_ID);
		}
		if (this.id != null) {
			myForm.setId(this.id);
		}
		myForm.setStyleClass("survey");
		if (this._currentSurvey != null) {

			Heading1 h1 = new Heading1(this._currentSurvey.getName());
			myForm.add(h1);

			if (!this._surveyAnswerDifference.isEmpty()) {
				Layer layer = new Layer();
				layer.setStyleClass("surveyError");
				myForm.add(layer);

				layer.add(new Heading1(this._iwrb.getLocalizedString("you_have_not_answered_all_of_the_questions", "You have not answered all of the questions.")));
			}

			ICLocale locale = ICLocaleBusiness.getICLocale(this._iLocaleID);
			try {
				Collection questions = this._currentSurvey.getSurveyQuestions();
				int questionNumber = 1;
				for (Iterator iter = questions.iterator(); iter.hasNext(); questionNumber++) {
					SurveyQuestion question = (SurveyQuestion) iter.next();

					boolean highlight = false;
					String qPK = question.getPrimaryKey().toString();
					highlight = this._surveyAnswerDifference.contains(qPK);

					Layer qL = new Layer(Layer.DIV);
					qL.setStyleClass("survey_question");
					qL.add(new HiddenInput(PRM_QUESTIONS, qPK));
					myForm.add(qL);

					Heading2 h2 = new Heading2(questionNumber + ". ");
					try {
						h2 = new Heading2(questionNumber + ". " + question.getQuestion(locale));
					}
					catch (IDOLookupException e1) {
						e1.printStackTrace();
					}
					catch (FinderException e1) {
						e1.printStackTrace();
					}
					if (highlight) {
						h2.setStyleClass("highlight");
					}
					qL.add(h2);

					qL.add(getAnswerLayer(iwc, question, locale, false));
					//					qL.add(getAnswerTable(question, locale),2,(surveyTable.getRows()+1));

				}
				Layer submit = new Layer(Layer.DIV);
				submit.setStyleClass("survey_buttons");
				Layer sL = new Layer(Layer.SPAN);
				sL.add(this._iwrb.getLocalizedString("submit", "Submit"));
				Link submitLink = new Link(sL);
				submitLink.setFormToSubmit(myForm, false);
				myForm.addParameter(PRM_ACTION, String.valueOf(ACTION_SURVEYREPLY));
				submit.add(submitLink);

				myForm.add(submit);

			}
			catch (IDORelationshipException e) {
				e.printStackTrace();
			}
			//myForm.add(surveyTable);		
		}
		else {
			Layer l = new Layer();
			l.setStyleClass("survey_message");
			l.add(this._iwrb.getLocalizedString("no_survey_defined", "No survey defined"));
			myForm.add(l);
		}

		for (Iterator iter = this.prmVector.iterator(); iter.hasNext();) {
			myForm.add((Parameter) iter.next());
		}

		return myForm;
	}

	private Layer getAnswerLayer(IWContext iwc, SurveyQuestion question, ICLocale locale, boolean results) {
		Layer aL = new Layer(Layer.DIV);
		aL.setStyleClass("survey_answers");

		try {
			if (question.getAnswerType() == SurveyBusinessBean.ANSWERTYPE_TEXTAREA) {
				Lists lists = new Lists();
				lists.setStyleClass("textarea");
				aL.add(lists);
				ListItem li = new ListItem();
				lists.add(li);
				li.add(new HiddenInput(PRM_SELECTION_PREFIX + question.getPrimaryKey().toString(), question.getPrimaryKey().toString()));
				li.add(getAnswerTextArea(question.getPrimaryKey()));
			}
			else {
				Collection answers = this._sBusiness.getAnswerHome().findQuestionsAnswer(question);

				Lists lists = new Lists();
				aL.add(lists);
				int answerNumber = 1;
				String sSelected = iwc.getParameter(PRM_SELECTION_PREFIX + question.getPrimaryKey().toString());
				int selected = -1;
				if (sSelected != null) {
					selected = Integer.parseInt(sSelected);
				}

				for (Iterator iter = answers.iterator(); iter.hasNext(); answerNumber++) {
					SurveyAnswer answer = (SurveyAnswer) iter.next();
					ListItem li = new ListItem();
					lists.add(li);
					if (!results) {
						switch (question.getAnswerType()) {
							case SurveyBusinessBean.ANSWERTYPE_SINGLE_CHOICE:
								lists.setStyleClass("radiobutton");
								li.add(getRadioButton(question.getPrimaryKey(), answer.getPrimaryKey()));
								break;

							case SurveyBusinessBean.ANSWERTYPE_MULTI_CHOICE:
								lists.setStyleClass("checkbox");
								li.add(getCheckBox(question.getPrimaryKey(), answer.getPrimaryKey()));
								break;
						}
					}

					try {
						//add the option that TextInput is added  
						Layer l = new Layer(Layer.SPAN);
						if (results) {
							boolean correct = answer.getIsCorrectAnswer();
							if (selected > 0 && selected == ((Integer) answer.getPrimaryKey()).intValue()) {
								if (correct) {
									li.setStyleClass("selected_correct");
								}
								else {
									li.setStyleClass("selected_incorrect");
								}
							}
							else if (correct) {
								li.setStyleClass("correct");
							}
						}
						l.add(answer.getAnswer(locale));
						li.add(l);
					}
					catch (IDOLookupException e1) {
						e1.printStackTrace();
					}
					catch (FinderException e1) {
						e1.printStackTrace();
					}
				}

			}
		}
		catch (IDOLookupException e) {
			e.printStackTrace();
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}

		return aL;
	}

	public ICPage getBackPage() {
		return this.iBackPage;
	}

	public void setBackPage(ICPage backPage) {
		this.iBackPage = backPage;
	}

}
