package com.andwari.event.matches;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Optional;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import com.andwari.core.tournamentcore.event.entity.Match;
import com.andwari.core.tournamentcore.event.entity.Round;
import com.andwari.event.matches.control.MatchListCallback;
import com.andwari.event.matches.control.MatchesPageService;
import com.andwari.event.matches.dvos.MatchListDvo;
import com.andwari.event.rankings.dvos.RankingsDvo;
import com.andwari.event.standingoverview.StandingFinalOverviewController;
import com.andwari.event.standingoverview.StandingOverviewController;
import com.andwari.util.FxmlResource;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MatchesPageController implements Serializable {

	private static final long serialVersionUID = 4247502216462360362L;

	@FXML
	private ListView<MatchListDvo> listViewOfMatches;

	private ObservableList<MatchListDvo> listOfMatches;

	@FXML
	private TableView<RankingsDvo> listViewOfRankings;
	private ObservableList<RankingsDvo> listOfRankings;

	@FXML
	private TableColumn<RankingsDvo, String> tcStandingRank, tcStandingPlayer, tcStandingScore, tcStandingPoints;

	@FXML
	private MatchViewController matchViewController;

	@FXML
	private Label lbRound;
	private static final String lbRoundText = "Matches - Round ";

	@FXML
	private Button btnPrev, btnNext, btnFinish, btnFinishEvent, btnRevokeRound;

	private Round round;

	@Inject
	private MatchListCallback callback;

	@Inject
	private MatchesPageService pageService;

	@FXML
	private Label lbBye;

	@Inject
	private Instance<FXMLLoader> instanceOfLoader;

	public void initialize(Round round) {
		this.round = round;
		listOfMatches = FXCollections.observableArrayList(pageService.getListOfDvos(this.round));
		listViewOfMatches.setCellFactory(callback);

		listViewOfMatches.setItems(listOfMatches);
		MatchListDvo match = listOfMatches.stream().findFirst().get();
		matchViewController.init(this, this.round);
		matchViewController.updateMatch(match);
		if (round.getBye() != null) {
			lbBye.setText(round.getBye().getPlayerName());
		} else {
			lbBye.setText("-");
		}

		listViewOfMatches.setOnMouseClicked((MouseEvent event) -> {
			matchViewController.updateMatch(listViewOfMatches.getSelectionModel().getSelectedItem());
		});

		handleHeader();

		initRankingList();
	}

	private void handleHeader() {
		int roundNumber = round.getEvent().getCurrentRound();
		lbRound.setText(lbRoundText + roundNumber);
		if (roundNumber > 1) {
			btnPrev.setDisable(false);
		} else {
			btnPrev.setDisable(true);
		}
		if (roundNumber < round.getEvent().getRounds().size()) {
			btnNext.setDisable(false);
		} else {
			btnNext.setDisable(true);
		}
		if (round.getFinished()) {
			btnFinish.setDisable(true);
		} else {
			btnFinish.setDisable(false);
		}
		if (round.getEvent().getMaxNumberOfRounds() == roundNumber) {
			btnFinish.setDisable(true);
			btnFinishEvent.setVisible(true);
		} else {
			btnFinishEvent.setVisible(false);
		}
		if (round.getEvent().getCurrentRound() == 1) {
			btnRevokeRound.setDisable(true);
		} else {
			btnRevokeRound.setDisable(false);
		}
	}

	public void updateMatch(Match match, MatchListDvo dvo) {
		int index = listOfMatches.indexOf(dvo);
		listOfMatches.remove(index);
		if (match.isFinished()) {
			listOfMatches.add(dvo);
			listViewOfMatches.getSelectionModel().clearAndSelect(listOfMatches.size() - 1);

		} else {
			listOfMatches.add(0, dvo);
			listViewOfMatches.getSelectionModel().clearAndSelect(0);
		}

		updateRankingList();
	}

	private void initRankingList() {
		listOfRankings = FXCollections.observableArrayList();
		listViewOfRankings.setItems(listOfRankings);

		tcStandingRank.setCellValueFactory(cellData -> cellData.getValue().getRankProperty());
		tcStandingPlayer.setCellValueFactory(cellData -> cellData.getValue().getPlayerProperty());
		tcStandingScore.setCellValueFactory(cellData -> cellData.getValue().getScoreStringProperty());
		tcStandingPoints.setCellValueFactory(cellData -> cellData.getValue().getScoreProperty());

		tcStandingRank.setSortable(false);
		tcStandingPlayer.setSortable(false);
		tcStandingScore.setSortable(false);
		tcStandingPoints.setSortable(false);

		updateRankingList();
	}

	private void updateRankingList() {
		ArrayList<RankingsDvo> rankings = pageService.getRankings(round.getEvent());
		listOfRankings.clear();
		listOfRankings.addAll(rankings);

	}

	public void finishRound() {
		if (!pageService.validateRound(round)) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("unfinished Matches");
			alert.setHeaderText("Warning");
			alert.setContentText("Cannot go to next round, not all matches are finished!");
			alert.showAndWait();
			return;
		}
		showAndWaitDetails();
		round.setFinished(true);
		Round nextRound = pageService.createNextRound(round.getEvent());
		initialize(nextRound);
	}

	public void revokeRound() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Revoke Round");
		alert.setHeaderText("Please confirm");
		alert.setContentText(
				"When you revoke this round, all results of this round will be deleted and the previous round will be loaded and opened again.\nAre you sure you want to do this?");
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			Round prevRound = pageService.revokeRound(round);
			initialize(prevRound);
		}
	}

	public void goToPreviousRound() {
		int currentRound = round.getEvent().getCurrentRound();
		if (currentRound > 1) {
			currentRound--;
			round.getEvent().setCurrentRound(currentRound);
			Round prevRound = ((ArrayList<Round>) round.getEvent().getRounds()).get(currentRound - 1);
			initialize(prevRound);
		}
	}

	public void goToNextRound() {

		int currentRound = round.getEvent().getCurrentRound();
		if (currentRound - 1 < round.getEvent().getRounds().size()) {
			currentRound++;
			round.getEvent().setCurrentRound(currentRound);
			Round prevRound = ((ArrayList<Round>) round.getEvent().getRounds()).get(currentRound - 1);
			initialize(prevRound);
		}
	}

	public void showDetails() {
		try {
			FXMLLoader loader = instanceOfLoader.get();
			loader.setLocation(FxmlResource.EVENT_STANDINGS_OVERVIEW.getResourceUrl());
			BorderPane root = loader.load();
			Scene scene = new Scene(root);
			Stage newWindow = new Stage();
			newWindow.setScene(scene);
			StandingOverviewController controller = loader.getController();
			newWindow.show();
			controller.initialize(round);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void showAndWaitDetails() {
		try {
			FXMLLoader loader = instanceOfLoader.get();
			loader.setLocation(FxmlResource.EVENT_STANDINGS_OVERVIEW.getResourceUrl());
			BorderPane root = loader.load();
			Scene scene = new Scene(root);
			Stage newWindow = new Stage();
			newWindow.setScene(scene);
			StandingOverviewController controller = loader.getController();
			controller.initialize(round);
			newWindow.showAndWait();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void finishEvent() {
		if (!pageService.validateRound(round)) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("unfinished Matches");
			alert.setHeaderText("Warning");
			alert.setContentText("Cannot finish the event, not all matches are finished!");
			alert.showAndWait();
			return;
		}
		round.setFinished(true);
		initialize(round);
		try {
			FXMLLoader loader = instanceOfLoader.get();
			loader.setLocation(FxmlResource.EVENT_STANDINGS_FINAL.getResourceUrl());
			BorderPane root = loader.load();
			Scene scene = new Scene(root);
			Stage newWindow = new Stage();
			newWindow.setScene(scene);
			StandingFinalOverviewController controller = loader.getController();
			newWindow.show();
			controller.initialize(round);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
