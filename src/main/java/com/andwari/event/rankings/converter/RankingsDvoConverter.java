package com.andwari.event.rankings.converter;

import java.math.RoundingMode;

import javax.inject.Inject;

import com.andwari.core.tournamentcore.event.entity.Standing;
import com.andwari.core.tournamentcore.standings.StandingsService;
import com.andwari.event.rankings.dvos.RankingsDvo;

public class RankingsDvoConverter {
	
	@Inject
	private StandingsService standingService;

	public RankingsDvo convertToDvo(Standing standing) {
		RankingsDvo dvo = new RankingsDvo();
		dvo.setPlayer(standing.getPlayer().getPlayerName());
		dvo.setScore(Integer.toString(standing.getScore()));
		dvo.setOpGamesScorer(standing.getOpponentGameWinPercentage().setScale(4, RoundingMode.HALF_UP).toPlainString());
		dvo.setOpMatchScore(standing.getOpponentMatchWinPercentage().setScale(4, RoundingMode.HALF_UP).toPlainString());
		dvo.setMatchScore(standing.getMatchWinPercentage().setScale(4, RoundingMode.HALF_UP).toPlainString());
		dvo.setScoreString(standingService.getScoreString(standing));
		dvo.setStandingId(standing.getId());
		return dvo;
	}
	
}
