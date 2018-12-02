package com.andwari.event.matches;

import java.io.Serializable;
import java.util.ArrayList;

import javax.inject.Inject;

import com.andwari.core.tournamentcore.event.entity.Match;
import com.andwari.core.tournamentcore.event.entity.Round;
import com.andwari.event.matches.control.MatchListCallback;
import com.andwari.event.matches.control.MatchesPageService;
import com.andwari.event.matches.dvos.MatchListDvo;
import com.andwari.event.rankings.dvos.RankingsDvo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;

public class MatchesPageController implements Serializable {

	private static final long serialVersionUID = 4247502216462360362L; 

	@FXML
	private ListView<MatchListDvo> listViewOfMatches;

	private ObservableList<MatchListDvo> listOfMatches;
	
	@FXML
	private TableView<RankingsDvo> listViewOfRankings;
	private ObservableList<RankingsDvo> listOfRankings;
	
	@FXML
	private TableColumn<RankingsDvo, String> tcStandingRank, tcStandingPlayer, tcStandingScore;
	
	@FXML
	private MatchViewController matchViewController;

	private Round round;

	@Inject
	private MatchListCallback callback;

	@Inject
	private MatchesPageService pageService;

	@FXML
	private Label lbBye;

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
		
		initRankingList();
	}


	public void updateMatch(Match match, MatchListDvo dvo) {
		int index = listOfMatches.indexOf(dvo);
		listOfMatches.remove(index);
		if(match.isFinished()) {
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
		
		tcStandingRank.setSortable(false);
		tcStandingPlayer.setSortable(false);
		tcStandingScore.setSortable(false);
		
		updateRankingList();
	}
	
	private void updateRankingList() {
		ArrayList<RankingsDvo> rankings = pageService.getRankings(round.getEvent());	
		listOfRankings.clear();
		listOfRankings.addAll(rankings);
		
	}
	
	public void finishRound() {
		if(!pageService.validateRound(round)) {
			//TODO error message
		}
		Round nextRound = pageService.createNextRound(round.getEvent());		
		initialize(nextRound);
	}
}
