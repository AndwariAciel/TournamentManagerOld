package com.andwari.playermanagement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import com.andwari.core.tournamentcore.player.boundary.PlayerService;
import com.andwari.core.tournamentcore.player.entity.Player;
import com.andwari.core.tournamentcore.player.exceptions.NameNotUniqueException;

public class TabPlayerService {

	@Inject
	private PlayerService playerService;	
	
	public Collection<PlayerDVO> getAllPlayersFromDatabase() {
		ArrayList<PlayerDVO> players = new ArrayList<>();
		for (Player p : playerService.getListOfAllPlayers()) {
			players.add(convertFromPlayer(p));
		}
		return players;
	}

	public PlayerDVO convertFromPlayer(Player player) {
		PlayerDVO dvo = new PlayerDVO();

		dvo.setId(String.valueOf(player.getId()));
		dvo.setName(player.getPlayerName());
		dvo.setDci(player.getDci());
		dvo.setMember(player.getMember());
		return dvo;
	}

	public PlayerDVO createNewPlayer(String name, String dci, Boolean membership) throws NameNotUniqueException {
		Player player = playerService.addPlayer(name, dci, membership);
		return convertFromPlayer(player);
	}

	public void deletePlayer(PlayerDVO player) {
		long id = Long.parseLong(player.getId());
		playerService.deletePlayer(id);

	}

	public List<PlayerDVO> filterList(String searchValue) {
		ArrayList<PlayerDVO> filteredPlayers = new ArrayList<>();
		for (PlayerDVO player : getAllPlayersFromDatabase()) {
			if (filterPlayer(player.getName(), searchValue)) {
				filteredPlayers.add(player);
			}
		}
		return filteredPlayers;
	}

	private boolean filterPlayer(String name, String searchValue) {
		if (name == null) {
			return false;
		}
		if (searchValue.isEmpty()) {
			return true;
		}
		String smallName = name.toLowerCase();
		String smallSearch = searchValue.toLowerCase();
		return smallName.contains(smallSearch);

	}

}
