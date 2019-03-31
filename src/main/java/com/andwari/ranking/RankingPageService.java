package com.andwari.ranking;

import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.andwari.core.tournamentcore.ranking.control.PlayerRank;

public class RankingPageService {

	public List<RankDVO> convertListToDVO(List<PlayerRank> ranks) {
		return ranks.stream().map(this::convert).collect(Collectors.toList());
	}

	private RankDVO convert(PlayerRank rank) {
		RankDVO dvo = new RankDVO();
		dvo.setRank(Integer.toString(rank.rank));
		dvo.setPlayer(rank.player.getPlayerName());
		dvo.setPoints(Integer.toString(rank.points));
		return dvo;
	}

	public Month getCurrentMonth() {
		int month = Calendar.getInstance().get(Calendar.MONTH);
		return Stream.of(Month.values()).filter(m -> m.getNumber() == month).findFirst().get();
	}

}
