package com.andwari.ranking;

import java.util.List;
import java.util.stream.Stream;

import javax.inject.Inject;

import com.andwari.core.tournamentcore.ranking.boundary.RankingService;
import com.andwari.core.tournamentcore.ranking.control.PlayerRank;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class RankingPageController {

	private enum Mode {
		YEAR, MONTH;
	}

	private Mode mode;

	@FXML
	private TableView<RankDVO> tvListOfRankings;
	private ObservableList<RankDVO> listOfRanks;

	@FXML
	private TableColumn<RankDVO, String> tcRank, tcPlayer, tcPoints;

	@FXML
	private Button btYear, btMonth;

	@FXML
	private ChoiceBox<String> cbYear, cbMonth;

	@FXML
	private Label lbHeading;

	private static final String HEADING = "Rankings - ";

	@Inject
	private RankingService rankingService;

	@Inject
	private RankingPageService pageService;

	@FXML
	public void initialize() {
		mode = Mode.YEAR;
		intiColumns();
		initDropdown();
		loadList();
		updateLabelYear();
		registerListener();
	}

	public void switchMode() {
		switch (mode) {
		case YEAR:
			switchToMonth();
			break;
		case MONTH:
			switchToYear();
			break;
		}
	}

	private void switchToMonth() {
		mode = Mode.MONTH;
		btMonth.setDisable(true);
		btYear.setDisable(false);
		cbMonth.setDisable(false);
		loadList();
		updateLabelMonth();
	}

	private void switchToYear() {
		mode = Mode.YEAR;
		btMonth.setDisable(false);
		btYear.setDisable(true);
		cbMonth.setDisable(true);
		loadList();
		updateLabelYear();
	}

	private void updateLabelYear() {
		String year = cbYear.getSelectionModel().getSelectedItem();
		lbHeading.setText(HEADING + year);
	}

	private void updateLabelMonth() {
		String year = cbYear.getSelectionModel().getSelectedItem();
		String month = cbMonth.getSelectionModel().getSelectedItem();
		lbHeading.setText(HEADING + month + " " + year);
	}

	private void updateLabelMonth(String month) {
		String year = cbYear.getSelectionModel().getSelectedItem();
		lbHeading.setText(HEADING + month + " " + year);
	}

	private void registerListener() {
		cbYear.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				loadRanksForYear(Integer.parseInt(cbYear.getItems().get((Integer) newValue)));
			}
		});

		cbMonth.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				int year = Integer.parseInt(cbYear.getSelectionModel().getSelectedItem());
				updateLabelMonth(cbMonth.getItems().get((Integer) newValue));
				loadRanksForMonth(year, (Integer) newValue);
			}

		});

	}

	private void loadList() {
		switch (mode) {
		case YEAR:
			loadRanksForYear();
			break;
		case MONTH:
			loadRanksForMonth();
			break;
		}

	}

	private void loadRanksForMonth() {
		String year = cbYear.getSelectionModel().getSelectedItem();
		int month = pageService.parseMonth(cbMonth.getSelectionModel().getSelectedItem());
		if (year != null && month != -1)
			System.out.println("Load " + year + " " + month);
		loadRanksForMonth(Integer.parseInt(year), month);
	}

	private void loadRanksForYear() {
		String year = cbYear.getSelectionModel().getSelectedItem();
		if (year != null)
			loadRanksForYear(Integer.parseInt(year));
	}

	private void loadRanksForYear(int year) {
		List<PlayerRank> rankings = rankingService.getRankings(year);
		listOfRanks = FXCollections.observableArrayList();
		pageService.convertListToDVO(rankings).stream().forEach(r -> listOfRanks.add(r));
		tvListOfRankings.setItems(listOfRanks);
	}

	private void loadRanksForMonth(int year, int month) {
		List<PlayerRank> rankings = rankingService.getRankings(year, month);
		listOfRanks = FXCollections.observableArrayList();
		pageService.convertListToDVO(rankings).stream().forEach(r -> listOfRanks.add(r));
		tvListOfRankings.setItems(listOfRanks);
	}

	private void initDropdown() {
		List<String> years = rankingService.getYears();
		if (years.isEmpty()) {
			return;
		}
		Stream.of(Month.values()).forEach(m -> cbMonth.getItems().add(m.getName()));
		years.stream().forEach(y -> cbYear.getItems().add(y));
		String currentMonth = pageService.getCurrentMonth();
		cbMonth.getSelectionModel().select(currentMonth);
		cbYear.getSelectionModel().select(0);
	}

	private void intiColumns() {
		tcRank.setCellValueFactory(cellData -> cellData.getValue().getRankProperty());
		tcPlayer.setCellValueFactory(cellData -> cellData.getValue().getPlayerProperty());
		tcPoints.setCellValueFactory(cellData -> cellData.getValue().getPointsProperty());

		tcRank.setSortable(false);
		tcPlayer.setSortable(false);
		tcPoints.setSortable(false);
	}

}
