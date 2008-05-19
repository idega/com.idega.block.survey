/*
 * Created on 18.1.2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.idega.block.survey.data;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.Vector;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.IDOException;
import com.idega.data.IDOQuery;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.Table;
import com.idega.user.data.User;
import com.idega.util.ListUtil;

/**
 * Title: SurveyParticipant Description: Copyright: Copyright (c) 2004 Company:
 * idega Software
 * 
 * @author 2004 - idega team - <br>
 *         <a href="mailto:gummi@idega.is">Gudmundur Agust Saemundsson</a><br>
 * @version 1.0
 */
public class SurveyParticipantBMPBean extends GenericEntity implements SurveyParticipant {

	public static final String COLUMN_NAME = "PARTICIPANT_NAME";
	public static final String COLUMN_SURVEY = "SU_SURVEY_ID";
	protected static final String COLUMN_USER = "user_id";
	protected static final String COLUMN_ENTRY_DATE = "entry_date";

	/**
	 * 
	 */
	public SurveyParticipantBMPBean() {
		super();
	}

	public String getEntityName() {
		return "SU_SURVEY_PARTICIPANT";
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(COLUMN_NAME, "Participant name", String.class);
		addManyToOneRelationship(COLUMN_USER, User.class);

		addManyToOneRelationship(COLUMN_SURVEY, SurveyEntity.class);
		addAttribute(COLUMN_ENTRY_DATE, "Entry date", Timestamp.class);
	}

	// setters
	public void setParticipantName(String name) {
		setColumn(COLUMN_NAME, name);
	}

	public void setSurvey(SurveyEntity survey) {
		setColumn(COLUMN_SURVEY, survey);
	}

	public void setUser(User user) {
		setColumn(COLUMN_USER, user);
	}

	public void setEntryDate(Timestamp entryDate) {
		setColumn(COLUMN_ENTRY_DATE, entryDate);
	}

	// getters
	public String getParticipantName() {
		return getStringColumnValue(COLUMN_NAME);
	}

	public SurveyEntity getSurvey() {
		return (SurveyEntity) getColumnValue(COLUMN_SURVEY);
	}

	public User getUser() {
		return (User) getColumnValue(COLUMN_USER);
	}

	public Timestamp getEntryDate() {
		return getTimestampColumnValue(COLUMN_ENTRY_DATE);
	}

	// ejb
	public int ejbHomeGetNumberOfParticipations(SurveyEntity survey, String name) throws IDOException {
		IDOQuery query = idoQueryGetSelectCount();
		query.appendWhereEquals(COLUMN_SURVEY, survey);
		query.appendAndEquals(COLUMN_NAME, name);

		return idoGetNumberOfRecords(query);
	}

	public Collection ejbFindRandomParticipants(SurveyEntity survey, int maxNumberOfReturnedParticipants, boolean evenChance) throws FinderException {
		Collection toReturn = new Vector();
		IDOQuery query = idoQueryGetSelect();
		query.appendWhereEquals(COLUMN_SURVEY, survey);
		query.appendAndIsNotNull(COLUMN_NAME);
		query.appendAnd();
		query.append(COLUMN_NAME);
		query.appendNOTLike();
		query.appendWithinSingleQuotes("");

		List pks = ListUtil.convertCollectionToList(idoFindPKsByQuery(query));

		if (pks.size() <= maxNumberOfReturnedParticipants) {
			return pks;
		}
		else {

			Set set = new HashSet();

			while (set.size() < maxNumberOfReturnedParticipants) {
				Random rand = new Random();
				int index = rand.nextInt(pks.size());
				boolean success = set.add(pks.get(index));
				int startIndex = index;
				boolean oneRound = false;
				while (!success) {
					if (startIndex == ++index) {
						oneRound = true;
						break;
					}
					if (index == pks.size()) {
						index = 0;
					}
					success = set.add(pks.get(index));
				}
				if (oneRound) {
					break;
				}
			}
			toReturn.addAll(set);
		}
		return toReturn;
	}

	public Object ejbFindParticipant(SurveyEntity survey, User user) throws FinderException {

		Table table = new Table(this);

		SelectQuery query = new SelectQuery(table);
		query.addColumn(table.getColumn(getIDColumnName()));
		query.addCriteria(new MatchCriteria(table.getColumn(COLUMN_SURVEY), MatchCriteria.EQUALS, survey));
		query.addCriteria(new MatchCriteria(table.getColumn(COLUMN_USER), MatchCriteria.EQUALS, user));

		return idoFindOnePKByQuery(query);
	}
	
	public Object ejbFindParticipantByName(String name, SurveyEntity survey) throws FinderException {

		Table table = new Table(this);

		SelectQuery query = new SelectQuery(table);
		query.addColumn(table.getColumn(getIDColumnName()));
		query.addCriteria(new MatchCriteria(table.getColumn(COLUMN_SURVEY), MatchCriteria.EQUALS, survey));
		query.addCriteria(new MatchCriteria(table.getColumn(COLUMN_NAME), MatchCriteria.EQUALS, name));

		return idoFindOnePKByQuery(query);
	}

}