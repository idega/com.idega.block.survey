package com.idega.block.survey.data;


import java.util.Collection;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import com.idega.data.IDOEntity;
import com.idega.data.IDOFactory;

public class SurveyTypeHomeImpl extends IDOFactory implements SurveyTypeHome {
	public Class getEntityInterfaceClass() {
		return SurveyType.class;
	}

	public SurveyType create() throws CreateException {
		return (SurveyType) super.createIDO();
	}

	public SurveyType findByPrimaryKey(Object pk) throws FinderException {
		return (SurveyType) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findAll() throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((SurveyTypeBMPBean) entity).ejbFindAll();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public SurveyType findByName(String name) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((SurveyTypeBMPBean) entity).ejbFindByName(name);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}
}