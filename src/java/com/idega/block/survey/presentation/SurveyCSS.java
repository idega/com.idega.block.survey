package com.idega.block.survey.presentation;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
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
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.Parameter;
import com.idega.presentation.ui.RadioButton;
import com.idega.presentation.ui.TextArea;
import com.idega.util.PresentationUtil;

public class SurveyCSS extends Survey {

	private String id = null;
	private ICPage iBackPage;
	private HashMap dependentRadioButtons = new HashMap();
	private HashMap dependentCheckBoxes = new HashMap();
	private boolean showDescription = false;

	public SurveyCSS() {
		super();
	}

	protected Table getAdminPart() {
		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);

		Image createImage = this.iwb.getImage("shared/create.gif");
		Link createLink = new Link(createImage);
		createLink.setWindowToOpen(SurveyEditorWindow.class);
		createLink.addParameter(SurveyEditorWindow.PRM_INSTANCE_ID, super.getICObjectInstanceID());
		createLink.addParameter(PRM_SWITCHTO_MODE, MODE_EDIT);
		createLink.addParameter(SurveyEditor.PRM_SURVEY_SELECTED, "false");

		table.add(createLink);

		if (this.currentSurvey != null) {
			Image editImage = this.iwb.getImage("shared/edit.gif");
			Link adminLink = new Link(editImage);
			adminLink.setWindowToOpen(SurveyEditorWindow.class);
			adminLink.addParameter(SurveyEditorWindow.PRM_INSTANCE_ID, super.getICObjectInstanceID());
			adminLink.addParameter(PRM_SWITCHTO_MODE, MODE_EDIT);
			adminLink.addParameter(SurveyEditor.PRM_SURVEY_ID, this.currentSurvey.getPrimaryKey().toString());
			table.add(adminLink);

			Image resultImage = this.iwb.getImage("shared/info.gif");
			Link resultLink = new Link(resultImage);
			resultLink.setWindowToOpen(SurveyEditorWindow.class);
			resultLink.addParameter(SurveyEditorWindow.PRM_INSTANCE_ID, super.getICObjectInstanceID());
			resultLink.addParameter(PRM_SWITCHTO_MODE, MODE_RESULTS);
			resultLink.addParameter(SurveyResultEditor.PARAMETER_SURVEY_ID, this.currentSurvey.getPrimaryKey().toString());
			table.add(resultLink);
		}

		return table;
	}

	public void main(IWContext iwc) throws Exception {
		PresentationUtil.addStyleSheetToHeader(iwc, getBundle(iwc).getVirtualPathWithFileNameString("style/survey.css"));

		if (this.mode.equals(MODE_EDIT)) {
			SurveyEditor editor = new SurveyEditor(this.getICObjectInstanceID());
			add(editor);
		}
		else if (this.mode.equals(MODE_RESULTS)) {
			SurveyResultEditor editor = new SurveyResultEditor();
			add(editor);
		}
		else {
			if (iwc.isSuperAdmin() || iwc.hasRole("survey_admin")) {
				add(getAdminPart());
				if (this.showHelp) {
					add(getHelp("su_help_survey"));
				}
			}

			if (this.action == ACTION_NO_ACTION && this.showIdentificationState) {
				add(getOpenPresentation(iwc));
			}
			else {
				if (this.action == ACTION_SURVEYREPLY) {
					storeReply(iwc);
					if (this.currentSurvey != null && this.currentSurvey.getSurveyType().getShowResults()) {
						add(getSurveyResults(iwc));
					}
					else {
						Layer l = new Layer();
						l.setStyleClass("survey");
						add(l);

						Heading1 h1 = new Heading1(this.currentSurvey.getName());
						l.add(h1);
						if (showDescription) {
							Heading2 h2 = new Heading2(this.currentSurvey.getDescription());
							l.add(h2);							
						}

						Layer layer = new Layer();
						layer.setStyleClass("surveySuccess");
						layer.add(new Heading1(this.iwrb.getLocalizedString("survey_has_been_replied", "Thank you for participating")));
						l.add(layer);

						if (getBackPage() != null) {
							Layer buttons = new Layer(Layer.DIV);
							buttons.setStyleClass("survey_buttons");
							l.add(buttons);

							Link back = new Link(new Span(new Text(this.iwrb.getLocalizedString("back", "Back"))));
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

		if (this.currentSurvey != null) {

			Heading1 h1 = new Heading1(this.currentSurvey.getName());
			l.add(h1);
			if (showDescription) {
				Heading2 h2 = new Heading2(this.currentSurvey.getDescription());
				l.add(h2);							
			}


			ICLocale locale = ICLocaleBusiness.getICLocale(this.iLocaleID);
			try {
				Collection questions = this.currentSurvey.getSurveyQuestions();
				int questionNumber = 1;
				for (Iterator iter = questions.iterator(); iter.hasNext(); questionNumber++) {
					SurveyQuestion question = (SurveyQuestion) iter.next();

					boolean highlight = false;
					String qPK = question.getPrimaryKey().toString();
					highlight = this.surveyAnswerDifference.contains(qPK);

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
			corr.add(this.iwrb.getLocalizedString("correct_answer", "Correct answer"));
			Layer inc = new Layer(Layer.SPAN);
			inc.setStyleClass("selected_incorrect");
			inc.add(this.iwrb.getLocalizedString("incorrect_answer", "Incorrect answer"));
			Layer right = new Layer(Layer.SPAN);
			right.setStyleClass("correct");
			right.add(this.iwrb.getLocalizedString("correct_option", "Correct option"));

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
			myForm.addParameter(PRM_SURVEY_ID, this.currentSurvey.getPrimaryKey().toString());
		}
		else {
			myForm.maintainParameter(PRM_SURVEY_ID);
		}
		if (this.id != null) {
			myForm.setId(this.id);
		}
		myForm.setStyleClass("survey");
		if (this.currentSurvey != null) {

			Heading1 h1 = new Heading1(this.currentSurvey.getName());
			myForm.add(h1);
			if (showDescription) {
				Heading2 h2 = new Heading2(this.currentSurvey.getDescription());
				myForm.add(h2);							
			}


			if (!this.surveyAnswerDifference.isEmpty()) {
				Layer layer = new Layer();
				layer.setStyleClass("surveyError");
				myForm.add(layer);

				layer.add(new Heading1(this.iwrb.getLocalizedString("you_have_not_answered_all_of_the_questions", "You have not answered all of the questions.")));
			}

			ICLocale locale = ICLocaleBusiness.getICLocale(this.iLocaleID);
			try {
				Collection questions = this.currentSurvey.getSurveyQuestions();
				int questionNumber = 1;
				for (Iterator iter = questions.iterator(); iter.hasNext(); questionNumber++) {
					SurveyQuestion question = (SurveyQuestion) iter.next();

					boolean highlight = false;
					String qPK = question.getPrimaryKey().toString();
					highlight = this.surveyAnswerDifference.contains(qPK);

					Layer qL = new Layer(Layer.DIV);
					qL.setStyleClass("survey_question");
					qL.add(new HiddenInput(PRM_QUESTIONS, qPK));
					myForm.add(qL);

					String headingText = questionNumber + ". ";
					if (question.getQuestionDisplayNumber() != null) {
						headingText = question.getQuestionDisplayNumber() + " ";
					}
					
					Heading2 h2 = new Heading2(headingText);
					try {
						h2 = new Heading2(headingText + question.getQuestion(locale));
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
				}
				Layer submit = new Layer(Layer.DIV);
				submit.setStyleClass("survey_buttons");
				Layer sL = new Layer(Layer.SPAN);
				sL.add(this.iwrb.getLocalizedString("submit", "Submit"));
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
			l.add(this.iwrb.getLocalizedString("no_survey_defined", "No survey defined"));
			myForm.add(l);
		}

		for (Iterator iter = this.prmVector.iterator(); iter.hasNext();) {
			myForm.add((Parameter) iter.next());
		}

		return myForm;
	}

	private void checkParent(DependantRadioButtonHolder holder, String disableName) {
		if (holder.getQuestion().getDependantOnQuestion() != null) {
			ArrayList buttons = (ArrayList) this.dependentRadioButtons.get(holder.getQuestion().getDependantOnQuestion().getPrimaryKey());
			if (buttons != null) {
				Iterator it = buttons.iterator();
				while (it.hasNext()) {
					DependantRadioButtonHolder holder2 = (DependantRadioButtonHolder) it.next();
					if (holder2.getAnswer().getDisableDependantQuestions()) {
						holder2.getButton().setToDisableOnClick(disableName, true);								
					} else {
						holder2.getButton().setToDisableOnClick(disableName, false);																
					}
					
					checkParent(holder2, disableName);
				}
			}			
		}
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
				TextArea area = getAnswerTextArea(question.getPrimaryKey()); 
				li.add(area);
				
				if (question.getDependantOnQuestion() != null) {
					if (question.getDependantOnQuestion().getAnswerType() == SurveyBusinessBean.ANSWERTYPE_MULTI_CHOICE) {
						area.setDisabled(true);
						ArrayList checkBoxes = (ArrayList) this.dependentCheckBoxes.get(question.getDependantOnQuestion().getPrimaryKey());
						if (checkBoxes != null) {
							Iterator it = checkBoxes.iterator();
							while (it.hasNext()) {
								DependantCheckBoxHolder holder = (DependantCheckBoxHolder) it.next();
								if (holder.getAnswer().getEnableCheckedDepenantQuestions()) {
									holder.getCheckBox().setToEnableWhenChecked(area);																
									holder.getCheckBox().setToDisableWhenUnchecked(area);								
								} 
								
								//checkParent(holder, PRM_ANSWER_IN_TEXT_AREA_PREFIX + question.getPrimaryKey());
							}
						}												
					} else {
						ArrayList buttons = (ArrayList) this.dependentRadioButtons.get(question.getDependantOnQuestion().getPrimaryKey());
						if (buttons != null) {
							Iterator it = buttons.iterator();
							while (it.hasNext()) {
								DependantRadioButtonHolder holder = (DependantRadioButtonHolder) it.next();
								if (holder.getAnswer().getDisableDependantQuestions()) {
									holder.getButton().setToDisableOnClick(PRM_ANSWER_IN_TEXT_AREA_PREFIX + question.getPrimaryKey(), true);								
								} else {
									holder.getButton().setToDisableOnClick(PRM_ANSWER_IN_TEXT_AREA_PREFIX + question.getPrimaryKey(), false);																
								}
								
								checkParent(holder, PRM_ANSWER_IN_TEXT_AREA_PREFIX + question.getPrimaryKey());
							}
						}						
					}
				}
			}
			else {
				Collection answers = this.sBusiness.getAnswerHome().findQuestionsAnswer(question);

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
								PresentationObject button = getRadioButton(question.getPrimaryKey(), answer.getPrimaryKey());
								li.add(button);
								if (question.getHasDependantQuestions()) {
									ArrayList buttons = null;
									if (this.dependentRadioButtons.containsKey(question.getPrimaryKey())) {
										buttons = (ArrayList) this.dependentRadioButtons.get(question.getPrimaryKey());
									} else {
										buttons = new ArrayList();
									}
									
									buttons.add(new DependantRadioButtonHolder(question, answer, (RadioButton)button));
									this.dependentRadioButtons.put(question.getPrimaryKey(), buttons);
								}
								
								break;

							case SurveyBusinessBean.ANSWERTYPE_MULTI_CHOICE:
								lists.setStyleClass("checkbox");
								PresentationObject checkBox = getCheckBox(question.getPrimaryKey(), answer.getPrimaryKey());

								li.add(checkBox);
								
								if (question.getHasDependantQuestions()) {
									ArrayList checkBoxes = null;
									if (this.dependentCheckBoxes.containsKey(question.getPrimaryKey())) {
										checkBoxes = (ArrayList) this.dependentCheckBoxes.get(question.getPrimaryKey());
									} else {
										checkBoxes = new ArrayList();
									}
									
									checkBoxes.add(new DependantCheckBoxHolder(question, answer, (CheckBox)checkBox));
									this.dependentCheckBoxes.put(question.getPrimaryKey(), checkBoxes);
								}

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
					
					if (question.getDependantOnQuestion() != null) {
						ArrayList buttons = (ArrayList) this.dependentRadioButtons.get(question.getDependantOnQuestion().getPrimaryKey());
						if (buttons != null) {
							Iterator it = buttons.iterator();
							while (it.hasNext()) {
								DependantRadioButtonHolder holder = (DependantRadioButtonHolder) it.next();
								if (holder.getAnswer().getDisableDependantQuestions()) {
									holder.getButton().setToDisableOnClick(PRM_SELECTION_PREFIX + question.getPrimaryKey(), true);								
								} else {
									holder.getButton().setToDisableOnClick(PRM_SELECTION_PREFIX + question.getPrimaryKey(), false);																
								}
								
								checkParent(holder, PRM_SELECTION_PREFIX + question.getPrimaryKey());
							}
						}
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

	
	private class DependantRadioButtonHolder {
		protected RadioButton button = null;
		protected SurveyQuestion question = null;
		protected SurveyAnswer answer = null;
		
		public DependantRadioButtonHolder(SurveyQuestion question, SurveyAnswer answer, RadioButton button) {
			this.question = question;
			this.answer = answer;
			this.button = button;
		}
		
		public SurveyAnswer getAnswer() {
			return this.answer;
		}
		
		public SurveyQuestion getQuestion() {
			return this.question;
		}
		
		public RadioButton getButton() {
			return this.button;
		}
	}

	private class DependantCheckBoxHolder {
		protected CheckBox checkbox = null;
		protected SurveyQuestion question = null;
		protected SurveyAnswer answer = null;
		
		public DependantCheckBoxHolder(SurveyQuestion question, SurveyAnswer answer, CheckBox checkbox) {
			this.question = question;
			this.answer = answer;
			this.checkbox = checkbox;
		}
		
		public SurveyAnswer getAnswer() {
			return this.answer;
		}
		
		public SurveyQuestion getQuestion() {
			return this.question;
		}
		
		public CheckBox getCheckBox() {
			return this.checkbox;
		}
	}

	
	public boolean getShowDescription() {
		return this.showDescription;
	}
	
	public void setShowDescription(boolean showDescription) {
		this.showDescription = showDescription;
	}
}