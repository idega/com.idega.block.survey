package com.idega.block.survey.data;


import com.idega.data.IDOException;
import java.util.Collection;
import javax.ejb.CreateException;
import com.idega.data.IDOHome;
import javax.ejb.FinderException;
import com.idega.user.data.User;

public interface SurveyParticipantHome extends IDOHome {
	public SurveyParticipant create() throws CreateException;

	public SurveyParticipant findByPrimaryKey(Object pk) throws FinderException;

	public int getNumberOfParticipations(SurveyEntity survey, String name)
			throws IDOException;

	public Collection findRandomParticipants(SurveyEntity survey,
			int maxNumberOfReturnedParticipants, boolean evenChance)
			throws FinderException;

	public SurveyParticipant findParticipant(SurveyEntity survey, User user)
			throws FinderException;
}