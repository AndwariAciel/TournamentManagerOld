package com.andwari.ranking;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;
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

	public int parseMonth(String selectedItem) {
		Optional<Month> month = Stream.of(Month.values()).filter(m -> m.getName().equals(selectedItem)).findFirst();
		if (month.isPresent()) {
			return month.get().getNumber();
		}
		return -1;
	}

	public String getCurrentMonth() {
		int month = Calendar.getInstance().get(Calendar.MONTH);
		return Stream.of(Month.values()).filter(m -> m.getNumber() == month).map(m -> m.getName()).findFirst().get();
	}

}
