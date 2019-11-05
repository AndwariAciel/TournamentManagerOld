package com.andwari.main;

import org.apache.log4j.Logger;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MainWindowController {

	@FXML
	private Label versionLabel;

	Logger log = Logger.getLogger(getClass().getSimpleName());

	@FXML
	public void initialize() {
		String version = getClass().getPackage().getImplementationVersion();
		versionLabel.setText("version " + version);
	}

}
