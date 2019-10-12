package com.andwari.event.seatings;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import com.andwari.core.tournamentcore.event.EventRepository;
import com.andwari.core.tournamentcore.event.boundary.EventService;
import com.andwari.core.tournamentcore.event.entity.Event;
import com.andwari.core.tournamentcore.event.entity.Round;
import com.andwari.core.tournamentcore.matches.MatchFactory;
import com.andwari.core.tournamentcore.player.entity.Player;
import com.andwari.event.matches.MatchesPageController;
import com.andwari.util.FxmlResource;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class SeatingsPageController {

	@FXML
	private TableView<SeatingsDvo> tvSeatings;

	@FXML
	private TableColumn<SeatingsDvo, String> tcSeat;
	@FXML
	private TableColumn<SeatingsDvo, String> tcPlayer;

	private ObservableList<SeatingsDvo> listOfSeatings;

	private Event event;

	@Inject
	private FXMLLoader fxmlLoader;

	@Inject
	private EventService eventService;

	private Stage stage;

	@Inject
	private MatchFactory matchFactory;

	@Inject
	private SeatingsDvoConverter converter;

	@Inject
	private EventRepository eventRepos;

	public void setEvent(Event event) {
		this.event = event;
	}

	public void init() {
		listOfSeatings = FXCollections.observableArrayList();
		List<Player> seatings = eventService.createSeatings(event);
		for (int x = 1; x <= seatings.size(); x++) {
			listOfSeatings.add(converter.convertToDto(seatings.get(x - 1), x));
		}

		tcSeat.setCellValueFactory(cellData -> cellData.getValue().getSeatingNumberProperty());
		tcPlayer.setCellValueFactory(cellData -> cellData.getValue().getPlayerNameProperty());

		tcSeat.setSortable(false);
		tcPlayer.setSortable(false);

		tvSeatings.setItems(listOfSeatings);
		tvSeatings.setSelectionModel(null);
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public void startFirstRound() {
		Round round1 = matchFactory.createCrosspairings(event);
		event.getRounds().add(round1);
		event.setCurrentRound(1);
		eventRepos.update(event);

		try {
			fxmlLoader.setLocation(FxmlResource.EVENT_MATCHES_EVENTMATCHES.getResourceUrl());
			BorderPane root = (BorderPane) fxmlLoader.load();
			MatchesPageController controller = fxmlLoader.getController();
			controller.initialize(round1);

			stage.getScene().setRoot(root);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
