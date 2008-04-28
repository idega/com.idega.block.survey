package com.idega.block.survey.data;


import java.util.Collection;
import javax.ejb.CreateException;
import com.idega.data.IDOHome;
import javax.ejb.FinderException;

public interface SurveyAnswerHome extends IDOHome {
	public SurveyAnswer create() throws CreateException;

	public SurveyAnswer findByPrimaryKey(Object pk) throws FinderException;

	public Collection findQuestionsAnswer(SurveyQuestion question)
			throws FinderException;
}