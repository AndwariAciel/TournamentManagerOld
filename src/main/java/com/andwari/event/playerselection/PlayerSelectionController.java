package com.andwari.event.playerselection;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.apache.log4j.Logger;

import com.andwari.core.tournamentcore.event.boundary.EventService;
import com.andwari.core.tournamentcore.event.entity.Event;
import com.andwari.core.tournamentcore.player.entity.Player;
import com.andwari.event.pairings.PairingsPageController;
import com.andwari.event.seatings.SeatingsPageController;
import com.andwari.event.settings.EventSettingsPageController;
import com.andwari.playermanagement.PlayerConverter;
import com.andwari.playermanagement.PlayerDVO;
import com.andwari.playermanagement.TabPlayerService;
import com.andwari.util.FxmlResource;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class PlayerSelectionController {

	@FXML
	private TableView<PlayerDVO> tvPlayers;
	private ObservableList<PlayerDVO> listOfPlayers;

	@FXML
	private TableView<PlayerDVO> tvPlayersInEvent;
	private ObservableList<PlayerDVO> listOfPlayersInEvent;

	@FXML
	private Button btnStartEvent;

	@Inject
	private TabPlayerService tabPlayerService;

	@Inject
	private PlayerConverter converter;

	@FXML
	private TextField tfSearchPlayer;

	@FXML
	private TextField tfSearchPlayerEvent;

	@FXML
	private Label lblPlayers;

	private final String lblPlayersText = " Players in Event";

	private Stage stage;

	@Inject
	private Instance<FXMLLoader> fxmlLoaderInst;

	@Inject
	private EventService eventService;

	private Logger logger = Logger.getLogger(this.getClass());

	@FXML
	public void initialize() {

		setupColumnsPlayer();
		setupColumnsPlayerEvent();

		tvPlayers.setOnMouseClicked((MouseEvent event) -> {
			if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
				movePlayer(listOfPlayers, listOfPlayersInEvent, tvPlayers.getSelectionModel().getSelectedItem());
			}
		});

		tvPlayersInEvent.setOnMouseClicked((MouseEvent event) -> {
			if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
				movePlayer(listOfPlayersInEvent, listOfPlayers, tvPlayersInEvent.getSelectionModel().getSelectedItem());
			}
		});
		updateLabel();
	}

	private void movePlayer(ObservableList<PlayerDVO> fromList, ObservableList<PlayerDVO> toList, PlayerDVO player) {
		toList.add(player);
		fromList.remove(player);
		updateLabel();
	}

	private void updateLabel() {
		String lableText = listOfPlayersInEvent.size() + lblPlayersText;
		lblPlayers.setText(lableText);
	}

	@SuppressWarnings("unchecked")
	private void setupColumnsPlayer() {
		listOfPlayers = FXCollections.observableArrayList(tabPlayerService.getAllPlayersFromDatabase());

		TableColumn<PlayerDVO, String> name = new TableColumn<>("Player");
		name.setMinWidth(250);
		name.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
		TableColumn<PlayerDVO, String> dci = new TableColumn<>("DCI");
		dci.setMinWidth(100);
		dci.setCellValueFactory(cellData -> cellData.getValue().dciProperty());
		tvPlayers.getColumns().setAll(name, dci);

		FilteredList<PlayerDVO> filteredListPlayer = new FilteredList<>(listOfPlayers, p -> true);
		SortedList<PlayerDVO> sortedList = new SortedList<>(filteredListPlayer);
		sortedList.comparatorProperty().bind(tvPlayers.comparatorProperty());
		tvPlayers.setItems(sortedList);
		tvPlayers.getSortOrder().setAll(name);

		tfSearchPlayer.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredListPlayer.setPredicate(player -> {
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}
				String lowerCaseFilter = newValue.toLowerCase();

				if (player.getName().toLowerCase().contains(lowerCaseFilter)) {
					return true;
				} else if (player.getDci() != null && player.getDci().toLowerCase().contains(lowerCaseFilter)) {
					return true;
				}
				return false;
			});
		});
	}

	@SuppressWarnings("unchecked")
	private void setupColumnsPlayerEvent() {
		listOfPlayersInEvent = FXCollections.observableArrayList();

		TableColumn<PlayerDVO, String> name = new TableColumn<>("Player");
		name.setMinWidth(250);
		name.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
		TableColumn<PlayerDVO, String> dci = new TableColumn<>("DCI");
		dci.setMinWidth(100);
		dci.setCellValueFactory(cellData -> cellData.getValue().dciProperty());
		tvPlayersInEvent.getColumns().setAll(name, dci);

		FilteredList<PlayerDVO> filteredListPlayer = new FilteredList<>(listOfPlayersInEvent, p -> true);
		SortedList<PlayerDVO> sortedList = new SortedList<>(filteredListPlayer);
		sortedList.comparatorProperty().bind(tvPlayersInEvent.comparatorProperty());
		tvPlayersInEvent.setItems(sortedList);
		tvPlayersInEvent.getSortOrder().setAll(name);

		tfSearchPlayerEvent.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredListPlayer.setPredicate(player -> {
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}
				String lowerCaseFilter = newValue.toLowerCase();

				if (player.getName().toLowerCase().contains(lowerCaseFilter)) {
					return true;
				} else if (player.getDci() != null && player.getDci().toLowerCase().contains(lowerCaseFilter)) {
					return true;
				}
				return false;
			});
		});
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public void startEvent() {
		List<Player> players = converter.convertBackToPlayer(listOfPlayersInEvent);
		Event event = eventService.createNewEvent(players);
		event.setMaxNumberOfRounds(eventService.getMaxNumberOfRounds(listOfPlayersInEvent.size()));
		EventSettingsPageController settingsController = openNewWindow("com/andwari/event/EventSettings.fxml");
		settingsController.init(event, this, false);
	}

	public void startEventManuelly() {
		List<Player> players = converter.convertBackToPlayer(listOfPlayersInEvent);
		Event event = eventService.createNewEvent(players);
		event.setMaxNumberOfRounds(eventService.getMaxNumberOfRounds(listOfPlayersInEvent.size()));
		EventSettingsPageController settingsController = openNewWindow("com/andwari/event/EventSettings.fxml");
		settingsController.init(event, this, true);
	}

	private EventSettingsPageController openNewWindow(String xhtmlPath) {
		EventSettingsPageController controller = null;
		FXMLLoader fxmlLoader = fxmlLoaderInst.get();
		try {
			URL fxmlRes = getClass().getClassLoader().getResource(xhtmlPath);
			fxmlLoader.setLocation(fxmlRes);
			BorderPane root = fxmlLoader.load();
			Scene scene = new Scene(root);
			Stage newWindow = new Stage();
			newWindow.setScene(scene);
			controller = fxmlLoader.getController();
			newWindow.show();
		} catch (Exception e) {
			logger.error(e);
		}
		return controller;
	}

	public void continueEvent(Event event, boolean manuelly) {
		if (manuelly) {
			continueEventManually(event);
		} else {
			continueEvent(event);
		}
	}

	private void continueEvent(Event event) {
		FXMLLoader fxmlLoader = fxmlLoaderInst.get();
		try {
			fxmlLoader.setLocation(FxmlResource.EVENT_SEATINGS.getResourceUrl());
			BorderPane root = (BorderPane) fxmlLoader.load();
			SeatingsPageController controller = fxmlLoader.getController();
			controller.setEvent(event);
			controller.setStage(stage);
			controller.init();

			stage.getScene().setRoot(root);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void continueEventManually(Event event) {
		FXMLLoader fxmlLoader = fxmlLoaderInst.get();
		try {
			fxmlLoader.setLocation(FxmlResource.PAIRINGS.getResourceUrl());
			BorderPane root = (BorderPane) fxmlLoader.load();
			PairingsPageController controller = fxmlLoader.getController();
			controller.setEvent(event);
			controller.setStage(stage);

			stage.getScene().setRoot(root);

			controller.init();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
