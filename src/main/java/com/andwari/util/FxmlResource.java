package com.andwari.util;

import java.net.URL;

public enum FxmlResource {

	MAIN("startup/mainWindow"), TAB_EVENT("startup/TabEvent"), TAB_PLAYERS("startup/TabPlayer"),
	TAB_RANKINGS("startup/TabRankings"), TAB_SETTINGS("startup/TabSettings"), TAB_START("startup/TabStart"),
	PLAYERMANAGEMENT_EDIT("playermanagement/PlayerEdit"), EVENT_MATCHES_EVENTMATCHES("event/matches/EventMatches"),
	EVENT_MATCHES_MATCH_LIST("event/matches/MatchListItem"), EVENT_MATCHES_MATCH_VIEW("event/matches/MatchView"),
	PAIRINGS("event/pairings/Pairings"), RANKINGS("event/rankings/RankingManually"),
	EVENT_PLAYER_SELECTION("event/EventPlayerSelection"), EVENT_SEATINGS("event/EventSeatings"),
	EVENT_SETTINGS("event/EventSettings"), EVENT_STANDINGS_FINAL("event/StandingFinalOverview"),
	EVENT_STANDINGS_OVERVIEW("event/StandingOverview");

	private final static String PREFIX = "com/andwari/";
	private final static String SUFFIX = ".fxml";
	private final String path;

	FxmlResource(String path) {
		this.path = PREFIX + path + SUFFIX;
	}

	public String getPath() {
		return path;
	}

	public URL getResourceUrl() {
		return getClass().getClassLoader().getResource(path);
	}
}
