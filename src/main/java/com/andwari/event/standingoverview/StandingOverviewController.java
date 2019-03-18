package com.andwari.event.standingoverview;

import java.util.ArrayList;

import javax.inject.Inject;

import com.andwari.core.tournamentcore.event.entity.Round;
import com.andwari.core.tournamentcore.exception.HasOngoingMatchException;
import com.andwari.event.matches.control.MatchesPageService;
import com.andwari.event.rankings.converter.RankingsDvoConverter;
import com.andwari.event.rankings.dvos.RankingsDvo;
import com.andwari.event.standingoverview.control.StandingListCellCallback;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class StandingOverviewController {
	
	@FXML
	private TableColumn<RankingsDvo, String> colRank, colPlayer, colScore, colOpscore, colGamescroe, colOpGamescore;
	
	@FXML
	private TableView<RankingsDvo> tableStandings;
	
	@FXML
	private Button btnOk, btnDrop;
	
	private ObservableList<RankingsDvo> listOfStandings;
	
	private Round round;
	
	@Inject
	private StandingListCellCallback callback;
	
	@Inject
	private MatchesPageService pageService;
	
	@Inject 
	private RankingsDvoConverter converter;
	
	public void initialize(Round round) {
		this.round = round;
		listOfStandings = FXCollections.observableArrayList();
		tableStandings.setRowFactory(callback);
		tableStandings.setItems(listOfStandings);
		initColumns();
		initList();
	}

	private void initColumns() {
		colRank.setCellValueFactory(cellData -> cellData.getValue().getRankProperty());
		colPlayer.setCellValueFactory(cellData -> cellData.getValue().getPlayerProperty());
		colScore.setCellValueFactory(cellData -> cellData.getValue().getScoreProperty());
		colOpscore.setCellValueFactory(cellData -> cellData.getValue().getOpMatchScoreProperty());
		colGamescroe.setCellValueFactory(cellData -> cellData.getValue().getGameScoreProperty());
		colOpGamescore.setCellValueFactory(cellData -> cellData.getValue().getOpGamesScoreProperty());
		
		colRank.setSortable(false);
		colPlayer.setSortable(false);
		colScore.setSortable(false);
		colOpscore.setSortable(false);
		colOpGamescore.setSortable(false);
		colGamescroe.setSortable(false);
		colOpGamescore.setSortable(false);
	}

	private void initList() {
		listOfStandings.removeAll(listOfStandings);
		ArrayList<RankingsDvo> rankings = pageService.getRankings(round.getEvent());	
		rankings.stream().forEach(dvo -> listOfStandings.add(dvo));
	}

	public void dropPlayer() {
		int index = tableStandings.getSelectionModel().getSelectedIndex();
		if(index < 0) 
			return;
		RankingsDvo dvo = listOfStandings.get(index);
		try {
			pageService.dropPlayer(converter.convertToEntity(dvo, round));
			dvo.setDropped(true);
			listOfStandings.set(index, dvo);
		} catch(HasOngoingMatchException ex) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("unfinished Match");
			alert.setHeaderText("Warning");
			alert.setContentText("Cannot drop this player, finish his open match first!");
			alert.showAndWait();
		}
	}
	
	public void closeWindow() {
		Stage stage = (Stage) btnOk.getScene().getWindow();
	    stage.close();
	}
}
