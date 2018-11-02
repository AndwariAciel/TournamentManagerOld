package com.andwari.main.player;

import java.util.ArrayList;
import java.util.List;

import com.andwari.core.tournamentcore.player.entity.Player;

import javafx.collections.ObservableList;

public class PlayerConverter {

	public static List<Player> convertBackToPlayer(ObservableList<PlayerDVO> listOfDVOs) {
		List<Player> players = new ArrayList<>();
		for(PlayerDVO dvo : listOfDVOs) {
			players.add(convert(dvo));
		}
		
		return players;
	}

	private static Player convert(PlayerDVO dvo) {
		Player p = new Player();
		p.setId(Long.parseLong(dvo.getId()));
		p.setPlayerName(dvo.getName());
		p.setDci(dvo.getDci());
		return p;
	}
	
}
