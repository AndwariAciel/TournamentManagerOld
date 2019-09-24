package com.andwari.event.pairings;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.andwari.core.tournamentcore.event.entity.Event;
import com.andwari.core.tournamentcore.player.entity.Player;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TextField;
import javafx.scene.input.DataFormat;
import javafx.stage.Stage;

public class PairingsPageController {

	public static final DataFormat dndKey = new DataFormat("dndKey");

	@FXML
	private ListView<Player> playersLeft, playersRight, playersList;

	@FXML
	private Button btnNext;

	@FXML
	private TextField byeText;

	@FXML
	private Label byeLabel;

	private ObservableList<Player> listLeft, listRight, listPlayers;

	private Event event;
	private Stage stage;

	public void init() {

		/*
		 * Populate lists with empty players depending on actual number of players add
		 * drag and drop events
		 */

		listPlayers = FXCollections.observableArrayList(getPlayersFromEvent());
		playersList.setItems(listPlayers);
		playersList.setCellFactory(new PlayerListCellFactory());

		listRight = FXCollections.observableArrayList(getEmptyPlayers(listPlayers.size()));
		playersRight.setItems(listRight);
//		playersRight.setStyle("-fx-background-color: white;");
		playersRight.setCellFactory(new PairingsListCellFactory(playersList));

		listLeft = FXCollections.observableArrayList(getEmptyPlayers(listPlayers.size()));
		playersLeft.setItems(listLeft);
		playersLeft.setCellFactory(new PairingsListCellFactory(playersList));
	}

	public void linkViews() {
		Node n1 = playersRight.lookup(".scroll-bar");
		if (n1 instanceof ScrollBar) {
			final ScrollBar bar1 = (ScrollBar) n1;
			Node n2 = playersLeft.lookup(".scroll-bar");
			if (n2 instanceof ScrollBar) {
				final ScrollBar bar2 = (ScrollBar) n2;
				bar1.valueProperty().bindBidirectional(bar2.valueProperty());
			}
		}

	}

	private List<Player> getEmptyPlayers(int size) {
		ArrayList<Player> emptyList = new ArrayList<>();
		for (int x = 0; x < size / 2; x++) {
			emptyList.add(new EmptyPlayer());
		}
		return emptyList;
	}

	private List<Player> getPlayersFromEvent() {
		return event.getRankings().stream().map(r -> r.getPlayer()).collect(Collectors.toList());
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

}
