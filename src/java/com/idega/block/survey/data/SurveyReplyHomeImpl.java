package com.idega.block.survey.data;


import com.idega.data.IDOException;
import java.util.Collection;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import com.idega.data.IDOEntity;
import com.idega.data.IDOFactory;

public class SurveyReplyHomeImpl extends IDOFactory implements SurveyReplyHome {
	public Class getEntityInterfaceClass() {
		return SurveyReply.class;
	}

	public SurveyReply create() throws CreateException {
		return (SurveyReply) super.createIDO();
	}

	public SurveyReply findByPrimaryKey(Object pk) throws FinderException {
		return (SurveyReply) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findByQuestion(SurveyQuestion question)
			throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((SurveyReplyBMPBean) entity)
				.ejbFindByQuestion(question);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findBySurveyAndParticipant(SurveyEntity survey,
			SurveyParticipant participant) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((SurveyReplyBMPBean) entity)
				.ejbFindBySurveyAndParticipant(survey, participant);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findByQuestions(Collection questions)
			throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((SurveyReplyBMPBean) entity)
				.ejbFindByQuestions(questions);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public int getCountByQuestionAndAnswer(SurveyQuestion question,
			SurveyAnswer answer) throws IDOException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		int theReturn = ((SurveyReplyBMPBean) entity)
				.ejbHomeGetCountByQuestionAndAnswer(question, answer);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

	public int getCountByQuestion(SurveyQuestion question) throws IDOException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		int theReturn = ((SurveyReplyBMPBean) entity)
				.ejbHomeGetCountByQuestion(question);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}
}