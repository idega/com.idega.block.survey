package com.idega.block.survey.data;


import com.idega.data.IDOEntity;

public interface SurveyType extends IDOEntity {
	/**
	 * @see com.idega.block.survey.data.SurveyTypeBMPBean#getName
	 */
	public String getName();

	/**
	 * @see com.idega.block.survey.data.SurveyTypeBMPBean#setName
	 */
	public void setName(String name);

	/**
	 * @see com.idega.block.survey.data.SurveyTypeBMPBean#getLocalizationKey
	 */
	public String getLocalizationKey();

	/**
	 * @see com.idega.block.survey.data.SurveyTypeBMPBean#setLocalizationKey
	 */
	public void setLocalizationKey(String key);

	/**
	 * @see com.idega.block.survey.data.SurveyTypeBMPBean#getDescription
	 */
	public String getDescription();

	/**
	 * @see com.idega.block.survey.data.SurveyTypeBMPBean#setDescription
	 */
	public void setDescription(String description);

	/**
	 * @see com.idega.block.survey.data.SurveyTypeBMPBean#getAnswerTypes
	 */
	public char[] getAnswerTypes();

	/**
	 * @see com.idega.block.survey.data.SurveyTypeBMPBean#setAnswerTypes
	 */
	public void setAnswerTypes(char[] types);

	/**
	 * @see com.idega.block.survey.data.SurveyTypeBMPBean#getAllowEmail
	 */
	public boolean getAllowEmail();

	/**
	 * @see com.idega.block.survey.data.SurveyTypeBMPBean#setAllowEmail
	 */
	public void setAllowEmail(boolean allow);

	/**
	 * @see com.idega.block.survey.data.SurveyTypeBMPBean#getAllowCheckBoxValue
	 */
	public boolean getAllowCheckBoxValue();

	/**
	 * @see com.idega.block.survey.data.SurveyTypeBMPBean#setAllowCheckBoxValue
	 */
	public void setAllowCheckBoxValue(boolean allow);

	/**
	 * @see com.idega.block.survey.data.SurveyTypeBMPBean#getAllowRadioButtonValue
	 */
	public boolean getAllowRadioButtonValue();

	/**
	 * @see com.idega.block.survey.data.SurveyTypeBMPBean#setAllowRadioButtonValue
	 */
	public void setAllowRadioButtonValue(boolean allow);

	/**
	 * @see com.idega.block.survey.data.SurveyTypeBMPBean#getShowGrade
	 */
	public boolean getShowGrade();

	/**
	 * @see com.idega.block.survey.data.SurveyTypeBMPBean#setShowGrade
	 */
	public void setShowGrade(boolean show);

	/**
	 * @see com.idega.block.survey.data.SurveyTypeBMPBean#getCustomizableGrade
	 */
	public boolean getCustomizableGrade();

	/**
	 * @see com.idega.block.survey.data.SurveyTypeBMPBean#setCustomizableGrade
	 */
	public void setCustomizableGrade(boolean settable);

	/**
	 * @see com.idega.block.survey.data.SurveyTypeBMPBean#getShowResults
	 */
	public boolean getShowResults();

	/**
	 * @see com.idega.block.survey.data.SurveyTypeBMPBean#setShowResults
	 */
	public void setShowResults(boolean show);
}