package com.idega.block.survey.business;


import com.idega.block.survey.data.SurveyReply;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.core.localisation.data.ICLocale;
import javax.ejb.CreateException;
import com.idega.block.survey.data.SurveyStatus;
import com.idega.block.survey.data.SurveyEntityHome;
import com.idega.data.IDORemoveRelationshipException;
import com.idega.block.survey.data.SurveyAnswer;
import com.idega.business.IBOService;
import com.idega.block.survey.data.SurveyType;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.block.survey.data.SurveyStatusHome;
import com.idega.data.IDOLookupException;
import com.idega.user.data.User;
import com.idega.block.category.data.InformationFolder;
import com.idega.block.survey.data.SurveyAnswerHome;
import com.idega.block.survey.data.SurveyReplyHome;
import java.rmi.RemoteException;
import com.idega.block.survey.data.SurveyQuestionHome;
import com.idega.block.survey.data.SurveyParticipant;
import com.idega.data.IDOAddRelationshipException;
import com.idega.block.survey.data.SurveyTypeHome;
import java.util.Collection;
import com.idega.block.survey.data.SurveyQuestion;
import com.idega.block.survey.data.SurveyParticipantHome;
import javax.ejb.FinderException;
import com.idega.util.IWTimestamp;
import com.idega.block.survey.data.SurveyEntity;

public interface SurveyBusiness extends IBOService {
	/**
	 * @see com.idega.block.survey.business.SurveyBusinessBean#createSurvey
	 */
	public SurveyEntity createSurvey(InformationFolder folder, String name, String description, IWTimestamp startTime, IWTimestamp endTime, String surveyTypePK) throws IDOLookupException, CreateException, RemoteException;

	/**
	 * @see com.idega.block.survey.business.SurveyBusinessBean#createSurveyQuestion
	 */
	public SurveyQuestion createSurveyQuestion(SurveyEntity survey, String[] question, ICLocale[] locale, char answerType) throws IDOLookupException, CreateException, IDOAddRelationshipException, RemoteException;

	/**
	 * @see com.idega.block.survey.business.SurveyBusinessBean#createSurveyQuestion
	 */
	public SurveyQuestion createSurveyQuestion(SurveyEntity survey, String question, ICLocale locale, char answerType) throws IDOLookupException, IDOAddRelationshipException, CreateException, RemoteException;

	/**
	 * @see com.idega.block.survey.business.SurveyBusinessBean#createSurveyAnswer
	 */
	public SurveyAnswer createSurveyAnswer(SurveyQuestion question, String[] answer, ICLocale[] locale) throws IDOLookupException, CreateException, RemoteException;

	/**
	 * @see com.idega.block.survey.business.SurveyBusinessBean#createSurveyAnswer
	 */
	public SurveyAnswer createSurveyAnswer(SurveyQuestion question, String answer, ICLocale locale) throws IDOLookupException, CreateException, RemoteException;

	/**
	 * @see com.idega.block.survey.business.SurveyBusinessBean#createSurveyReply
	 */
	public SurveyReply createSurveyReply(SurveyEntity survey, SurveyQuestion question, String participantKey, SurveyAnswer answer, String answerText) throws IDOLookupException, CreateException, RemoteException;

	/**
	 * @see com.idega.block.survey.business.SurveyBusinessBean#updateSurveyQuestion
	 */
	public SurveyQuestion updateSurveyQuestion(SurveyEntity survey, SurveyQuestion question, String questionText, ICLocale locale, char type) throws IDOLookupException, CreateException, RemoteException;

	/**
	 * @see com.idega.block.survey.business.SurveyBusinessBean#updateSurveyAnswer
	 */
	public SurveyAnswer updateSurveyAnswer(SurveyAnswer ans, String answerString, ICLocale locale) throws IDOLookupException, CreateException, RemoteException;

	/**
	 * @see com.idega.block.survey.business.SurveyBusinessBean#removeQuestionFromSurvey
	 */
	public void removeQuestionFromSurvey(SurveyEntity survey, SurveyQuestion question, User user) throws IDORemoveRelationshipException, RemoteException;

	/**
	 * @see com.idega.block.survey.business.SurveyBusinessBean#removeAnswer
	 */
	public void removeAnswer(SurveyAnswer ans, User user) throws IDORemoveRelationshipException, RemoteException;

	/**
	 * @see com.idega.block.survey.business.SurveyBusinessBean#getAnswerHome
	 */
	public SurveyAnswerHome getAnswerHome() throws IDOLookupException, RemoteException;

	/**
	 * @see com.idega.block.survey.business.SurveyBusinessBean#getSurveyHome
	 */
	public SurveyEntityHome getSurveyHome() throws IDOLookupException, RemoteException;

	/**
	 * @see com.idega.block.survey.business.SurveyBusinessBean#getSurveyTypeHome
	 */
	public SurveyTypeHome getSurveyTypeHome() throws IDOLookupException, RemoteException;

	/**
	 * @see com.idega.block.survey.business.SurveyBusinessBean#getQuestionHome
	 */
	public SurveyQuestionHome getQuestionHome() throws IDOLookupException, RemoteException;

	/**
	 * @see com.idega.block.survey.business.SurveyBusinessBean#getSurveyReplyHome
	 */
	public SurveyReplyHome getSurveyReplyHome() throws IDOLookupException, RemoteException;

	/**
	 * @see com.idega.block.survey.business.SurveyBusinessBean#getSurveyParticipantHome
	 */
	public SurveyParticipantHome getSurveyParticipantHome() throws IDOLookupException, RemoteException;

	/**
	 * @see com.idega.block.survey.business.SurveyBusinessBean#getSurveyStatusHome
	 */
	public SurveyStatusHome getSurveyStatusHome() throws IDOLookupException, RemoteException;

	/**
	 * @see com.idega.block.survey.business.SurveyBusinessBean#reportParticipation
	 */
	public SurveyParticipant reportParticipation(SurveyEntity survey, String participant) throws IDOLookupException, CreateException, RemoteException;

	/**
	 * @see com.idega.block.survey.business.SurveyBusinessBean#getSurveyStatus
	 */
	public SurveyStatus getSurveyStatus(SurveyEntity survey) throws RemoteException;

	/**
	 * @see com.idega.block.survey.business.SurveyBusinessBean#getSurveyType
	 */
	public SurveyType getSurveyType(SurveyEntity survey) throws FinderException, RemoteException;

	/**
	 * @see com.idega.block.survey.business.SurveyBusinessBean#getSurveyTypeDropdownMenu
	 */
	public DropdownMenu getSurveyTypeDropdownMenu(IWResourceBundle iwrb, String name) throws RemoteException;

	/**
	 * @see com.idega.block.survey.business.SurveyBusinessBean#getSurveyTypes
	 */
	public Collection getSurveyTypes() throws RemoteException;
}