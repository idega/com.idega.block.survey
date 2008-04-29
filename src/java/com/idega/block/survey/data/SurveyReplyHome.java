package com.idega.block.survey.data;


import com.idega.data.IDOException;
import java.util.Collection;
import javax.ejb.CreateException;
import com.idega.data.IDOHome;
import javax.ejb.FinderException;

public interface SurveyReplyHome extends IDOHome {
	public SurveyReply create() throws CreateException;

	public SurveyReply findByPrimaryKey(Object pk) throws FinderException;

	public Collection findByQuestion(SurveyQuestion question)
			throws FinderException;

	public Collection findBySurveyAndParticipant(SurveyEntity survey,
			SurveyParticipant participant) throws FinderException;

	public Collection findByQuestions(Collection questions)
			throws FinderException;

	public int getCountByQuestionAndAnswer(SurveyQuestion question,
			SurveyAnswer answer) throws IDOException;

	public int getCountByQuestion(SurveyQuestion question) throws IDOException;
}