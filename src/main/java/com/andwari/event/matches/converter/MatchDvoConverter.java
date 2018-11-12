package com.andwari.event.matches.converter;

import javax.inject.Singleton;

import com.andwari.core.tournamentcore.event.entity.Match;
import com.andwari.core.tournamentcore.event.entity.Round;
import com.andwari.event.matches.dvos.MatchListDvo;

@Singleton
public class MatchDvoConverter {
	
	public MatchListDvo convertToDvo(Match match) {
		MatchListDvo dvo = new MatchListDvo();
		dvo.setPlayer1(match.getPlayer1().getPlayerName());
		dvo.setPlayer2(match.getPlayer2().getPlayerName());
		dvo.setWinsPlayer1(match.getScorePlayer1());
		dvo.setWinsPlayer2(match.getScorePlayer2());
		dvo.setMatchId(match.getId());
		return dvo;
	}

	public Match convertToEntity(MatchListDvo dvo, Round round) {
		for(Match match : round.getMatches()) {
			if(match.getId() == dvo.getMatchId()) {
				match.setScorePlayer1(dvo.getWinsPlayer1());
				match.setScorePlayer2(dvo.getWinsPlayer2());
				return match;
			}
		}
		return null;
	}
}
