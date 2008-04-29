package com.idega.block.survey.data;


import java.util.Collection;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import com.idega.block.category.data.ICInformationFolder;
import java.sql.Timestamp;
import com.idega.data.IDOEntity;
import com.idega.data.IDOFactory;

public class SurveyEntityHomeImpl extends IDOFactory implements
		SurveyEntityHome {
	public Class getEntityInterfaceClass() {
		return SurveyEntity.class;
	}

	public SurveyEntity create() throws CreateException {
		return (SurveyEntity) super.createIDO();
	}

	public SurveyEntity findByPrimaryKey(Object pk) throws FinderException {
		return (SurveyEntity) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findAllSurveys(ICInformationFolder folder)
			throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((SurveyEntityBMPBean) entity)
				.ejbFindAllSurveys(folder);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findActiveSurveys(ICInformationFolder folder,
			Timestamp time) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((SurveyEntityBMPBean) entity).ejbFindActiveSurveys(
				folder, time);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
}