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
			String mode = iwc.getParameter(Survey.PRM_SWITCHTO_MODE);
			if (mode.equals(Survey.MODE_EDIT)) {
				SurveyEditor se = new SurveyEditor(Integer.parseInt(sInst));
				se.maintainParameter(PRM_INSTANCE_ID, sInst);
				add(se);
			}
			else if (mode.equals(Survey.MODE_RESULTS)) {
				SurveyResultEditor sre = new SurveyResultEditor();
				sre.setICObjectInstanceID(Integer.parseInt(sInst));
				add(sre);
			}
		}
		else {
			add("No instance ID");
		}
	}
}
