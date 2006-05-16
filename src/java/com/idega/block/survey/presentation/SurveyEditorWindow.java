package com.idega.block.survey.presentation;

import com.idega.presentation.IWContext;
import com.idega.presentation.ui.Window;

public class SurveyEditorWindow extends Window {

	static final String PRM_INSTANCE_ID = "sew_p_id";
	
	public SurveyEditorWindow() {
		super(600, 700);
		setScrollbar(true);
		setAllMargins(0);
	}
	
	public void main(IWContext iwc) {
		String sInst = iwc.getParameter(PRM_INSTANCE_ID);
		if (sInst != null) {
			SurveyEditor se = new SurveyEditor(Integer.parseInt(sInst));
			se.maintainParameter(PRM_INSTANCE_ID, sInst);
			add(se);
		} else {
			add("No instance ID");
		}
	}
}
