package com.idega.block.survey.data;


import com.idega.core.localisation.data.ICLocale;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import com.idega.data.IDOLookupException;
import com.idega.user.data.User;
import com.idega.data.IDOEntity;

public interface SurveyQuestion extends IDOEntity {
	/**
	 * @see com.idega.block.survey.data.SurveyQuestionBMPBean#store
	 */
	public void store();

	/**
	 * @see com.idega.block.survey.data.SurveyQuestionBMPBean#getAnswerType
	 */
	public char getAnswerType();

	/**
	 * @see com.idega.block.survey.data.SurveyQuestionBMPBean#getCreationLocale
	 */
	public ICLocale getCreationLocale();

	/**
	 * @see com.idega.block.survey.data.SurveyQuestionBMPBean#getQuestion
	 */
	public String getQuestion(ICLocale locale) throws IDOLookupException,
			FinderException;

	/**
	 * @see com.idega.block.survey.data.SurveyQuestionBMPBean#getDependantOnQuestion
	 */
	public SurveyQuestion getDependantOnQuestion();

	/**
	 * @see com.idega.block.survey.data.SurveyQuestionBMPBean#getHasDependantQuestions
	 */
	public boolean getHasDependantQuestions();

	/**
	 * @see com.idega.block.survey.data.SurveyQuestionBMPBean#getQuestionDisplayNumber
	 */
	public String getQuestionDisplayNumber();

	/**
	 * @see com.idega.block.survey.data.SurveyQuestionBMPBean#setAnswerType
	 */
	public void setAnswerType(char value);

	/**
	 * @see com.idega.block.survey.data.SurveyQuestionBMPBean#setQuestion
	 */
	public void setQuestion(String question, ICLocale locale)
			throws IDOLookupException, CreateException;

	/**
	 * @see com.idega.block.survey.data.SurveyQuestionBMPBean#setCreationLocale
	 */
	public void setCreationLocale(ICLocale locale);

	/**
	 * @see com.idega.block.survey.data.SurveyQuestionBMPBean#setRemoved
	 */
	public void setRemoved(User user);

	/**
	 * @see com.idega.block.survey.data.SurveyQuestionBMPBean#setDependantOnQuestion
	 */
	public void setDependantOnQuestion(SurveyQuestion question);

	/**
	 * @see com.idega.block.survey.data.SurveyQuestionBMPBean#setHasDepandantQuestions
	 */
	public void setHasDepandantQuestions(boolean hasDependantQuestions);

	/**
	 * @see com.idega.block.survey.data.SurveyQuestionBMPBean#setQuestionDisplayNumber
	 */
	public void setQuestionDisplayNumber(String displayNumber);
}