package com.idega.block.survey.data;


import javax.ejb.CreateException;
import javax.ejb.FinderException;
import com.idega.data.IDOFactory;

public class SurveyQuestionHomeImpl extends IDOFactory implements
		SurveyQuestionHome {
	public Class getEntityInterfaceClass() {
		return SurveyQuestion.class;
	}

	public SurveyQuestion create() throws CreateException {
		return (SurveyQuestion) super.createIDO();
	}

	public SurveyQuestion findByPrimaryKey(Object pk) throws FinderException {
		return (SurveyQuestion) super.findByPrimaryKeyIDO(pk);
	}
}