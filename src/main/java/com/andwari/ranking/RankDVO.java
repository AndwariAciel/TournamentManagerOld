package com.andwari.ranking;

import javafx.beans.property.SimpleStringProperty;

public class RankDVO {

	private SimpleStringProperty rank;
	private SimpleStringProperty player;
	private SimpleStringProperty points;
	
	public RankDVO() {
		rank = new SimpleStringProperty();
		player = new SimpleStringProperty();
		points = new SimpleStringProperty();
	}

	public SimpleStringProperty getRankProperty() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank.set(rank);
	}
	
	public String getRank() {
		return rank.get();
	}

	public SimpleStringProperty getPlayerProperty() {
		return player;
	}

	public void setPlayer(String player) {
		this.player.set(player);
	}
	
	public String getPlayer() {
		return player.get();
	}

	public SimpleStringProperty getPointsProperty() {
		return points;
	}

	public void setPoints(String points) {
		this.points.set(points);
	}
	
	public String getPoints() {
		return points.get();
	}
	
}
