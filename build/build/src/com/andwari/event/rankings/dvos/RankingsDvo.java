package com.andwari.event.rankings.dvos;

import javafx.beans.property.SimpleStringProperty;

public class RankingsDvo {

	private Long standingId;
	
	private SimpleStringProperty rank;
	
	private SimpleStringProperty player;
	
	private SimpleStringProperty score;
	
	private SimpleStringProperty scoreString;
	
	private SimpleStringProperty opGamesScore;
	
	private SimpleStringProperty matchScore;
	
	private SimpleStringProperty opMatchScore;
	
	public String getRank() {
		return rank.get();
	}
	
	public void setRank(String rank) {
		this.rank = new SimpleStringProperty(rank);
	}
	
	public SimpleStringProperty getRankProperty() {
		return rank;
	}
	
	public String getPlayer() {
		return player.get();
	}
	
	public void setPlayer(String player) {
		this.player = new SimpleStringProperty(player);
	}
	
	public SimpleStringProperty getPlayerProperty() {
		return player;
	}
	
	public String getScore() {
		return score.get();
	}
	
	public void setScore(String score) {
		this.score = new SimpleStringProperty(score);
	}
	
	public SimpleStringProperty getScoreProperty() {
		return score;
	}
	
	public String getOpGamesScore() {
		return opGamesScore.get();
	}
	
	public void setOpGamesScorer(String opGamesScore) {
		this.opGamesScore = new SimpleStringProperty(opGamesScore);
	}
	
	public SimpleStringProperty getOpGamesScoreProperty() {
		return opGamesScore;
	}
	
	public String getMatchScore() {
		return matchScore.get();
	}
	
	public void setMatchScore(String matchScore) {
		this.matchScore = new SimpleStringProperty(matchScore);
	}
	
	public SimpleStringProperty getMatchScoreProperty() {
		return matchScore;
	}
	
	public String getOpMatchScore() {
		return opMatchScore.get();
	}
	
	public void setOpMatchScore(String opMatchScore) {
		this.opMatchScore = new SimpleStringProperty(opMatchScore);
	}
	
	public SimpleStringProperty getOpMatchScoreProperty() {
		return opMatchScore;
	}

	public Long getStandingId() {
		return standingId;
	}

	public void setStandingId(Long standingId) {
		this.standingId = standingId;
	}

	public String getScoreString() {
		return scoreString.get();
	}
	
	public void setScoreString(String scoreString) {
		this.scoreString = new SimpleStringProperty(scoreString);
	}

	public SimpleStringProperty getScoreStringProperty() {
		return scoreString;
	}


}
