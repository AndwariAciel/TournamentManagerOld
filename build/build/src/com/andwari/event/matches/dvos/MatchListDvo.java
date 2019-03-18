package com.andwari.event.matches.dvos;

import javafx.beans.property.SimpleStringProperty;

public class MatchListDvo {
	
	private Long matchId;
	
	private boolean finished = false;

	private SimpleStringProperty matchNumber;
	
	private SimpleStringProperty player1;
	
	private SimpleStringProperty player2;
	
	private SimpleStringProperty winsPlayer1;
	
	private SimpleStringProperty winsPlayer2;

	public int getMatchNumber() {
		return Integer.parseInt(matchNumber.get());
	}

	public void setMatchNumber(int matchNumber) {
		this.matchNumber = new SimpleStringProperty(Integer.toString(matchNumber));
	}
	
	public SimpleStringProperty getMatchNumberProperty() {
		return matchNumber;
	}

	public SimpleStringProperty getPlayer1Property() {
		return player1;
	}

	public void setPlayer1(String player) {
		this.player1 = new SimpleStringProperty(player);
	}
	
	public String getPlayer1() {
		return player1.get();
	}

	public SimpleStringProperty getPlayer2Property() {
		return player2;
	}

	public void setPlayer2(String player) {
		this.player2 = new SimpleStringProperty(player);
	}
	
	public String getPlayer2() {
		return player2.get();
	}

	public SimpleStringProperty getWinsPlayer1Property() {
		return winsPlayer1;
	}

	public void setWinsPlayer1(int wins) {
		this.winsPlayer1 = new SimpleStringProperty(Integer.toString(wins));
	}
	
	public int getWinsPlayer1() {
		return Integer.parseInt(winsPlayer1.get());
	}
	public SimpleStringProperty getWinsPlayer2Property() {
		return winsPlayer2;
	}

	public void setWinsPlayer2(int wins) {
		this.winsPlayer2 = new SimpleStringProperty(Integer.toString(wins));
	}
	
	public int getWinsPlayer2() {
		return Integer.parseInt(winsPlayer2.get());
	}

	public Long getMatchId() {
		return matchId;
	}

	public void setMatchId(Long matchId) {
		this.matchId = matchId;
	}

	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}
	
	
	
}
