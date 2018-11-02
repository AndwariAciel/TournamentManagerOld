package com.andwari.event.seatings;

import java.util.List;

import com.andwari.core.tournamentcore.event.boundary.EventService;
import com.andwari.core.tournamentcore.event.entity.Event;
import com.andwari.core.tournamentcore.event.entity.Match;
import com.andwari.core.tournamentcore.event.entity.Round;
import com.andwari.core.tournamentcore.matches.MatchFactory;
import com.andwari.core.tournamentcore.player.entity.Player;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class SeatingsPageController {

	@FXML
	private TableView<SeatingsDvo> tvSeatings;
	
	@FXML
	private TableColumn<SeatingsDvo, String> tcSeat;
	@FXML
	private TableColumn<SeatingsDvo, String> tcPlayer;
	
	private ObservableList<SeatingsDvo> listOfSeatings;
	
	private Event event;
	
	public void setEvent(Event event) {
		this.event = event;
	}
	
	public void init() {		
		listOfSeatings = FXCollections.observableArrayList();
		List<Player> seatings = EventService.createSeatings(event);
		event.setSeatings(seatings);
		for(int x = 1; x <= seatings.size(); x++) {
			listOfSeatings.add(SeatingsDvoConverter.convertToDto(seatings.get(x-1), x));
		}
		
		tcSeat.setCellValueFactory(cellData -> cellData.getValue().getSeatingNumberProperty());
		tcPlayer.setCellValueFactory(cellData -> cellData.getValue().getPlayerNameProperty());
		
		tcSeat.setSortable(false);
		tcPlayer.setSortable(false);
		
		tvSeatings.setItems(listOfSeatings);	
		tvSeatings.setSelectionModel(null);
	}
	
	public void startFirstRound() {
		Round round1 = MatchFactory.createCrosspairings(event);
		System.out.println("------------Matches-------------");
		if(round1.getBye() != null) {
			System.out.println("-- Bye: " + round1.getBye().getPlayerName());
			System.out.println();
		}
		for(Match m : round1.getMatches()) {
			System.out.println(m.getPlayer1().getPlayerName() + " vs. " + m.getPlayer2().getPlayerName());
		}
	}
}
