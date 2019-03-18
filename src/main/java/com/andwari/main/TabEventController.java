package com.andwari.main;

import javax.inject.Inject;

import com.andwari.FxmlPageManager;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class TabEventController {
	
	@Inject
	private FxmlPageManager pageManager;

	@FXML
	private Button startEventBtn;

	public void createNewEvent() {
		pageManager.openNewWindow("event/EventPlayerSelection.fxml");
	}

}
