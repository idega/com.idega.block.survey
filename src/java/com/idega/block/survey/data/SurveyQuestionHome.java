package com.idega.block.survey.data;


import javax.ejb.CreateException;
import com.idega.data.IDOHome;
import javax.ejb.FinderException;

public interface SurveyQuestionHome extends IDOHome {
	public SurveyQuestion create() throws CreateException;

	public SurveyQuestion findByPrimaryKey(Object pk) throws FinderException;
}