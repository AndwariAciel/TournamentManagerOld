package com.andwari.event.seatings;

import javafx.beans.property.SimpleStringProperty;

public class SeatingsDvo {

	private SimpleStringProperty seatingNumber = new SimpleStringProperty();
	
	private SimpleStringProperty playerName = new SimpleStringProperty();
	
	public String getSeatingNumber() {
		return seatingNumber.get();
	}
	
	public void setSeatingNumber(String seatingNumber) {
		this.seatingNumber.set(seatingNumber);
	}
	
	public SimpleStringProperty getSeatingNumberProperty() {
		return seatingNumber;
	}
	
	public String getPlayerName() {
		return playerName.get();
	}
	
	public void setPlayerName(String playerName) {
		this.playerName.set(playerName);
	}
	
	public SimpleStringProperty getPlayerNameProperty() {
		return playerName;
	}
	
}
