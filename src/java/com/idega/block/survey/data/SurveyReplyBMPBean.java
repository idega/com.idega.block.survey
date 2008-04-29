/*
 * Created on 2.1.2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.idega.block.survey.data;

import java.sql.Timestamp;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.block.survey.business.SurveyConstants;
import com.idega.data.GenericEntity;
import com.idega.data.IDOException;
import com.idega.data.query.CountColumn;
import com.idega.data.query.InCriteria;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.Table;
import com.idega.data.query.WildCardColumn;

/**
 * Title: SurveyReplyBMPBean Description: Copyright: Copyright (c) 2004 Company:
 * idega Software
 * 
 * @author 2004 - idega team - <br>
 *         <a href="mailto:gummi@idega.is">Gudmundur Agust Saemundsson</a><br>
 * @version 1.0
 */

public class SurveyReplyBMPBean extends GenericEntity implements SurveyReply {

	private static final String ENTITY_NAME = "SU_SURVEY_REPLY";
	// PARTICIPANT_KEY could either be generated key or encrypted representative
	// string (e.g. e-mail)
	private final static String COLUMN_PARTICIPANT_KEY = "PARTICIPANT_KEY";
	private static final String COLUMN_ANSWER = "ANSWER";
	private final static String COLUMN_SURVEY_ID = "SU_SURVEY_ID";
	private final static String COLUMN_QUESTION_ID = "SU_QUESTION_ID";
	private final static String COLUMN_ANSWER_ID = "SU_ANSWER_ID";
	private final static String COLUMN_PARTICIPANT = "participant_id";
	private final static String COLUMN_ENTRY_DATE = "entry_date";

	public String getEntityName() {
		return ENTITY_NAME;
	}

	public void initializeAttributes() {
		addAttribute(this.getIDColumnName());

		addAttribute(COLUMN_PARTICIPANT_KEY, "Participant key", String.class,
				255);
		addManyToOneRelationship(COLUMN_SURVEY_ID, SurveyEntity.class);
		addManyToOneRelationship(COLUMN_QUESTION_ID, SurveyQuestion.class);

		addManyToOneRelationship(COLUMN_ANSWER_ID, SurveyAnswer.class);
		addAttribute(COLUMN_ANSWER, "Answer", String.class,
				SurveyConstants.SURVEY_ANSWER_MAX_LENGTH);
		addManyToOneRelationship(COLUMN_PARTICIPANT, SurveyParticipant.class);
		addAttribute(COLUMN_ENTRY_DATE, "Entry date", Timestamp.class);
	}

	// setters
	public void setParticipantKey(String key) {
		setColumn(COLUMN_PARTICIPANT_KEY, key);
	}

	public void setAnswer(String answer) {
		setColumn(COLUMN_ANSWER, answer);
	}

	public void setAnswer(SurveyAnswer answer) {
		setColumn(COLUMN_ANSWER_ID, answer);
	}

	public void setSurvey(SurveyEntity survey) {
		setColumn(COLUMN_SURVEY_ID, survey);
	}

	public void setQuestion(SurveyQuestion question) {
		setColumn(COLUMN_QUESTION_ID, question);
	}

	public void setAnswerPK(Object answer) {
		setColumn(COLUMN_ANSWER_ID, answer);
	}

	public void setSurveyPK(Object survey) {
		setColumn(COLUMN_SURVEY_ID, survey);
	}

	public void setQuestionPK(Object question) {
		setColumn(COLUMN_QUESTION_ID, question);
	}

	public void setParticipant(SurveyParticipant participant) {
		setColumn(COLUMN_PARTICIPANT, participant);
	}

	public void setEntryDate(Timestamp entryDate) {
		setColumn(COLUMN_ENTRY_DATE, entryDate);
	}
	
	// getters
	public String getParticipantKey() {
		return getStringColumnValue(COLUMN_PARTICIPANT_KEY);
	}

	public String getAnswer() {
		return getStringColumnValue(COLUMN_ANSWER);
	}

	public SurveyAnswer getSurveyAnswer() {
		return (SurveyAnswer) getColumnValue(COLUMN_ANSWER_ID);
	}

	public SurveyEntity getSurvey() {
		return (SurveyEntity) getColumnValue(COLUMN_SURVEY_ID);
	}

	public SurveyQuestion getQuestion() {
		return (SurveyQuestion) getColumnValue(COLUMN_QUESTION_ID);
	}

	public SurveyParticipant getParticipant() {
		return (SurveyParticipant) getColumnValue(COLUMN_PARTICIPANT);
	}

	public Timestamp getEntryDate() {
		return getTimestampColumnValue(COLUMN_ENTRY_DATE);
	}
	
	// ejb
	public Collection ejbFindByQuestion(SurveyQuestion question)
			throws FinderException {
		Table table = new Table(this);
		SelectQuery selectQuery = new SelectQuery(table);
		selectQuery.addColumn(new WildCardColumn(table));
		selectQuery
				.addCriteria(new MatchCriteria(table
						.getColumn(COLUMN_QUESTION_ID), MatchCriteria.EQUALS,
						question));
		selectQuery.addOrder(table, getIDColumnName(), true);

		Collection coll = this.idoFindPKsBySQL(selectQuery.toString());

		return coll;
	}

	public Collection ejbFindBySurveyAndParticipant(SurveyEntity survey, SurveyParticipant participant)
			throws FinderException {
		Table table = new Table(this);
		SelectQuery selectQuery = new SelectQuery(table);
		selectQuery.addColumn(new WildCardColumn(table));
		selectQuery
				.addCriteria(new MatchCriteria(table
						.getColumn(COLUMN_SURVEY_ID), MatchCriteria.EQUALS,
						survey));
		selectQuery
		.addCriteria(new MatchCriteria(table
				.getColumn(COLUMN_PARTICIPANT), MatchCriteria.EQUALS,
				participant));

		
		return idoFindPKsBySQL(selectQuery.toString());
	}

	public Collection ejbFindByQuestions(Collection questions)
			throws FinderException {
		Table table = new Table(this);
		SelectQuery selectQuery = new SelectQuery(table);
		selectQuery.addColumn(new WildCardColumn(table));
		selectQuery.addCriteria(new InCriteria(table
				.getColumn(COLUMN_QUESTION_ID), questions));

		Collection coll = this.idoFindPKsBySQL(selectQuery.toString());

		return coll;
	}

	public int ejbHomeGetCountByQuestionAndAnswer(SurveyQuestion question,
			SurveyAnswer answer) throws IDOException {
		Table table = new Table(this);
		SelectQuery query = new SelectQuery(table);
		query.addColumn(new CountColumn(table, COLUMN_QUESTION_ID));
		query.addCriteria(new MatchCriteria(
				table.getColumn(COLUMN_QUESTION_ID), MatchCriteria.EQUALS,
				question));
		query.addCriteria(new MatchCriteria(table.getColumn(COLUMN_ANSWER_ID),
				MatchCriteria.EQUALS, answer));

		return this.idoGetNumberOfRecords(query.toString());
	}

	public int ejbHomeGetCountByQuestion(SurveyQuestion question)
			throws IDOException {
		Table table = new Table(this);
		SelectQuery query = new SelectQuery(table);
		query.addColumn(new CountColumn(table, COLUMN_QUESTION_ID));
		query.addCriteria(new MatchCriteria(
				table.getColumn(COLUMN_QUESTION_ID), MatchCriteria.EQUALS,
				question));

		return this.idoGetNumberOfRecords(query.toString());
	}
}