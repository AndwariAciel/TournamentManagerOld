package com.andwari.playermanagement;

import java.util.ArrayList;
import java.util.List;

import com.andwari.core.tournamentcore.player.entity.Player;

import javafx.collections.ObservableList;

public class PlayerConverter {

	public List<Player> convertBackToPlayer(ObservableList<PlayerDVO> listOfDVOs) {
		List<Player> players = new ArrayList<>();
		for(PlayerDVO dvo : listOfDVOs) {
			players.add(convert(dvo));
		}
		
		return players;
	}

	private Player convert(PlayerDVO dvo) {
		Player p = new Player();
		p.setId(dvo.getId());
		p.setPlayerName(dvo.getName());
		p.setDci(dvo.getDci());
		p.setMember(dvo.getMember());
		return p;
	}
	
}
