package com.andwari.event.seatings;

import com.andwari.core.tournamentcore.player.entity.Player;

public class SeatingsDvoConverter {

	public SeatingsDvo convertToDto(Player p, int seatingNumber) {
		SeatingsDvo dvo = new SeatingsDvo();
		dvo.setPlayerName(p.getPlayerName());
		dvo.setSeatingNumber(String.valueOf(seatingNumber));
		return dvo;
	}
	
}
