package com.idega.block.survey.data;


import java.util.Collection;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import com.idega.data.IDOEntity;
import com.idega.data.IDOFactory;

public class SurveyAnswerHomeImpl extends IDOFactory implements
		SurveyAnswerHome {
	public Class getEntityInterfaceClass() {
		return SurveyAnswer.class;
	}

	public SurveyAnswer create() throws CreateException {
		return (SurveyAnswer) super.createIDO();
	}

	public SurveyAnswer findByPrimaryKey(Object pk) throws FinderException {
		return (SurveyAnswer) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findQuestionsAnswer(SurveyQuestion question)
			throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((SurveyAnswerBMPBean) entity)
				.ejbFindQuestionsAnswer(question);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
}