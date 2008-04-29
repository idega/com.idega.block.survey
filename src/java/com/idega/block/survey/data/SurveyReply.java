package com.idega.block.survey.data;


import java.sql.Timestamp;
import com.idega.data.IDOEntity;

public interface SurveyReply extends IDOEntity {
	/**
	 * @see com.idega.block.survey.data.SurveyReplyBMPBean#setParticipantKey
	 */
	public void setParticipantKey(String key);

	/**
	 * @see com.idega.block.survey.data.SurveyReplyBMPBean#setAnswer
	 */
	public void setAnswer(String answer);

	/**
	 * @see com.idega.block.survey.data.SurveyReplyBMPBean#setAnswer
	 */
	public void setAnswer(SurveyAnswer answer);

	/**
	 * @see com.idega.block.survey.data.SurveyReplyBMPBean#setSurvey
	 */
	public void setSurvey(SurveyEntity survey);

	/**
	 * @see com.idega.block.survey.data.SurveyReplyBMPBean#setQuestion
	 */
	public void setQuestion(SurveyQuestion question);

	/**
	 * @see com.idega.block.survey.data.SurveyReplyBMPBean#setAnswerPK
	 */
	public void setAnswerPK(Object answer);

	/**
	 * @see com.idega.block.survey.data.SurveyReplyBMPBean#setSurveyPK
	 */
	public void setSurveyPK(Object survey);

	/**
	 * @see com.idega.block.survey.data.SurveyReplyBMPBean#setQuestionPK
	 */
	public void setQuestionPK(Object question);

	/**
	 * @see com.idega.block.survey.data.SurveyReplyBMPBean#setParticipant
	 */
	public void setParticipant(SurveyParticipant participant);

	/**
	 * @see com.idega.block.survey.data.SurveyReplyBMPBean#setEntryDate
	 */
	public void setEntryDate(Timestamp entryDate);

	/**
	 * @see com.idega.block.survey.data.SurveyReplyBMPBean#getParticipantKey
	 */
	public String getParticipantKey();

	/**
	 * @see com.idega.block.survey.data.SurveyReplyBMPBean#getAnswer
	 */
	public String getAnswer();

	/**
	 * @see com.idega.block.survey.data.SurveyReplyBMPBean#getSurveyAnswer
	 */
	public SurveyAnswer getSurveyAnswer();

	/**
	 * @see com.idega.block.survey.data.SurveyReplyBMPBean#getSurvey
	 */
	public SurveyEntity getSurvey();

	/**
	 * @see com.idega.block.survey.data.SurveyReplyBMPBean#getQuestion
	 */
	public SurveyQuestion getQuestion();

	/**
	 * @see com.idega.block.survey.data.SurveyReplyBMPBean#getParticipant
	 */
	public SurveyParticipant getParticipant();

	/**
	 * @see com.idega.block.survey.data.SurveyReplyBMPBean#getEntryDate
	 */
	public Timestamp getEntryDate();
}