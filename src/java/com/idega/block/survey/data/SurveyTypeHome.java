package com.idega.block.survey.data;


import java.util.Collection;
import javax.ejb.CreateException;
import com.idega.data.IDOHome;
import javax.ejb.FinderException;

public interface SurveyTypeHome extends IDOHome {
	public SurveyType create() throws CreateException;

	public SurveyType findByPrimaryKey(Object pk) throws FinderException;

	public Collection findAll() throws FinderException;

	public SurveyType findByName(String name) throws FinderException;
}