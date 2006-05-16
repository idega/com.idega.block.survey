package com.idega.block.survey.business;


import javax.ejb.CreateException;
import com.idega.business.IBOHomeImpl;

public class SurveyBusinessHomeImpl extends IBOHomeImpl {
	public Class getBeanInterfaceClass() {
		return SurveyBusiness.class;
	}

	public SurveyBusiness create() throws CreateException {
		return (SurveyBusiness) super.createIBO();
	}
}