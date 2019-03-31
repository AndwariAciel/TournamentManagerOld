package com.andwari.event.settings;

import java.util.Calendar;
import java.util.Date;

import com.andwari.core.tournamentcore.event.entity.Event;
import com.andwari.event.playerselection.PlayerSelectionController;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EventSettingsPageController {

	@FXML
	private TextField tfEventName;

	@FXML
	private CheckBox cbRankpoints;

	@FXML
	private Label lbDate, lbRounds;

	@FXML
	private Button btnOk;

	private Event event;
	private PlayerSelectionController playerSelectionController;

	public void startEvent() {
		event.setEventName(tfEventName.getText());
		event.setRankingPoints(cbRankpoints.isSelected());
		playerSelectionController.continueEvent(event);
		Stage stage = (Stage) btnOk.getScene().getWindow();
		stage.close();
	}

	public void init(Event event, PlayerSelectionController controller) {
		this.event = event;
		this.playerSelectionController = controller;
		lbDate.setText(parseDate(event.getCreated()));
		lbRounds.setText(Integer.toString(event.getMaxNumberOfRounds()));
	}

	private String parseDate(Date created) {
		Calendar date = Calendar.getInstance();
		date.setTime(created);
		StringBuilder sb = new StringBuilder();
		sb.append(date.get(Calendar.DAY_OF_MONTH));
		sb.append(".");
		sb.append(String.format("%1$2s", (date.get(Calendar.MONTH) + 1)).replace(' ', '0'));
		sb.append(".");
		sb.append(date.get(Calendar.YEAR));
		return sb.toString();
	}
}
