package com.idega.block.survey.data;


import com.idega.core.localisation.data.ICLocale;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import com.idega.data.IDOLookupException;
import com.idega.user.data.User;
import com.idega.data.IDOEntity;

public interface SurveyAnswer extends IDOEntity {
	/**
	 * @see com.idega.block.survey.data.SurveyAnswerBMPBean#getSurveyQuestion
	 */
	public SurveyQuestion getSurveyQuestion();

	/**
	 * @see com.idega.block.survey.data.SurveyAnswerBMPBean#setSurveyQuestion
	 */
	public void setSurveyQuestion(SurveyQuestion question);

	/**
	 * @see com.idega.block.survey.data.SurveyAnswerBMPBean#getAnswer
	 */
	public String getAnswer(ICLocale locale) throws IDOLookupException, FinderException;

	/**
	 * @see com.idega.block.survey.data.SurveyAnswerBMPBean#setAnswer
	 */
	public void setAnswer(String question, ICLocale locale) throws IDOLookupException, CreateException;

	/**
	 * @see com.idega.block.survey.data.SurveyAnswerBMPBean#useTextInput
	 */
	public boolean useTextInput();

	/**
	 * @see com.idega.block.survey.data.SurveyAnswerBMPBean#setToUseTextInput
	 */
	public void setToUseTextInput(boolean value);

	/**
	 * @see com.idega.block.survey.data.SurveyAnswerBMPBean#setToUseTextInput
	 */
	public void setToUseTextInput(Boolean value);

	/**
	 * @see com.idega.block.survey.data.SurveyAnswerBMPBean#setCreationLocale
	 */
	public void setCreationLocale(ICLocale locale);

	/**
	 * @see com.idega.block.survey.data.SurveyAnswerBMPBean#getCreationLocale
	 */
	public ICLocale getCreationLocale();

	/**
	 * @see com.idega.block.survey.data.SurveyAnswerBMPBean#store
	 */
	public void store();

	/**
	 * @see com.idega.block.survey.data.SurveyAnswerBMPBean#setRemoved
	 */
	public void setRemoved(User user);

	/**
	 * @see com.idega.block.survey.data.SurveyAnswerBMPBean#setCorrectAnswer
	 */
	public void setCorrectAnswer(boolean isCorrect);

	/**
	 * @see com.idega.block.survey.data.SurveyAnswerBMPBean#getIsCorrectAnswer
	 */
	public boolean getIsCorrectAnswer();
}