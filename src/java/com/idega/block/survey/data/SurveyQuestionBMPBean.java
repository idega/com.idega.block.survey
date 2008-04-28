package com.idega.block.survey.data;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.core.localisation.data.ICLocale;
import com.idega.data.GenericEntity;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;

/**
 * Title: SurveyQuestionBMPBean Description: Copyright: Copyright (c) 2004
 * Company: idega Software
 * 
 * @author 2004 - idega team - <br>
 *         <a href="mailto:gummi@idega.is">Gudmundur Agust Saemundsson</a><br>
 * @version 1.0
 */

public class SurveyQuestionBMPBean extends GenericEntity implements
		SurveyQuestion {

	protected static final String ENTITY_NAME = "SU_SURVEY_QUESTION";

	private HashMap storeMap = new HashMap();
	public static final String COLUMNNAME_CREATION_LOCALE = "CREATION_LOCALE";

	private final static String COLUMN_ANSWER_TYPE = "ANSWER_TYPE";
	private final static String COLUMN_DELETED = "DELETED";
	private final static String COLUMN_DELETED_BY = "DELETED_BY";
	private final static String COLUMN_DELETED_WHEN = "DELETED_WHEN";

	protected final static String COLUMN_DEPENDANT_ON_QUESTION = "dependant_on_question";
	protected final static String COLUMN_HAS_DEPENDANT_QUESTIONS = "has_dependant";
	protected final static String COLUMN_QUESTION_NUMBER = "question_number";

	public final static String DELETED = "Y";
	public final static String NOT_DELETED = "N";

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(COLUMN_ANSWER_TYPE, "Answer type", String.class, 1);
		setNullable(COLUMN_ANSWER_TYPE, false);
		addManyToOneRelationship(COLUMNNAME_CREATION_LOCALE, ICLocale.class);
		addAttribute(COLUMN_DELETED, "Deleted", String.class, 1);
		addManyToOneRelationship(COLUMN_DELETED_BY, User.class);
		addAttribute(COLUMN_DELETED_WHEN, "Deleted when", Timestamp.class);
		addManyToOneRelationship(COLUMN_DEPENDANT_ON_QUESTION,
				SurveyQuestion.class);
		addAttribute(COLUMN_HAS_DEPENDANT_QUESTIONS, "Has dependant questions", Boolean.class);
		addAttribute(COLUMN_QUESTION_NUMBER, "Question display number", String.class);
	}

	public String getEntityName() {
		return ENTITY_NAME;
	}

	public void store() {
		super.store();
		Collection translations = this.storeMap.values();
		for (Iterator iter = translations.iterator(); iter.hasNext();) {
			SurveyQuestionTranslation element = (SurveyQuestionTranslation) iter
					.next();
			element.setTransletedEntity(this);
			element.store();
		}
	}

	// getters
	public char getAnswerType() {
		return getCharColumnValue(COLUMN_ANSWER_TYPE);
	}

	public ICLocale getCreationLocale() {
		return (ICLocale) getColumnValue(COLUMNNAME_CREATION_LOCALE);
	}

	public String getQuestion(ICLocale locale) throws IDOLookupException,
			FinderException {
		SurveyQuestionTranslationHome sqtHome = (SurveyQuestionTranslationHome) IDOLookup
				.getHome(SurveyQuestionTranslation.class);
		SurveyQuestionTranslation qTR;
		try {
			qTR = sqtHome.findQuestionTranslation(this, locale);
		} catch (FinderException e) {
			qTR = sqtHome.findQuestionTranslation(this, this
					.getCreationLocale());
		}
		return qTR.getQuestion();
	}

	public SurveyQuestion getDependantOnQuestion() {
		return (SurveyQuestion) getColumnValue(COLUMN_DEPENDANT_ON_QUESTION);
	}
	
	public boolean getHasDependantQuestions() {
		return getBooleanColumnValue(COLUMN_HAS_DEPENDANT_QUESTIONS, false);
	}
	
	public String getQuestionDisplayNumber() {
		return getStringColumnValue(COLUMN_QUESTION_NUMBER);
	}
	
	// setters
	public void setAnswerType(char value) {
		setColumn(COLUMN_ANSWER_TYPE, value);
	}

	public void setQuestion(String question, ICLocale locale)
			throws IDOLookupException, CreateException {
		SurveyQuestionTranslationHome sqtHome = (SurveyQuestionTranslationHome) IDOLookup
				.getHome(SurveyQuestionTranslation.class);
		SurveyQuestionTranslation qTR = null;
		try {
			qTR = sqtHome.findQuestionTranslation(this, locale);
		} catch (FinderException e) {
			qTR = sqtHome.create();
			qTR.setLocale(locale);
			if (this.getCreationLocale() == null) {
				this.setCreationLocale(locale);
			}
		}

		qTR.setQuestion(question);

		this.storeMap.put(locale, qTR);
	}

	public void setCreationLocale(ICLocale locale) {
		setColumn(COLUMNNAME_CREATION_LOCALE, locale);
	}

	public void setRemoved(User user) {
		setColumn(COLUMN_DELETED, DELETED);
		setDeletedWhen(IWTimestamp.getTimestampRightNow());
		setDeletedBy(user);

		super.store();
	}

	private void setDeletedBy(User user) {
		setColumn(COLUMN_DELETED_BY, user);
	}

	private void setDeletedWhen(Timestamp when) {
		setColumn(COLUMN_DELETED_WHEN, when);
	}

	public void setDependantOnQuestion(SurveyQuestion question) {
		setColumn(COLUMN_DEPENDANT_ON_QUESTION, question);
	}
	
	public void setHasDepandantQuestions(boolean hasDependantQuestions) {
		setColumn(COLUMN_HAS_DEPENDANT_QUESTIONS, hasDependantQuestions);
	}
	
	public void setQuestionDisplayNumber(String displayNumber) {
		setColumn(COLUMN_QUESTION_NUMBER, displayNumber);
	}
	
	// ejb
}