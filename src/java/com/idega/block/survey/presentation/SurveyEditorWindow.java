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
		String mode = iwc.getParameter(Survey.PRM_SWITCHTO_MODE);
		if (mode != null && mode.equals(Survey.MODE_EDIT)) {
			String sInst = iwc.getParameter(PRM_INSTANCE_ID);
			if (sInst != null) {
				SurveyEditor se = new SurveyEditor(Integer.parseInt(sInst));
				se.maintainParameter(PRM_INSTANCE_ID, sInst);
				se.maintainParameter(Survey.PRM_SWITCHTO_MODE, Survey.MODE_EDIT);
				add(se);
			}
			else {
				add("No instance ID");
			}
		}
		else {
			SurveyResultEditor sre = new SurveyResultEditor();
			add(sre);
		}
	}
}
