package com.andwari.event.playerselection;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.inject.Inject;

import com.andwari.core.tournamentcore.event.boundary.EventService;
import com.andwari.core.tournamentcore.event.entity.Event;
import com.andwari.core.tournamentcore.player.entity.Player;
import com.andwari.event.seatings.SeatingsPageController;
import com.andwari.playermanagement.PlayerConverter;
import com.andwari.playermanagement.PlayerDVO;
import com.andwari.playermanagement.TabPlayerService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
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
	private FXMLLoader fxmlLoader;

	@Inject
	private EventService eventService;

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
		event.setMaxNumberOfRounds(askForMaxNumberOfRounds());
		try {
			URL fxmlRes = getClass().getResource("../EventSeatings.fxml");
			fxmlLoader.setLocation(fxmlRes);
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

	private int askForMaxNumberOfRounds() {
		int maxNumberOfRounds = eventService.getMaxNumberOfRounds(listOfPlayersInEvent.size());
		List<Integer> choices = IntStream.range(1, maxNumberOfRounds + 1).boxed().collect(Collectors.toList());
		ChoiceDialog<Integer> dialog = new ChoiceDialog<Integer>(choices.get(choices.size()-1), choices);
		dialog.setHeaderText("How many rounds do you want to play?");
		dialog.setContentText("Rounds: ");
		Integer choice = dialog.showAndWait().orElse(new Integer(0));
		return choice.intValue();
	}

}
