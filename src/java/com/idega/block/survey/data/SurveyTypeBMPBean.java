package com.idega.block.survey.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.query.Column;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.Table;

public class SurveyTypeBMPBean extends GenericEntity implements SurveyType {

	private static final String COLUMN_NAME = "NAME";
	private static final String COLUMN_LOCALIZATION_KEY = "LOCALICATION_KEY";
	private static final String COLUMN_DESCRIPTION = "DESCRIPTION";
	private static final String COLUMN_ANSWER_TYPES = "ANSWER_TYPES";
	private static final String COLUMN_ALLOW_EMAIL = "ALLOW_EMAIL";
	private static final String COLUMN_ALLOW_CHECKBOX_VALUE = "ALLOW_CB_VALUE";
	private static final String COLUMN_ALLOW_RADIOBUTTON_VALUE = "ALLOW_RB_VALUE";
	private static final String COLUMN_SHOW_GRADE = "SHOW_GRADE";
	private static final String COLUMN_CUSTOMIZABLE_GRADE = "CUSTOMIZABLE_GRADE";
	private static final String COLUMN_SHOW_RESULTS = "SHOW_RESULTS";
	
	public String getEntityName() {
		return "SU_SURVEY_TYPE";
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(COLUMN_NAME, "name", String.class);
		addAttribute(COLUMN_LOCALIZATION_KEY, "localizationkey", String.class);
		addAttribute(COLUMN_DESCRIPTION, "description", String.class);
		addAttribute(COLUMN_ANSWER_TYPES, "answer types", String.class);
		addAttribute(COLUMN_ALLOW_EMAIL, "email", Boolean.class);
		addAttribute(COLUMN_ALLOW_CHECKBOX_VALUE, "checkbox value", Boolean.class);
		addAttribute(COLUMN_ALLOW_RADIOBUTTON_VALUE, "radiobutton value", Boolean.class);
		addAttribute(COLUMN_SHOW_GRADE, "show grade", Boolean.class);
		addAttribute(COLUMN_CUSTOMIZABLE_GRADE, "customizable grade", Boolean.class);
		addAttribute(COLUMN_SHOW_RESULTS, "show results", Boolean.class);
	}

	public String getName() {
		return getStringColumnValue(COLUMN_NAME);
	}
	
	public void setName(String name) {
		setColumn(COLUMN_NAME, name);
	}
	
	public String getLocalizationKey() {
		return getStringColumnValue(COLUMN_LOCALIZATION_KEY);
	}
	
	public void setLocalizationKey(String key) {
		setColumn(COLUMN_LOCALIZATION_KEY, key);
	}
	
	public String getDescription() {
		return getStringColumnValue(COLUMN_DESCRIPTION);
	}
	
	public void setDescription(String description) {
		setColumn(COLUMN_DESCRIPTION, description);
	}
	
	public char[] getAnswerTypes() {
		String at = getStringColumnValue(COLUMN_ANSWER_TYPES);
		if (at != null) {
			return at.toCharArray();
		} 
		return null;
	}
	
	public void setAnswerTypes(char[] types) {
		if (types != null) {
			setColumn(COLUMN_ANSWER_TYPES, new String(types));
		}
	}
	
	public boolean getAllowEmail() {
		return getBooleanColumnValue(COLUMN_ALLOW_EMAIL);
	}
	
	public void setAllowEmail(boolean allow) {
		setColumn(COLUMN_ALLOW_EMAIL, allow);
	}

	public boolean getAllowCheckBoxValue() {
		return getBooleanColumnValue(COLUMN_ALLOW_CHECKBOX_VALUE);
	}
	
	public void setAllowCheckBoxValue(boolean allow) {
		setColumn(COLUMN_ALLOW_CHECKBOX_VALUE, allow);
	}

	public boolean getAllowRadioButtonValue() {
		return getBooleanColumnValue(COLUMN_ALLOW_RADIOBUTTON_VALUE);
	}
	
	public void setAllowRadioButtonValue(boolean allow) {
		setColumn(COLUMN_ALLOW_RADIOBUTTON_VALUE, allow);
	}
	

	public boolean getShowGrade() {
		return getBooleanColumnValue(COLUMN_SHOW_GRADE);
	}
	
	public void setShowGrade(boolean show) {
		setColumn(COLUMN_SHOW_GRADE, show);
	}

	public boolean getCustomizableGrade() {
		return getBooleanColumnValue(COLUMN_CUSTOMIZABLE_GRADE);
	}
	
	public void setCustomizableGrade(boolean settable) {
		setColumn(COLUMN_CUSTOMIZABLE_GRADE, settable);
	}
	
	public boolean getShowResults() {
		return getBooleanColumnValue(COLUMN_SHOW_RESULTS);
	}
	
	public void setShowResults(boolean show) {
		setColumn(COLUMN_SHOW_RESULTS, show);
	}
	
	public Collection ejbFindAll() throws FinderException {
		Table table = new Table(this);
		SelectQuery query =  new SelectQuery(table);
		query.addColumn(new Column(table, getIDColumnName()));
		
		return this.idoFindPKsByQuery(query);
	}
	
	public Object ejbFindByName(String name) throws FinderException {
		Table table = new Table(this);
		Column nameCol = new Column(table, COLUMN_NAME);
		SelectQuery query =  new SelectQuery(table);
		query.addColumn(new Column(table, getIDColumnName()));
		query.addCriteria(new MatchCriteria(nameCol, MatchCriteria.EQUALS, name));
		
		return idoFindOnePKByQuery(query);
	}
	
}
