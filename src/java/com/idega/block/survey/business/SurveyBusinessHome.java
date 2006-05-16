package com.idega.block.survey.business;


import javax.ejb.CreateException;
import com.idega.business.IBOHome;
import java.rmi.RemoteException;

public interface SurveyBusinessHome extends IBOHome {
	public SurveyBusiness create() throws CreateException, RemoteException;
}