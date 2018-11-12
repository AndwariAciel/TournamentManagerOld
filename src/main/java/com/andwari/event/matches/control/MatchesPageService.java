package com.andwari.event.matches.control;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.andwari.core.tournamentcore.event.entity.Event;
import com.andwari.core.tournamentcore.event.entity.Match;
import com.andwari.core.tournamentcore.event.entity.Round;
import com.andwari.core.tournamentcore.event.entity.Standing;
import com.andwari.core.tournamentcore.standings.StandingsService;
import com.andwari.event.matches.converter.MatchDvoConverter;
import com.andwari.event.matches.dvos.MatchListDvo;
import com.andwari.event.rankings.converter.RankingsDvoConverter;
import com.andwari.event.rankings.dvos.RankingsDvo;

public class MatchesPageService {

	@Inject
	private MatchDvoConverter converter;
	
	@Inject
	private StandingsService standingService;

	@Inject
	private RankingsDvoConverter rankingsConverter;
	
	public List<MatchListDvo> getListOfDvos(Round round) {
		List<MatchListDvo> listOfDvos = new ArrayList<>();
		for (Match match : round.getMatches()) {
			listOfDvos.add(converter.convertToDvo(match));
		}
		return listOfDvos;
	}

	public ArrayList<RankingsDvo> getRankings(Event event) {
		ArrayList<Standing> orderdStandings = standingService.getRankings(event);
		ArrayList<RankingsDvo> orderedDvos = new ArrayList<>();
		int rank = 1;
		for(Standing standing : orderdStandings) {
			RankingsDvo dvo = rankingsConverter.convertToDvo(standing);
			dvo.setRank(Integer.toString(rank));
			orderedDvos.add(dvo);
			rank++;
			
		}
		return orderedDvos;
	}
}