package com.idega.block.survey.data;


import java.util.Collection;
import javax.ejb.CreateException;
import com.idega.data.IDOHome;
import javax.ejb.FinderException;
import com.idega.block.category.data.ICInformationFolder;
import java.sql.Timestamp;

public interface SurveyEntityHome extends IDOHome {
	public SurveyEntity create() throws CreateException;

	public SurveyEntity findByPrimaryKey(Object pk) throws FinderException;

	public Collection findAllSurveys(ICInformationFolder folder)
			throws FinderException;

	public Collection findActiveSurveys(ICInformationFolder folder,
			Timestamp time) throws FinderException;
}