package com.andwari.event.standingoverview;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import com.andwari.core.tournamentcore.event.entity.Round;
import com.andwari.core.tournamentcore.ranking.boundary.RankingService;
import com.andwari.core.tournamentcore.ranking.entity.Rank;
import com.andwari.core.tournamentcore.utils.BoosterDistributor;
import com.andwari.event.matches.control.MatchesPageService;
import com.andwari.event.rankings.dvos.RankingsDvo;
import com.andwari.event.standingoverview.control.StandingListCellCallback;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class StandingFinalOverviewController {

	@FXML
	private TableColumn<RankingsDvo, String> colRank, colPlayer, colScore, colOpscore, colGamescroe, colOpGamescore,
			colRankingPts, colBooster;

	@FXML
	private TableView<RankingsDvo> tableStandings;

	@FXML
	private Button btnOk;

	@Inject
	private StandingListCellCallback callback;

	@Inject
	private MatchesPageService pageService;

	@Inject
	private BoosterDistributor boosterDistributor;

	@Inject
	private RankingService rankService;

	private Round round;
	private ObservableList<RankingsDvo> listOfStandings;

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
		colRankingPts.setCellValueFactory(cellData -> cellData.getValue().getRankingPointsProperty());
		colBooster.setCellValueFactory(cellData -> cellData.getValue().getBoosterProperty());

		colRank.setSortable(false);
		colPlayer.setSortable(false);
		colScore.setSortable(false);
		colOpscore.setSortable(false);
		colOpGamescore.setSortable(false);
		colGamescroe.setSortable(false);
		colOpGamescore.setSortable(false);
		colRankingPts.setSortable(false);
		colBooster.setSortable(false);
	}

	private void initList() {
		listOfStandings.removeAll(listOfStandings);
		ArrayList<RankingsDvo> rankings = pageService.getRankings(round.getEvent());
		rankings.stream().forEach(dvo -> listOfStandings.add(dvo));
		calculateBooster();
		calculateRankPoints();
	}

	private void calculateRankPoints() {
		if (round.getEvent().getRankingPoints()) {
			List<Rank> rankPointsDistribution = rankService.getRankPointsDistribution(round.getEvent());
			rankService.saveRankings(rankPointsDistribution);
			for (int x = 0; x < listOfStandings.size(); x++) {
				String playername = listOfStandings.get(x).getPlayer();
				Optional<Rank> rank = rankPointsDistribution.stream()
						.filter(r -> r.getPlayer().getPlayerName().equals(playername)).findFirst();
				if (rank.isPresent()) {
					listOfStandings.get(x).setRankingPoints(Integer.toString(rank.get().getPoints()));
				}
			}
		} else {
			listOfStandings.stream().forEach(r -> r.setRankingPoints("-"));
		}
	}

	private void calculateBooster() {
		int[] distribution = boosterDistributor.getBoosterDistribution(round.getEvent().getRankings().size());
		for (int x = 0; x < distribution.length; x++) {
			listOfStandings.get(x).setBooster(Integer.toString(distribution[x]));
		}
		for (int x = distribution.length; x < listOfStandings.size(); x++) {
			listOfStandings.get(x).setBooster("0");
		}
	}

	public void closeWindow() {
		Stage stage = (Stage) btnOk.getScene().getWindow();
		stage.close();
	}

}
