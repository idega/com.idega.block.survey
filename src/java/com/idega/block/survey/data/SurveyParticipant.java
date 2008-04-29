package com.idega.block.survey.data;


import com.idega.user.data.User;
import java.sql.Timestamp;
import com.idega.data.IDOEntity;

public interface SurveyParticipant extends IDOEntity {
	/**
	 * @see com.idega.block.survey.data.SurveyParticipantBMPBean#setParticipantName
	 */
	public void setParticipantName(String name);

	/**
	 * @see com.idega.block.survey.data.SurveyParticipantBMPBean#setSurvey
	 */
	public void setSurvey(SurveyEntity survey);

	/**
	 * @see com.idega.block.survey.data.SurveyParticipantBMPBean#setUser
	 */
	public void setUser(User user);

	/**
	 * @see com.idega.block.survey.data.SurveyParticipantBMPBean#setEntryDate
	 */
	public void setEntryDate(Timestamp entryDate);

	/**
	 * @see com.idega.block.survey.data.SurveyParticipantBMPBean#getParticipantName
	 */
	public String getParticipantName();

	/**
	 * @see com.idega.block.survey.data.SurveyParticipantBMPBean#getSurvey
	 */
	public SurveyEntity getSurvey();

	/**
	 * @see com.idega.block.survey.data.SurveyParticipantBMPBean#getUser
	 */
	public User getUser();

	/**
	 * @see com.idega.block.survey.data.SurveyParticipantBMPBean#getEntryDate
	 */
	public Timestamp getEntryDate();
}