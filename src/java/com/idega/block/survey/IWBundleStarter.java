package com.idega.block.survey;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.block.survey.business.SurveyBusinessBean;
import com.idega.block.survey.data.SurveyType;
import com.idega.block.survey.data.SurveyTypeHome;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWBundleStartable;
import com.idega.idegaweb.include.GlobalIncludeManager;

public class IWBundleStarter implements IWBundleStartable {

	public void start(IWBundle starterBundle) {
		GlobalIncludeManager includeManager = GlobalIncludeManager.getInstance();
		includeManager.addBundleStyleSheet("com.idega.block.survey", "/style/survey.css");
		createSurveyTypes();
	}
	
	public void createSurveyTypes() {
		try {
			SurveyTypeHome stHome = (SurveyTypeHome) IDOLookup.getHome(SurveyType.class);
			try {
				stHome.findByName(SurveyBusinessBean.SURVEY_TYPE_QUESTIONNAIRE);
			} catch (FinderException e) {
				try {
					SurveyType st = stHome.create();
					st.setName(SurveyBusinessBean.SURVEY_TYPE_QUESTIONNAIRE);
					st.setLocalizationKey("survey_type.questionnaire");
					st.setDescription("A regular survey");
					st.setAnswerTypes(new char[]{SurveyBusinessBean.ANSWERTYPE_MULTI_CHOICE, SurveyBusinessBean.ANSWERTYPE_SINGLE_CHOICE, SurveyBusinessBean.ANSWERTYPE_TEXTAREA});
					st.setAllowEmail(true);
					st.setAllowCheckBoxValue(false);
					st.setAllowRadioButtonValue(false);
					st.setShowGrade(false);
					st.setCustomizableGrade(false);
					st.setShowResults(false);
					st.store();
					System.out.println("[SurveyBundleStarter] SurveyType "+SurveyBusinessBean.SURVEY_TYPE_QUESTIONNAIRE+" Created");
				} catch (CreateException e1) {
					System.out.println("[SurveyBundleStarter] Could not create SurveyType "+SurveyBusinessBean.SURVEY_TYPE_QUESTIONNAIRE);
				}
			}

			try {
				stHome.findByName(SurveyBusinessBean.SURVEY_TYPE_EXAM);
			} catch (FinderException e) {
				try {
					SurveyType st = stHome.create();
					st.setName(SurveyBusinessBean.SURVEY_TYPE_EXAM);
					st.setLocalizationKey("survey_type.exam");
					st.setDescription("Multiple answers exam, shows you the results right away.");
					st.setAnswerTypes(new char[]{SurveyBusinessBean.ANSWERTYPE_MULTI_CHOICE, SurveyBusinessBean.ANSWERTYPE_SINGLE_CHOICE});
					st.setAllowEmail(true);
					st.setAllowCheckBoxValue(false);
					st.setAllowRadioButtonValue(false);
					st.setShowGrade(false);
					st.setCustomizableGrade(false);
					st.setShowResults(true);
					st.store();
					System.out.println("[SurveyBundleStarter] SurveyType "+SurveyBusinessBean.SURVEY_TYPE_EXAM+" Created");
				} catch (CreateException e1) {
					System.out.println("[SurveyBundleStarter] Could not create SurveyType "+SurveyBusinessBean.SURVEY_TYPE_EXAM);
				}
			}
		
			try {
				stHome.findByName(SurveyBusinessBean.SURVEY_TYPE_TEST);
			} catch (FinderException e) {
				try {
					SurveyType st = stHome.create();
					st.setName(SurveyBusinessBean.SURVEY_TYPE_TEST);
					st.setLocalizationKey("survey_type.exam");
					st.setDescription("Multiple answers exam, shows you the results right away.");
					st.setAnswerTypes(new char[]{SurveyBusinessBean.ANSWERTYPE_MULTI_CHOICE, SurveyBusinessBean.ANSWERTYPE_SINGLE_CHOICE});
					st.setAllowEmail(true);
					st.setAllowCheckBoxValue(true);
					st.setAllowRadioButtonValue(true);
					st.setShowGrade(true);
					st.setCustomizableGrade(true);
					st.setShowResults(true);
					st.store();
					System.out.println("[SurveyBundleStarter] SurveyType "+SurveyBusinessBean.SURVEY_TYPE_TEST+" Created");
				} catch (CreateException e1) {
					System.out.println("[SurveyBundleStarter] Could not create SurveyType "+SurveyBusinessBean.SURVEY_TYPE_TEST);
				}
			}
		
		} catch (IDOLookupException e) {
			e.printStackTrace();
		}
	}

	public void stop(IWBundle starterBundle) {
	}

}
