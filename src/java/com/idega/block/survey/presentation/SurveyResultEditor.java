package com.idega.block.survey.presentation;

import java.rmi.RemoteException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.block.survey.business.SurveyBusiness;
import com.idega.block.survey.business.SurveyBusinessBean;
import com.idega.block.survey.data.SurveyAnswer;
import com.idega.block.survey.data.SurveyAnswerHome;
import com.idega.block.survey.data.SurveyEntity;
import com.idega.block.survey.data.SurveyParticipant;
import com.idega.block.survey.data.SurveyQuestion;
import com.idega.block.survey.data.SurveyReply;
import com.idega.block.survey.data.SurveyReplyHome;
import com.idega.block.survey.data.SurveyStatus;
import com.idega.business.IBOLookup;
import com.idega.core.file.data.ICFile;
import com.idega.core.localisation.business.ICLocaleBusiness;
import com.idega.core.localisation.data.ICLocale;
import com.idega.data.IDOException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.PresentationObjectContainer;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.BackButton;
import com.idega.presentation.ui.FieldSet;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.IntegerInput;
import com.idega.presentation.ui.Legend;
import com.idega.presentation.ui.Parameter;
import com.idega.presentation.ui.SubmitButton;
import com.idega.util.IWTimestamp;
import com.idega.util.poi.POIUtility;
import com.idega.util.poi.PresentationUtil;

/**
 * Title: SurveyResult Description: Copyright: Copyright (c) 2004 Company: idega
 * Software
 * 
 * @author 2004 - idega team - <br>
 *         <a href="mailto:gimmi@idega.is">Grimur Jonsson</a><br>
 * @version 1.0
 */
public class SurveyResultEditor extends Block {

	final static String IW_BUNDLE_IDENTIFIER = "com.idega.block.survey";

	public static String PARAMETER_SURVEY_ID = SurveyEditor.PRM_SURVEY_ID;
	private static String PARAMETER_STATUS_ID = "prmStId";
	private static String PARAMETER_NEW_STATUS = "prmNSt";
	private static String PARAMETER_CREATE_EXCEL = "prmCEx";
	private SurveyBusiness sBusiness;
	private IWResourceBundle iwrb;
	private Locale locale;
	private ICLocale icLocale;
	private SurveyEntity survey;
	private SurveyStatus status;
	private Collection allStatuses;
	private Collection questions;
	private SurveyReplyHome repHome;
	private SurveyAnswerHome ansHome;

	private NumberFormat nf = NumberFormat.getPercentInstance();

	private String messageTextStyle;// = "font-weight: bold;";
	private String messageTextHighlightStyle;// =
												// "font-weight: bold;color: #FF0000;"
												// ;
	private long startMilli = 0;

	private String style_submitbutton = "font-family:arial; font-size:8pt; color:#000000; text-align: center; border: 1 solid #000000;";

	public SurveyResultEditor() {
		super();
	}

	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}

	public void initializeInMain(IWContext iwc) throws Exception {
		super.initializeInMain(iwc);
		this.sBusiness = (SurveyBusiness) IBOLookup.getServiceInstance(iwc,
				SurveyBusiness.class);
		this.iwrb = getResourceBundle(iwc);
		this.locale = iwc.getCurrentLocale();
		this.icLocale = ICLocaleBusiness.getICLocale(this.locale);
		this.nf.setMinimumFractionDigits(2);

		String surveyID = iwc.getParameter(PARAMETER_SURVEY_ID);
		if (surveyID != null) {
			try {
				this.survey = this.sBusiness.getSurveyHome().findByPrimaryKey(
						new Integer(surveyID));
				this.questions = this.survey.getSurveyQuestions();
				if (iwc.isParameterSet(PARAMETER_STATUS_ID)) {
					this.status = this.sBusiness
							.getSurveyStatusHome()
							.findByPrimaryKey(
									new Integer(iwc
											.getParameter(PARAMETER_STATUS_ID)));
				} else {
					this.status = this.sBusiness.getSurveyStatus(this.survey);
				}
				this.allStatuses = this.sBusiness.getSurveyStatusHome()
						.findAllBySurvey(this.survey);
			} catch (Exception e) {
				e.printStackTrace(System.err);
			}
		}
	}

	public void main(IWContext iwc) throws RemoteException {
		PresentationUtil.addStyleSheetToHeader(iwc, getBundle(iwc).getVirtualPathWithFileNameString("style/survey.css"));

		if (this.survey != null) {
			Legend legend = new Legend(this.survey.getName() + " - "
					+ this.iwrb.getLocalizedString("history", "History"));
			FieldSet fs = new FieldSet(legend);
			if (iwc.isParameterSet(PARAMETER_STATUS_ID)) {
				fs.add(displayQuestions());
			} else {
				if (iwc.isParameterSet(PARAMETER_NEW_STATUS)) {
					newStatus(iwc);
				} else if (iwc.isParameterSet(PARAMETER_CREATE_EXCEL)) {
					createFile();
				}
				fs.add(displayStatuses());
			}
			add(fs);

			Form myForm = new Form();
			Legend legend2 = new Legend(this.survey.getName()
					+ " - "
					+ this.iwrb.getLocalizedString("random_participants",
							"Random participants:"));
			FieldSet fs2 = new FieldSet(legend2);
			fs2.setWidth("450");
			fs2.add(displayParticipants(iwc));
			myForm.add(fs2);
			add(myForm);

		} else {
			add(getText(this.iwrb.getLocalizedString("no_survey_defined",
					"No survey defined")));
		}
		add(Text.BREAK);
		BackButton link = new BackButton("Back");
		add(link);
	}

	/**
	 * @return
	 */
	private PresentationObjectContainer displayParticipants(IWContext iwc) {
		Table table = new Table();

		String prmNumberOfParticipants = "su_num_of_p";
		String prmSubmit = "su_subm";

		IntegerInput numberOfParticipantsInput = new IntegerInput(
				prmNumberOfParticipants);
		numberOfParticipantsInput.setValue(1);

		SubmitButton submit = new SubmitButton(prmSubmit, this.iwrb
				.getLocalizedString("submit", "  Submit  "));
		submit.setStyleAttribute(this.style_submitbutton);

		table.add(getText(this.iwrb.getLocalizedString(
				"number_of_participants", "Number of participants")), 1, 1);
		table.add(numberOfParticipantsInput, 1, 1);
		table.add(submit, 1, 1);

		table.add(new Parameter(PARAMETER_SURVEY_ID, this.survey
				.getPrimaryKey().toString()));
		table.add(new Parameter(Survey.PRM_SWITCHTO_MODE, Survey.MODE_RESULTS));

		try {
			String prm = iwc.getParameter(prmNumberOfParticipants);
			if (prm != null) {
				Table pTable = new Table();
				int numberOfParticipants = Integer.parseInt(prm);
				Collection participants = this.sBusiness
						.getSurveyParticipantHome().findRandomParticipants(
								this.survey, numberOfParticipants, true);

				int row = 1;
				for (Iterator iter = participants.iterator(); iter.hasNext(); row++) {
					SurveyParticipant participant = (SurveyParticipant) iter
							.next();
					pTable.add(participant.getParticipantName(), 1, row);
				}

				table.add(pTable, 1, 2);
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IDOLookupException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}

		return table;
	}

	private void newStatus(IWContext iwc) throws RemoteException {
		try {
			SurveyStatus status = this.sBusiness.getSurveyStatusHome().create();
			status.setSurvey(this.survey);
			status.setIsModified(false);
			status.store();

			this.status = this.sBusiness.getSurveyStatus(this.survey);
			this.allStatuses = this.sBusiness.getSurveyStatusHome()
					.findAllBySurvey(this.survey);
		} catch (IDOLookupException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		} catch (CreateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private Table displayStatuses() {
		Table table = new Table();
		table.setBorder(0);
		int row = 1;
		if (this.allStatuses != null && !this.allStatuses.isEmpty()) {
			Iterator iter = this.allStatuses.iterator();
			SurveyStatus status;
			IWTimestamp stamp;
			boolean modified;
			boolean warning = false;
			boolean isLatest = false;
			Link link;
			ICFile reportFile;
			String YES = this.iwrb.getLocalizedString("yes", "Yes");
			String NO = this.iwrb.getLocalizedString("no", "No");

			table.add(getHeader(this.iwrb.getLocalizedString(
					"last_saved_modification", "Last saved modification")), 1,
					row);
			table.add(getHeader(this.iwrb.getLocalizedString("modified_since",
					"Modified since")), 2, row);
			table.add(getHeader(this.iwrb
					.getLocalizedString("report", "Report")), 3, row);
			table.mergeCells(3, row, 4, row);

			while (iter.hasNext()) {
				status = (SurveyStatus) iter.next();
				++row;
				stamp = new IWTimestamp(status.getTimeOfStatus());
				modified = status.getIsModified();
				isLatest = status.getPrimaryKey().equals(
						this.status.getPrimaryKey());
				table.add(getText(stamp.getLocaleDateAndTime(this.locale)), 1,
						row);
				table.setAlignment(2, row, Table.HORIZONTAL_ALIGN_CENTER);
				if (modified) {
					table.add(getText(YES), 2, row);
				} else {
					table.add(getText(NO), 2, row);
				}

				if (isLatest) {
					link = new Link("HTML");
					link.addParameter(PARAMETER_SURVEY_ID, this.survey
							.getPrimaryKey().toString());
					link.addParameter(PARAMETER_STATUS_ID, status
							.getPrimaryKey().toString());
					link.addParameter(Survey.PRM_SWITCHTO_MODE,
							Survey.MODE_RESULTS);
					table.add(link, 3, row);
					table.add(Text.NON_BREAKING_SPACE, 3, row);
				}

				reportFile = status.getReportFile();
				if (reportFile != null) {
					link = new Link(getText(this.iwrb.getLocalizedString(
							"excel", "Excel")));
					link.setFile(reportFile);
					table.add(link, 4, row);
					if (modified) {
						warning = true;
						table.add(getText("*"), 4, row);
					}
				}
				if (isLatest) {
					link = new Link();
					if (reportFile != null) {
						link = new Link(getText(this.iwrb.getLocalizedString(
								"recreate", "Re-create")));
					} else {
						link = new Link(getText(this.iwrb.getLocalizedString(
								"create", "Create")));
					}
					link.addParameter(PARAMETER_SURVEY_ID, this.survey
							.getPrimaryKey().toString());
					link.addParameter(PARAMETER_CREATE_EXCEL, "true");
					link.addParameter(Survey.PRM_SWITCHTO_MODE,
							Survey.MODE_RESULTS);
					table.add(getText(Text.NON_BREAKING_SPACE), 4, row);
					table.add(link, 4, row);
				}
			}
			++row;
			table.mergeCells(1, row, 4, row);
			table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_RIGHT);
			Link newStatus = new Link(getText(this.iwrb.getLocalizedString(
					"new_status", "New status")));
			newStatus.addParameter(PARAMETER_SURVEY_ID, this.survey
					.getPrimaryKey().toString());
			newStatus.addParameter(Survey.PRM_SWITCHTO_MODE,
					Survey.MODE_RESULTS);
			newStatus.addParameter(PARAMETER_NEW_STATUS, "true");
			table.add(newStatus, 1, row);

			if (warning) {
				++row;
				table.mergeCells(1, row, 4, row);
				table.add(getText("*"), 1, row);
				table.add(getText(this.iwrb.getLocalizedString(
						"excel_contains_old_data", "Excel contains old data")),
						1, row);
			}
		} else {
			add(getText(this.iwrb.getLocalizedString("no_status", "No status")));
		}
		return table;
	}

	private Table displayQuestions() throws RemoteException {
		Table table = new Table();
		int row = 1;

		if (this.questions != null && !this.questions.isEmpty()) {
			try {
				this.startMilli = System.currentTimeMillis();

				Iterator iter = this.questions.iterator();
				SurveyQuestion question;
				while (iter.hasNext()) {
					question = (SurveyQuestion) iter.next();
					row = displayQuestion(question, table, row);
				}
				log("[SurveyResultEditor] Total Time : "
						+ ((System.currentTimeMillis() - this.startMilli) / 1000)
						+ "s");
			} catch (IDOLookupException e) {
				e.printStackTrace();
			}
		} else {
			add(getText(this.iwrb.getLocalizedString("no_questions_defined",
					"No questions defined")));
		}

		return table;

	}

	private Table displayQuestions2() throws RemoteException {
		Table table = new Table();
		int row = 1;
		int column = 2;
		boolean firstQuestion = true;
		int columnMove = 0;

		if (this.questions != null && !this.questions.isEmpty()) {
			try {
				Iterator iter = this.questions.iterator();
				SurveyQuestion question;
				while (iter.hasNext()) {
					question = (SurveyQuestion) iter.next();
					String questionName = "";
					try {
						questionName = question.getQuestion(this.icLocale);
					} catch (FinderException e) {
						e.printStackTrace();
					}
					column = ((Integer) question.getPrimaryKey()).intValue();

					if (firstQuestion) {
						firstQuestion = false;
						columnMove = column - 2;
					}

					table.add(questionName, column - columnMove, row);
				}

				row++;
				HashMap participants = new HashMap();

				iter = this.questions.iterator();
				while (iter.hasNext()) {
					question = (SurveyQuestion) iter.next();
					try {
						Collection replies = this.sBusiness
								.getSurveyReplyHome().findByQuestion(question);
						Iterator it = replies.iterator();
						while (it.hasNext()) {
							SurveyReply reply = (SurveyReply) it.next();

							if (participants.containsKey(reply
									.getParticipantKey())) {
								ArrayList list = (ArrayList) participants
										.get(reply.getParticipantKey());
								list.add(reply);
							} else {
								ArrayList list = new ArrayList();
								list.add(reply);
								participants.put(reply.getParticipantKey(),
										list);
							}
						}
					} catch (FinderException e) {
					}
				}

				
				iter = participants.keySet().iterator();
				while (iter.hasNext()) {
					String key = (String) iter.next();
					ArrayList list = (ArrayList) participants.get(key);
					Iterator it = list.iterator();

					try {
						SurveyParticipant participant = this.sBusiness
								.getSurveyParticipantHome()
								.findParticipantByName(key, this.survey);
						table.add(participant.getUser().getDisplayName(), 1,
								row);
					} catch (Exception e1) {
						table.add(key, 1, row);
					}

					while (it.hasNext()) {
						SurveyReply reply = (SurveyReply) it.next();
						column = ((Integer) reply.getQuestion().getPrimaryKey())
								.intValue();
						
						if (reply.getSurveyAnswer() == null) {
							table.add(reply.getAnswer(), column - columnMove,
									row);
						} else {
							String locAnswer = "";
							try {
								locAnswer = reply.getSurveyAnswer().getAnswer(
										this.icLocale);
							} catch (FinderException e) {
								e.printStackTrace();
							}
							table.add(locAnswer, column - columnMove, row);
						}
					}

					row++;
				}

			} catch (IDOLookupException e) {
				e.printStackTrace();
			}
		} else {
			add(getText(this.iwrb.getLocalizedString("no_questions_defined",
					"No questions defined")));
		}

		System.out.println("Done creating file");

		return table;

	}

	private void createFile() throws RemoteException {
		Table table = displayQuestions2();
		ICFile icFile = POIUtility.createICFileFromTable(table,
				"SurveyResults.xls", "SurveyResults");

		if (icFile != null) {
			this.status.setReportFile(icFile);
			this.status.store();
			try {
				this.status = this.sBusiness.getSurveyStatus(this.survey);
				this.allStatuses = this.sBusiness.getSurveyStatusHome()
						.findAllBySurvey(this.survey);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			add(getText(this.iwrb.getLocalizedString("file_creation_failed",
					"File creation failed")));
		}
	}

	private int displayQuestion(SurveyQuestion question, Table table, int row)
			throws RemoteException {
		try {
			Collection answers = getAnswerHome().findQuestionsAnswer(question);
			String questionName = question.getQuestion(this.icLocale);

			int column = 1;
			int[] totals;
			table.add(questionName, column, row);
			boolean choiceAnswer = SurveyBusinessBean.ANSWERTYPE_TEXTAREA != question
					.getAnswerType();
			boolean isCheckBox = SurveyBusinessBean.ANSWERTYPE_MULTI_CHOICE == question
					.getAnswerType();
			Object[] answersIds = new Object[] {};
			if (answers != null) {

				if (choiceAnswer) {
					table.add(getText(this.iwrb.getLocalizedString("total",
							"Total")
							+ ":"), column, (row + 1));
				}

				Iterator iter = answers.iterator();
				answersIds = new Object[answers.size() + 2]; // 0 and 1 are not
																// used
				SurveyAnswer answer;
				int count = 0;
				int totalCount = getReplyHome().getCountByQuestion(question);
				while (iter.hasNext()) {
					answer = (SurveyAnswer) iter.next();
					answersIds[++column] = answer.getPrimaryKey();
					table.add(answer.getAnswer(this.icLocale), column, row);
					if (choiceAnswer) {
						count = getReplyHome().getCountByQuestionAndAnswer(
								question, answer);
						table.add(getText(Integer.toString(count)), column,
								(row + 1));
						if (totalCount > 0) {
							table.add(getText(this.nf.format((double) count
									/ (double) totalCount)), column, (row + 2));
						}
					}
				}
				if (choiceAnswer) {
					++row;
					++row;
				}
			}

			if (!choiceAnswer) {
				Collection replys = getReplyHome().findByQuestion(question);
				if (replys != null) {
					Iterator iter = replys.iterator();
					SurveyReply reply;
					Object primaryKey;
					String lastParticipant = "";
					String participant = "";
					totals = new int[answersIds.length];
					while (iter.hasNext()) {
						reply = (SurveyReply) iter.next();
						if (isCheckBox) {
							participant = reply.getParticipantKey();
							if (participant == null
									|| !participant.equals(lastParticipant)) {
								lastParticipant = participant;
							}
						} else {
							++row;
						}
						primaryKey = reply.getSurveyAnswer() != null ? reply
								.getSurveyAnswer().getPrimaryKey() : "";
						for (int i = 2; i < answersIds.length; i++) {
							if (answersIds[i].equals(primaryKey)) {
								if (choiceAnswer) {
									totals[i] += 1;
									break;
								} else {
									table.add(getText(reply.getAnswer()), i,
											row);
									break;
								}
							}
						}
					}
					if (choiceAnswer) {
						++row;
						table.add("Totals", 1, row);
						for (int i = 2; i < answersIds.length; i++) {
							table.add(Integer.toString(totals[i]), i, row);
						}
					}
				}
			}

			++row;
		} catch (IDOLookupException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		} catch (IDOException e) {
			e.printStackTrace();
		}

		return row;
	}

	private Text getText(String text) {
		return (Text) getMessageTextObject(text, false);
	}

	private Text getHeader(String text) {
		return (Text) getMessageTextObject(text, true);
	}

	private PresentationObject getMessageTextObject(String message,
			boolean highlight) {
		Text text = new Text(message);
		if (!highlight) {
			if (this.messageTextStyle != null) {
				text.setStyleAttribute(this.messageTextStyle);
			}
		} else {
			if (this.messageTextHighlightStyle != null) {
				text.setStyleAttribute(this.messageTextHighlightStyle);
			}
		}
		return text;
	}

	public void setMessageTextStyle(String style) {
		this.messageTextStyle = style;
	}

	public void setMessageTextHighlightStyle(String style) {
		this.messageTextHighlightStyle = style;
	}

	protected SurveyReplyHome getReplyHome() throws IDOLookupException {
		if (this.repHome == null) {
			this.repHome = (SurveyReplyHome) IDOLookup
					.getHome(SurveyReply.class);
		}
		return this.repHome;
	}

	protected SurveyAnswerHome getAnswerHome() throws IDOLookupException {
		if (this.ansHome == null) {
			this.ansHome = (SurveyAnswerHome) IDOLookup
					.getHome(SurveyAnswer.class);
		}
		return this.ansHome;
	}
}