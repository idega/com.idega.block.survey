/*
 * Created on 27.12.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.idega.block.survey.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.block.category.presentation.FolderBlock;
import com.idega.block.survey.business.SurveyBusiness;
import com.idega.block.survey.business.SurveyBusinessBean;
import com.idega.block.survey.data.SurveyAnswer;
import com.idega.block.survey.data.SurveyEntity;
import com.idega.block.survey.data.SurveyParticipant;
import com.idega.block.survey.data.SurveyQuestion;
import com.idega.block.survey.data.SurveyStatus;
import com.idega.business.IBOLookup;
import com.idega.core.builder.data.ICPage;
import com.idega.core.localisation.business.ICLocaleBusiness;
import com.idega.core.localisation.data.ICLocale;
import com.idega.data.IDOLookupException;
import com.idega.data.IDORelationshipException;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.help.presentation.Help;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.GenericButton;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.Parameter;
import com.idega.presentation.ui.RadioButton;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.TextInput;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;
import com.idega.util.StringHandler;
import com.idega.util.datastructures.QueueMap;

/**
 * Title: Survey Description: Copyright: Copyright (c) 2003 Company: idega
 * Software
 * 
 * @author 2003 - idega team - <br>
 *         <a href="mailto:gummi@idega.is">Gudmundur Agust Saemundsson</a><br>
 * @version 1.0
 * 
 * 
 * Survey does only utilise folders not categories in the FolderBlock system at
 * this time
 */
public class Survey extends FolderBlock {

	final static String IW_BUNDLE_IDENTIFIER = "com.idega.block.survey";
	static final String HELP_BUNDLE_IDENTIFIER = IW_BUNDLE_IDENTIFIER;
	protected IWResourceBundle iwrb;
	protected IWBundle iwb;
	protected IWBundle iwbSurvey;
	protected Locale iLocaleID;

	public final static String STYLE = "font-family:arial; font-size:8pt; color:#000000; text-align: justify; border: 1 solid #000000;";
	public final static String STYLE_2 = "font-family:arial; font-size:8pt; color:#000000; text-align: justify;";
	public final static String STYLE_BUTTON = "font-family:arial; font-size:8pt; color:#000000; text-align: center; border: 1 solid #000000;";

	protected static final String PRM_SELECTION_PREFIX = "su_q_";
	protected static final String PRM_ANSWER_IN_TEXT_AREA_PREFIX = "su_q_ta_";
	public final static String PRM_MAINTAIN_SUFFIX = "_mt";
	public static final String PRM_SURVEY_ID = "survey_id";
	public final static String PRM_PARTICIPANT_IDENTIFIER = "su_p_id";

	public static final String PRM_QUESTIONS = "su_questions";

	public static final String PRM_ACTION = "su_act";
	public static final String PRM_LAST_ACTION = "su_last_act";
	public static final int ACTION_NO_ACTION = 0;
	public static final int ACTION_PARTICIPATE = 1;
	public static final int ACTION_SURVEYREPLY = 2;
	protected ICPage resultPage = null;

	protected int action = ACTION_NO_ACTION;
	protected int lastAction = ACTION_NO_ACTION;

	protected Vector prmVector = new Vector();
	protected QueueMap reply = new QueueMap();

	protected SurveyBusiness sBusiness = null;
	protected SurveyEntity currentSurvey = null;

	private String questionTextStyle;
	private String answerTextStyle;
	private String questionBackgroundColor;
	private String answerBackgroundColor;
	private String questionLabelTextStyle;
	private String questionTextHighlightStyle;
	private String questionHighlightBackgroundColor;
	private String answerHighlightBackgroundColor;
	private String questionLabelTextHighlightStyle;
	private String messageTextStyle;
	private String messageTextHighlightStyle;

	private String style_submitbutton = "font-family:arial; font-size:8pt; color:#000000; text-align: center; border: 1 solid #000000;";
	public final static String MODE_EDIT = "edit";
	public static final String MODE_SURVEY = "survey";
	public static final String MODE_RESULTS = "results";
	protected String mode = MODE_SURVEY;
	public final static String PRM_MODE = "su_mode";
	public final static String PRM_SWITCHTO_MODE = "su_swto_mode";

	List surveyAnswerDifference = new Vector();

	private int textAreaColumns = 50;
	private int textAreaRows = 8;

	protected boolean showHelp = true;
	protected boolean showIdentificationState = true;
	protected String participant = null;

	public Survey() {
		super();
		this.useLocalizedFolders(false);
		this.utiliseCategories(false);
	}

	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}

	public void initializeInMain(IWContext iwc) throws Exception {
		super.initializeInMain(iwc);
		this.sBusiness = (SurveyBusiness) IBOLookup.getServiceInstance(iwc, SurveyBusiness.class);
		this.iwrb = getResourceBundle(iwc);
		this.iwb = iwc.getIWMainApplication().getBundle(IW_CORE_BUNDLE_IDENTIFIER);
		this.iwbSurvey = getBundle(iwc);
		this.iLocaleID = iwc.getCurrentLocale();

		processParameters(iwc);
	}

	private void initializeSurvey(IWContext iwc) throws IDOLookupException, RemoteException, FinderException {
		String sSid = iwc.getParameter(PRM_SURVEY_ID);
		if (sSid == null) {
			Collection surveys = this.sBusiness.getSurveyHome().findActiveSurveys(this.getWorkFolder().getEntity(), IWTimestamp.RightNow().getTimestamp());
			Iterator surveysIter = surveys.iterator();
			// TODO change
			while (surveysIter.hasNext()) {
				this.currentSurvey = (SurveyEntity) surveysIter.next();
			}
		}
		else {
			this.currentSurvey = this.sBusiness.getSurveyHome().findByPrimaryKey(new Integer(sSid));
		}
	}

	/**
	 * @param iwc
	 */
	private void processParameters(IWContext iwc) throws IDORelationshipException, IDOLookupException, RemoteException, FinderException {
		processActionPRM(iwc);

		String mode = iwc.getParameter(PRM_MODE);
		String switchToMode = iwc.getParameter(PRM_SWITCHTO_MODE);
		if (switchToMode != null) {
			this.mode = switchToMode;
		}
		else if (mode != null) {
			this.mode = mode;
		}

		if (MODE_SURVEY.equals(this.mode)) {
			initializeSurvey(iwc);
		}

		processAnswerPRM(iwc);

		this.participant = iwc.getParameter(PRM_PARTICIPANT_IDENTIFIER);
		if (this.participant != null) {
			this.prmVector.add(new Parameter(PRM_PARTICIPANT_IDENTIFIER, this.participant));
		}

	}

	private void processAnswerPRM(IWContext iwc) throws IDORelationshipException {
		Vector allQuestions = new Vector();

		String[] questions = iwc.getParameterValues(PRM_QUESTIONS);
		if (questions != null) {
			if (this.currentSurvey != null) {
				Collection q = this.currentSurvey.getSurveyQuestions();
				for (Iterator iter = q.iterator(); iter.hasNext();) {
					SurveyQuestion element = (SurveyQuestion) iter.next();
					allQuestions.add(element.getPrimaryKey().toString());
				}
			}

			for (int i = 0; i < questions.length; i++) {
				String[] answers = iwc.getParameterValues(PRM_SELECTION_PREFIX + questions[i]);
				if (answers != null) {
					Vector ansList = new Vector();
					boolean noAnswer = true;
					for (int j = 0; j < answers.length; j++) {
						Integer answerPK = Integer.decode(answers[j]);
						if (answerPK != null) {
							Object[] answer = new Object[2];
							answer[0] = answerPK;
							String textAnswer = iwc.getParameter(PRM_ANSWER_IN_TEXT_AREA_PREFIX + answers[j]);
							if (textAnswer != null) {
								answer[1] = textAnswer;
							}
							ansList.add(answer);
							if (noAnswer) {
								allQuestions.remove(questions[i]);
								noAnswer = false;
							}
						}
					}
					Integer qPK = Integer.decode(questions[i]);
					if (qPK != null) {
						this.reply.put(qPK, ansList);
					}
				}

			}
		}
		this.surveyAnswerDifference = allQuestions;
	}

	public void main(IWContext iwc) throws Exception {
		if (this.mode.equals(MODE_EDIT)) {
			SurveyEditor editor = new SurveyEditor(this.getICObjectInstanceID());
			if (this.messageTextStyle != null) {
				editor.setMessageTextStyle(this.messageTextStyle);
			}

			if (this.messageTextHighlightStyle != null) {
				editor.setMessageTextHighlightStyle(this.messageTextHighlightStyle);
			}
			add(editor);
		}
		else if (this.mode.equals(MODE_RESULTS)) {
			SurveyResultEditor editor = new SurveyResultEditor();
			if (this.messageTextStyle != null) {
				editor.setMessageTextStyle(this.messageTextStyle);
			}

			if (this.messageTextHighlightStyle != null) {
				editor.setMessageTextHighlightStyle(this.messageTextHighlightStyle);
			}
			add(editor);
		}
		else {
			if (this.hasEditPermission()) {
				add(getAdminPart());
				if (this.showHelp) {
					add(getHelp("su_help_survey"));
				}
			}

			if (this.action == ACTION_NO_ACTION && this.showIdentificationState) {
				add(getOpenPresentation(iwc));

			}
			else {
				if (this.action == ACTION_SURVEYREPLY && this.surveyAnswerDifference.isEmpty()) {
					add(Text.BREAK);
					add(Text.BREAK);
					add(getMessageTextObject(this.iwrb.getLocalizedString("survey_has_been_replied", "Thank you for participating"), false));
					storeReply(iwc);
				}
				else {

					if (!this.surveyAnswerDifference.isEmpty()) {
						add(Text.BREAK);
						add(getMessageTextObject(this.iwrb.getLocalizedString("you_have_not_answered_all_of_the_questions", "You have not answered all of the questions."), true));
					}

					add(getSurveyPresentation(iwc));
				}
			}
		}

	}

	/**
	 * @param iwc
	 */
	protected void storeReply(IWContext iwc) throws FinderException, IDOLookupException, RemoteException, CreateException {

		Set questions = this.reply.keySet();
		if (questions != null) {
			String participantKey = StringHandler.getRandomStringNonAmbiguous(20);
			User currentUser = null;
			try {
				currentUser = iwc.getCurrentUser();
			} catch (Exception e) {
				currentUser = null;
			}
			SurveyParticipant participant = this.sBusiness.createSurveyParticipant(participantKey, this.currentSurvey, currentUser, this.currentSurvey.getCanUserAnswerMoreThanOnce());
			if (!this.currentSurvey.getCanUserAnswerMoreThanOnce()) {
				this.sBusiness.removeSurveyRepliesForParticipant(this.currentSurvey, participant);
			}
			IWTimestamp now = new IWTimestamp();
			for (Iterator qIter = questions.iterator(); qIter.hasNext();) {
				Object questionPK = qIter.next();
				SurveyQuestion question = this.sBusiness.getQuestionHome().findByPrimaryKey(questionPK);
				List answers = (List) this.reply.get(questionPK);
				for (Iterator ansIter = answers.iterator(); ansIter.hasNext();) {
					Object[] answerPKAndText = (Object[]) ansIter.next();
					SurveyAnswer answer = null;
					if (question.getAnswerType() != SurveyBusinessBean.ANSWERTYPE_TEXTAREA) {
						try {
							answer = this.sBusiness.getAnswerHome().findByPrimaryKey(answerPKAndText[0]);
						}
						catch (FinderException fe) {
							//No answer found...
						}
					}
					this.sBusiness.createSurveyReply(this.currentSurvey, question, participant, answer, (String) answerPKAndText[1], now);
				}
			}
			if (this.participant != null) {
				this.sBusiness.reportParticipation(this.currentSurvey, this.participant);
			}
		}

		SurveyStatus status = this.sBusiness.getSurveyStatus(this.currentSurvey);
		status.setIsModified(true);
		status.store();
		System.out.println("Stored...");
	}

	/**
	 * @param iwc
	 * @return
	 */
	protected PresentationObject getSurveyPresentation(IWContext iwc) {
		Form myForm = new Form();
		if (this.resultPage != null) {
			myForm.setPageToSubmitTo(this.resultPage);
			myForm.addParameter(PRM_SURVEY_ID, this.currentSurvey.getPrimaryKey().toString());
		}
		else {
			myForm.maintainParameter(PRM_SURVEY_ID);
		}

		if (this.currentSurvey != null) {
			Table surveyTable = new Table();
			surveyTable.setWidth("100%");
			surveyTable.setCellspacing(0);
			surveyTable.setCellpadding(4);
			ICLocale locale = ICLocaleBusiness.getICLocale(this.iLocaleID);
			try {
				Collection questions = this.currentSurvey.getSurveyQuestions();
				int questionNumber = 1;
				for (Iterator iter = questions.iterator(); iter.hasNext(); questionNumber++) {
					SurveyQuestion question = (SurveyQuestion) iter.next();

					boolean highlight = false;
					String qPK = question.getPrimaryKey().toString();
					highlight = this.surveyAnswerDifference.contains(qPK);

					surveyTable.add(new HiddenInput(PRM_QUESTIONS, qPK), 1, getQuestionRowIndex(questionNumber));
					surveyTable.add(getQuestionLabel(questionNumber, highlight), 1, getQuestionRowIndex(questionNumber));
					try {
						surveyTable.add(getQuestionTextObject(question.getQuestion(locale), highlight), 2, getQuestionRowIndex(questionNumber));
					}
					catch (IDOLookupException e1) {
						e1.printStackTrace();
					}
					catch (FinderException e1) {
						e1.printStackTrace();
					}
					if (highlight) {
						if (this.questionHighlightBackgroundColor != null) {
							surveyTable.setRowColor(getQuestionRowIndex(questionNumber), this.questionHighlightBackgroundColor);
						}
						else if (this.questionBackgroundColor != null) {
							surveyTable.setRowColor(getQuestionRowIndex(questionNumber), this.questionBackgroundColor);
						}
					}
					else {
						if (this.questionBackgroundColor != null) {
							surveyTable.setRowColor(getQuestionRowIndex(questionNumber), this.questionBackgroundColor);
						}
					}

					surveyTable.add(getAnswerTable(question, locale), 2, (surveyTable.getRows() + 1));

					if (highlight) {
						if (this.answerHighlightBackgroundColor != null) {
							surveyTable.setRowColor((surveyTable.getRows()), this.answerHighlightBackgroundColor);
						}
						else if (this.answerBackgroundColor != null) {
							surveyTable.setRowColor((surveyTable.getRows()), this.answerBackgroundColor);
						}
					}
					else {
						if (this.answerBackgroundColor != null) {
							surveyTable.setRowColor((surveyTable.getRows()), this.answerBackgroundColor);
						}
					}

				}

				if (surveyTable.getColumns() >= 2) {
					surveyTable.setColumnVerticalAlignment(1, Table.VERTICAL_ALIGN_TOP);
					surveyTable.setColumnVerticalAlignment(2, Table.VERTICAL_ALIGN_TOP);
					surveyTable.setColumnAlignment(1, Table.HORIZONTAL_ALIGN_RIGHT);
					surveyTable.setColumnAlignment(1, Table.HORIZONTAL_ALIGN_LEFT);
					surveyTable.setColumnWidth(2, "100%");
				}

				SubmitButton submit = new SubmitButton(this.iwrb.getLocalizedString("submit", "  Submit  "), PRM_ACTION, String.valueOf(ACTION_SURVEYREPLY));
				submit.setStyleAttribute(this.style_submitbutton);
				surveyTable.add(submit, 2, getQuestionRowIndex(questionNumber));
				surveyTable.setAlignment(2, getQuestionRowIndex(questionNumber), Table.HORIZONTAL_ALIGN_RIGHT);

			}
			catch (IDORelationshipException e) {
				e.printStackTrace();
			}
			myForm.add(surveyTable);
		}
		else {
			add(getQuestionTextObject(this.iwrb.getLocalizedString("no_survey_defined", "No survey defined"), false));
		}

		for (Iterator iter = this.prmVector.iterator(); iter.hasNext();) {
			myForm.add((Parameter) iter.next());
		}

		return myForm;
	}

	/**
	 * @param iwc
	 * @return
	 */
	protected PresentationObject getOpenPresentation(IWContext iwc) {
		Form myForm = new Form();
		myForm.maintainParameter(PRM_SURVEY_ID);
		if (this.currentSurvey != null) {
			Table surveyTable = new Table();
			//			surveyTable.setWidth("100%");
			surveyTable.setCellspacing(0);
			surveyTable.setCellpadding(4);

			//TODO Should be the survey description
			myForm.add(this.iwrb.getLocalizedString("introduction", "Here you can put your e-mail"));

			surveyTable.add(getLabel(this.iwrb.getLocalizedString("identitfier", "e-mail")), 1, 1);
			surveyTable.add(getIdentifierTextInput(PRM_PARTICIPANT_IDENTIFIER, null), 2, 1);
			surveyTable.setNoWrap(1, 1);

			if (surveyTable.getColumns() >= 2) {
				surveyTable.setColumnVerticalAlignment(1, Table.VERTICAL_ALIGN_TOP);
				surveyTable.setColumnVerticalAlignment(2, Table.VERTICAL_ALIGN_TOP);
				surveyTable.setColumnAlignment(1, Table.HORIZONTAL_ALIGN_RIGHT);
				surveyTable.setColumnAlignment(1, Table.HORIZONTAL_ALIGN_LEFT);
			}

			SubmitButton submit = new SubmitButton(this.iwrb.getLocalizedString("forward", "  Forward  "), PRM_ACTION, String.valueOf(ACTION_PARTICIPATE));
			submit.setStyleAttribute(this.style_submitbutton);
			surveyTable.add(submit, 2, 2);
			surveyTable.setAlignment(2, 2, Table.HORIZONTAL_ALIGN_RIGHT);

			myForm.add(surveyTable);
		}
		else {
			add(getQuestionTextObject(this.iwrb.getLocalizedString("no_survey_defined", "No survey defined"), false));
		}

		for (Iterator iter = this.prmVector.iterator(); iter.hasNext();) {
			myForm.add((Parameter) iter.next());
		}

		return myForm;
	}

	/**
	 * @param string
	 * @return
	 */
	private Text getLabel(String string) {
		Text t = new Text(string + ": ");
		setStyle(t);
		return t;
	}

	/**
	 * @param string
	 * @param ans
	 * @return
	 */
	private PresentationObject getIdentifierTextInput(String name, String displayText) {
		TextInput i = new TextInput(name);

		i.setSize(25);
		setStyle(i);
		if (displayText != null) {
			i.setValue(displayText);
		}
		return i;
	}

	public void setStyle(PresentationObject obj) {
		if (obj instanceof Text) {
			this.setStyle((Text) obj);
		}
		else if (obj instanceof GenericButton) {
			obj.setMarkupAttribute("style", STYLE_BUTTON);
		}
		else {
			obj.setMarkupAttribute("style", STYLE);
		}
	}

	public void setStyle(Text obj) {
		obj.setMarkupAttribute("style", STYLE_2);
	}

	/**
	 * @param iwc
	 * @return
	 */
	private Table getAnswerTable(SurveyQuestion question, ICLocale locale) {
		Table answerTable = new Table();

		try {
			Collection answers = this.sBusiness.getAnswerHome().findQuestionsAnswer(question);
			if (question.getAnswerType() == SurveyBusinessBean.ANSWERTYPE_TEXTAREA) {
				//hiddenInput
				//Textarea
				Iterator iter = answers.iterator();
				if (iter.hasNext()) {
					SurveyAnswer answer = (SurveyAnswer) iter.next();
					answerTable.add(new HiddenInput(PRM_SELECTION_PREFIX + question.getPrimaryKey().toString(), answer.getPrimaryKey().toString()));
					answerTable.add(getAnswerTextArea(answer.getPrimaryKey()));
				}
			}
			else {
				int answerNumber = 1;
				for (Iterator iter = answers.iterator(); iter.hasNext(); answerNumber++) {
					SurveyAnswer answer = (SurveyAnswer) iter.next();
					switch (question.getAnswerType()) {
						case SurveyBusinessBean.ANSWERTYPE_SINGLE_CHOICE:
							answerTable.add(getRadioButton(question.getPrimaryKey(), answer.getPrimaryKey()), 1, answerNumber);
							break;

						case SurveyBusinessBean.ANSWERTYPE_MULTI_CHOICE:
							answerTable.add(getCheckBox(question.getPrimaryKey(), answer.getPrimaryKey()), 1, answerNumber);
							break;
					}

					try {
						//add the option that TextInput is added  
						answerTable.add(getAnswerTextObject(answer.getAnswer(locale)), 2, answerNumber);
					}
					catch (IDOLookupException e1) {
						e1.printStackTrace();
					}
					catch (FinderException e1) {
						e1.printStackTrace();
					}
				}

				if (answerTable.getColumns() >= 2) {
					answerTable.setColumnVerticalAlignment(1, Table.VERTICAL_ALIGN_TOP);
					answerTable.setColumnVerticalAlignment(2, Table.VERTICAL_ALIGN_TOP);
					answerTable.setColumnAlignment(1, Table.HORIZONTAL_ALIGN_CENTER);
					answerTable.setColumnAlignment(1, Table.HORIZONTAL_ALIGN_LEFT);
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

		return answerTable;
	}

	/**
	 * @param iwc
	 */
	private void processActionPRM(IWContext iwc) {
		String action = iwc.getParameter(PRM_ACTION);
		boolean someAction = false;
		try {
			this.action = Integer.parseInt(action);
			this.prmVector.add(new Parameter(PRM_LAST_ACTION, String.valueOf(this.action)));
			someAction = true;
		}
		catch (NumberFormatException e) {
			this.action = ACTION_NO_ACTION;
		}

		String lastAction = iwc.getParameter(PRM_LAST_ACTION);
		try {
			this.lastAction = Integer.parseInt(lastAction);
			if (!someAction) {
				this.prmVector.add(new Parameter(PRM_LAST_ACTION, String.valueOf(this.lastAction)));
			}
		}
		catch (NumberFormatException e1) {
			if (!someAction) {
				this.prmVector.add(new Parameter(PRM_LAST_ACTION, String.valueOf(ACTION_NO_ACTION)));
			}
		}
	}

	/**
	 * @return
	 */
	protected PresentationObject getCheckBox(Object name, Object value) {
		CheckBox box = new CheckBox(PRM_SELECTION_PREFIX + name.toString(), value.toString());
		List answers = (List) this.reply.get(name);
		if (answers != null) {
			for (Iterator iter = answers.iterator(); iter.hasNext();) {
				Object[] answerPKAndText = (Object[]) iter.next();
				if (value.equals(answerPKAndText[0])) {
					box.setChecked(true);
				}
			}
		}
		return box;
	}

	/**
	 * @return
	 */
	protected PresentationObject getRadioButton(Object name, Object value) {
		RadioButton r = new RadioButton(PRM_SELECTION_PREFIX + name.toString(), value.toString());
		List answers = (List) this.reply.get(name);
		if (answers != null) {
			for (Iterator iter = answers.iterator(); iter.hasNext();) {
				Object[] answerPKAndText = (Object[]) iter.next();
				if (value.equals(answerPKAndText[0])) {
					r.setSelected(true);
				}
			}
		}

		return r;
	}

	/**
	 * @return
	 */
	protected TextArea getAnswerTextArea(Object name) {
		TextArea aTA = new TextArea(PRM_ANSWER_IN_TEXT_AREA_PREFIX + name);
		//aTA.setStyleAttribute(style_form_element);

		List answers = (List) this.reply.get(name);
		if (answers != null) {
			Iterator iter = answers.iterator();
			if (iter.hasNext()) {
				Object[] answerPKAndText = (Object[]) iter.next();
				if ("".equals(answerPKAndText[1])) {
					aTA.setValue(answerPKAndText[1].toString());
				}
			}
		}

		aTA.setColumns(this.textAreaColumns);
		aTA.setRows(this.textAreaRows);
		return aTA;
	}

	private int getQuestionRowIndex(int questionNumber) {
		return ((questionNumber * 2) - 1);
	}

	/**
	 * @param questionNumber
	 * @return
	 */
	private PresentationObject getQuestionLabel(int questionNumber, boolean highlight) {
		Text text = new Text(questionNumber + ".");
		if (!highlight) {
			if (this.questionLabelTextStyle != null) {
				text.setStyleAttribute(this.questionLabelTextStyle);
			}
		}
		else {
			if (this.questionLabelTextHighlightStyle != null) {
				text.setStyleAttribute(this.questionLabelTextHighlightStyle);
			}
		}

		return text;
	}

	/**
	 * @param question
	 * @return
	 */
	private PresentationObject getQuestionTextObject(String question, boolean highlight) {
		Text text = new Text(question);
		if (!highlight) {
			if (this.questionTextStyle != null) {
				text.setStyleAttribute(this.questionTextStyle);
			}
		}
		else {
			if (this.questionTextHighlightStyle != null) {
				text.setStyleAttribute(this.questionTextHighlightStyle);
			}
		}
		return text;
	}

	/**
	 * @param answer
	 * @return
	 */
	private PresentationObject getAnswerTextObject(String answer) {
		Text text = new Text(answer);
		if (this.answerTextStyle != null) {
			text.setStyleAttribute(this.answerTextStyle);
		}
		return text;
	}

	protected Table getAdminPart() {
		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);

		Image createImage = this.iwb.getImage("shared/create.gif");
		Link createLink = new Link(createImage);
		createLink.addParameter(PRM_SWITCHTO_MODE, MODE_EDIT);
		createLink.addParameter(SurveyEditor.PRM_SURVEY_SELECTED, "false");

		table.add(createLink);

		if (this.currentSurvey != null) {
			Image editImage = this.iwb.getImage("shared/edit.gif");
			Link adminLink = new Link(editImage);
			adminLink.addParameter(PRM_SWITCHTO_MODE, MODE_EDIT);
			adminLink.addParameter(SurveyEditor.PRM_SURVEY_ID, this.currentSurvey.getPrimaryKey().toString());
			table.add(adminLink);

			Image resultImage = this.iwb.getImage("shared/info.gif");
			Link resultLink = new Link(resultImage);
			resultLink.addParameter(PRM_SWITCHTO_MODE, MODE_RESULTS);
			resultLink.addParameter(SurveyResultEditor.PARAMETER_SURVEY_ID, this.currentSurvey.getPrimaryKey().toString());
			table.add(resultLink);
		}

		return table;
	}

	public synchronized Object clone() {
		Survey clone = (Survey) super.clone();
		clone.reply = new QueueMap();
		clone.prmVector = new Vector();
		return clone;
	}

	public Help getHelp(String helpTextKey) {
		Help help = new Help();
		help.setHelpTextBundle(HELP_BUNDLE_IDENTIFIER);
		help.setHelpTextKey(helpTextKey);
		help.setLinkText("help");
		return help;
	}

	/**
	 * @param color
	 */
	public void setAnswerBackgroundColor(String color) {
		this.answerBackgroundColor = color;
	}

	/**
	 * @param color
	 */
	public void setAnswerHighlightBackgroundColor(String color) {
		this.answerHighlightBackgroundColor = color;
	}

	/**
	 * @param color
	 */
	public void setQuestionBackgroundColor(String color) {
		this.questionBackgroundColor = color;
	}

	/**
	 * @param style
	 */
	public void setQuestionTextStyle(String style) {
		this.questionTextStyle = style;
	}

	/**
	 * @param style
	 */
	public void setQuestionHighlightBackgroundColor(String style) {
		this.questionHighlightBackgroundColor = style;
	}

	/**
	 * @param style
	 */
	public void setQuestionTextHighlightStyle(String style) {
		this.questionTextHighlightStyle = style;
	}

	/**
	 * @param columns
	 */
	public void setTextAreaColumns(int columns) {
		this.textAreaColumns = columns;
	}

	/**
	 * @param rows
	 */
	public void setTextAreaRows(int rows) {
		this.textAreaRows = rows;
	}

	public void setToShowHelp(boolean value) {
		this.showHelp = value;
	}

	public void setToShowIdentificationState(boolean value) {
		this.showIdentificationState = value;
	}

	public void setMessageTextStyle(String style) {
		this.messageTextStyle = style;
	}

	public void setMessageTextHighlightStyle(String style) {
		this.messageTextHighlightStyle = style;
	}

	protected PresentationObject getMessageTextObject(String message, boolean highlight) {
		Text text = new Text(message);
		if (!highlight) {
			if (this.messageTextStyle != null) {
				text.setStyleAttribute(this.messageTextStyle);
			}
		}
		else {
			if (this.messageTextHighlightStyle != null) {
				text.setStyleAttribute(this.messageTextHighlightStyle);
			}
		}
		return text;
	}

	/**
	 * @param style
	 */
	public void setAnswerTextStyle(String style) {
		this.answerTextStyle = style;
	}

	public void setResultPage(ICPage resultPage) {
		this.resultPage = resultPage;
	}

}
