package com.idega.block.survey.data;


import com.idega.core.localisation.data.ICLocale;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDORelationshipException;
import java.util.Collection;
import com.idega.block.category.data.ICInformationCategory;
import com.idega.user.data.User;
import com.idega.block.category.data.ICInformationFolder;
import com.idega.data.IDORemoveRelationshipException;
import java.sql.Timestamp;
import com.idega.data.IDOEntity;

public interface SurveyEntity extends IDOEntity {
	/**
	 * @see com.idega.block.survey.data.SurveyEntityBMPBean#getName
	 */
	public String getName();

	/**
	 * @see com.idega.block.survey.data.SurveyEntityBMPBean#getDescription
	 */
	public String getDescription();

	/**
	 * @see com.idega.block.survey.data.SurveyEntityBMPBean#setName
	 */
	public void setName(String name);

	/**
	 * @see com.idega.block.survey.data.SurveyEntityBMPBean#setDescription
	 */
	public void setDescription(String description);

	/**
	 * @see com.idega.block.survey.data.SurveyEntityBMPBean#getStartTime
	 */
	public Timestamp getStartTime();

	/**
	 * @see com.idega.block.survey.data.SurveyEntityBMPBean#getEndTime
	 */
	public Timestamp getEndTime();

	/**
	 * @see com.idega.block.survey.data.SurveyEntityBMPBean#setStartTime
	 */
	public void setStartTime(Timestamp time);

	/**
	 * @see com.idega.block.survey.data.SurveyEntityBMPBean#setEndTime
	 */
	public void setEndTime(Timestamp time);

	/**
	 * @see com.idega.block.survey.data.SurveyEntityBMPBean#getCanUserAnswerMoreThanOnce
	 */
	public boolean getCanUserAnswerMoreThanOnce();

	/**
	 * @see com.idega.block.survey.data.SurveyEntityBMPBean#setCanUserAnswerMoreThanOnce
	 */
	public void setCanUserAnswerMoreThanOnce(boolean canAnswerMultipleTimes);

	/**
	 * @see com.idega.block.survey.data.SurveyEntityBMPBean#addQuestion
	 */
	public void addQuestion(SurveyQuestion question)
			throws IDOAddRelationshipException;

	/**
	 * @see com.idega.block.survey.data.SurveyEntityBMPBean#removeQuestion
	 */
	public void removeQuestion(SurveyQuestion question)
			throws IDORemoveRelationshipException;

	/**
	 * @see com.idega.block.survey.data.SurveyEntityBMPBean#getFolderID
	 */
	public int getFolderID();

	/**
	 * @see com.idega.block.survey.data.SurveyEntityBMPBean#getCategoryID
	 */
	public int getCategoryID();

	/**
	 * @see com.idega.block.survey.data.SurveyEntityBMPBean#setFolder
	 */
	public void setFolder(ICInformationFolder folder);

	/**
	 * @see com.idega.block.survey.data.SurveyEntityBMPBean#setCategoryID
	 */
	public void setCategoryID(ICInformationCategory categoryID);

	/**
	 * @see com.idega.block.survey.data.SurveyEntityBMPBean#getSurveyQuestions
	 */
	public Collection getSurveyQuestions() throws IDORelationshipException;

	/**
	 * @see com.idega.block.survey.data.SurveyEntityBMPBean#setCreationLocale
	 */
	public void setCreationLocale(ICLocale locale);

	/**
	 * @see com.idega.block.survey.data.SurveyEntityBMPBean#getCreationLocale
	 */
	public ICLocale getCreationLocale();

	/**
	 * @see com.idega.block.survey.data.SurveyEntityBMPBean#store
	 */
	public void store();

	/**
	 * @see com.idega.block.survey.data.SurveyEntityBMPBean#setRemoved
	 */
	public void setRemoved(User user);

	/**
	 * @see com.idega.block.survey.data.SurveyEntityBMPBean#setSurveyType
	 */
	public void setSurveyType(SurveyType type);

	/**
	 * @see com.idega.block.survey.data.SurveyEntityBMPBean#getSurveyType
	 */
	public SurveyType getSurveyType();
}