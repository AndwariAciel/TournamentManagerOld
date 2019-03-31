package com.andwari.ranking;

import java.net.URL;
import java.util.List;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.apache.log4j.Logger;

import com.andwari.FxmlPageManager;
import com.andwari.core.tournamentcore.ranking.boundary.RankingService;
import com.andwari.core.tournamentcore.ranking.control.PlayerRank;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

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
	private ChoiceBox<String> cbYear;
	@FXML
	private ChoiceBox<Month> cbMonth;

	@FXML
	private Label lbHeading;

	private static final String HEADING = "Rankings - ";

	@Inject
	private RankingService rankingService;

	@Inject
	private RankingPageService pageService;

	@Inject
	private Instance<FXMLLoader> fxmlLoaderInst;
	@Inject
	private FxmlPageManager finder;

	private Logger logger = Logger.getLogger(this.getClass());

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
		String month = cbMonth.getSelectionModel().getSelectedItem().toString();
		lbHeading.setText(HEADING + month + " " + year);
	}

	private void updateLabelMonth(Month month) {
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
				Month month = cbMonth.getItems().get((Integer) newValue);
				updateLabelMonth(cbMonth.getItems().get((Integer) newValue));
				loadRanksForMonth(year, month.getNumber());
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
		int month = cbMonth.getSelectionModel().getSelectedItem().getNumber();
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
		cbMonth.getItems().addAll(Month.values());
		years.stream().forEach(y -> cbYear.getItems().add(y));
		Month currentMonth = pageService.getCurrentMonth();
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

	public void givePoints() {
		FXMLLoader fxmlLoader = fxmlLoaderInst.get();
		try {
			URL fxmlRes = finder.findFxmlResource("event/rankings/RankingManually.fxml");
			fxmlLoader.setLocation(fxmlRes);
			BorderPane root = fxmlLoader.load();
			Scene scene = new Scene(root);
			Stage newWindow = new Stage();
			newWindow.setScene(scene);
			newWindow.showAndWait();
			loadList();
		} catch (Exception e) {
			logger.error(e);
		}
	}

}
