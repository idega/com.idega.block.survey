package com.idega.block.survey.data;


import com.idega.data.IDOException;
import java.util.Collection;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import com.idega.user.data.User;
import com.idega.data.IDOEntity;
import com.idega.data.IDOFactory;

public class SurveyParticipantHomeImpl extends IDOFactory implements
		SurveyParticipantHome {
	public Class getEntityInterfaceClass() {
		return SurveyParticipant.class;
	}

	public SurveyParticipant create() throws CreateException {
		return (SurveyParticipant) super.createIDO();
	}

	public SurveyParticipant findByPrimaryKey(Object pk) throws FinderException {
		return (SurveyParticipant) super.findByPrimaryKeyIDO(pk);
	}

	public int getNumberOfParticipations(SurveyEntity survey, String name)
			throws IDOException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		int theReturn = ((SurveyParticipantBMPBean) entity)
				.ejbHomeGetNumberOfParticipations(survey, name);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

	public Collection findRandomParticipants(SurveyEntity survey,
			int maxNumberOfReturnedParticipants, boolean evenChance)
			throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((SurveyParticipantBMPBean) entity)
				.ejbFindRandomParticipants(survey,
						maxNumberOfReturnedParticipants, evenChance);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public SurveyParticipant findParticipant(SurveyEntity survey, User user)
			throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((SurveyParticipantBMPBean) entity).ejbFindParticipant(
				survey, user);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public SurveyParticipant findParticipantByName(String name,
			SurveyEntity survey) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((SurveyParticipantBMPBean) entity)
				.ejbFindParticipantByName(name, survey);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}
}