package com.andwari.event.rankings;

import java.util.Calendar;
import java.util.List;
import java.util.function.UnaryOperator;

import javax.inject.Inject;

import com.andwari.core.tournamentcore.player.boundary.PlayerService;
import com.andwari.core.tournamentcore.player.entity.Player;
import com.andwari.core.tournamentcore.ranking.boundary.RankingService;
import com.andwari.core.tournamentcore.ranking.entity.Rank;
import com.andwari.ranking.Month;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.stage.Stage;

public class ManuelPointsPageController {

	@FXML
	private ListView<String> listView;

	@FXML
	private TextField tfPoints, tfSearch, tfYear;

	@FXML
	private ChoiceBox<Month> cbMonth;

	@FXML
	private TextArea taComment;

	@Inject
	private RankingService rankingService;

	@Inject
	private PlayerService playerService;

	@FXML
	public void initialize() {
		List<Player> players = rankingService.getAllPlayersWithMembership();
		ObservableList<String> observableList = FXCollections.observableArrayList();
		players.stream().forEach(p -> observableList.add(p.getPlayerName()));
		FilteredList<String> filteredList = new FilteredList<>(observableList);

		tfSearch.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredList.setPredicate(player -> {
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}
				String lowerCaseFilter = newValue.toLowerCase();

				if (player.toLowerCase().contains(lowerCaseFilter)) {
					return true;
				}
				return false;
			});
		});
		listView.setItems(filteredList);
		tfPoints.setTextFormatter(new TextFormatter<>(filter));
		Calendar cal = Calendar.getInstance();
		tfYear.setText(Integer.toString(cal.get(Calendar.YEAR)));
		tfYear.setTextFormatter(new TextFormatter<>(filter));
		cbMonth.getItems().setAll(Month.values());
		cbMonth.getSelectionModel().select(0);
	}

	public void givePointsAndClose() {
		String playerName = listView.getSelectionModel().getSelectedItem();
		if (playerName == null) {
			showWarningNoPlayer();
			return;
		}
		if (taComment.getText().isEmpty() || taComment.getText().length() < 3) {
			showWarningNoComment();
			return;
		}
		if (tfPoints.getText().isEmpty()) {
			showWarningNoPoints();
			return;
		}
		Player player = playerService.findPlayerByName(playerName);
		int points = Integer.parseInt(tfPoints.getText());
		saveRank(player, points, taComment.getText(), tfYear.getText(), cbMonth.getSelectionModel().getSelectedItem());
		Stage stage = (Stage) taComment.getScene().getWindow();
		stage.close();
	}

	private void saveRank(Player player, int points, String text, String text2, Month selectedItem) {
		Rank rank = new Rank();
		rank.setPlayer(player);
		rank.setPoints(points);
		rank.setComment(text);
		rank.setYear(Integer.parseInt(text2));
		rank.setMonth(selectedItem.getNumber());
		rankingService.saveRank(rank);
	}

	private void showWarningNoPlayer() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("No player selected");
		alert.setHeaderText("Warning");
		alert.setContentText("You must select a player!");
		alert.showAndWait();
	}

	private void showWarningNoComment() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("No comment");
		alert.setHeaderText("Warning");
		alert.setContentText("You must write a comment, why this player gets the points!");
		alert.showAndWait();
	}

	private void showWarningNoPoints() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("No points");
		alert.setHeaderText("Warning");
		alert.setContentText("You must enter at least some points!");
		alert.showAndWait();
	}

	UnaryOperator<Change> filter = change -> {
		String text = change.getText();

		if (text.matches("[0-9]*")) {
			return change;
		}

		return null;
	};
}
