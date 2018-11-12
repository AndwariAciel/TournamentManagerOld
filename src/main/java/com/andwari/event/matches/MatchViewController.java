package com.andwari.event.matches;

import javax.inject.Inject;

import com.andwari.core.tournamentcore.event.entity.Match;
import com.andwari.core.tournamentcore.event.entity.Round;
import com.andwari.core.tournamentcore.matches.MatchService;
import com.andwari.event.matches.converter.MatchDvoConverter;
import com.andwari.event.matches.dvos.MatchListDvo;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;

public class MatchViewController {
	
	private MatchListDvo match;
	
	@FXML
	private Label lbPlayer1, lbPlayer2, lbScore1, lbScore2, lbStatus;
	
	@FXML
	private FlowPane fpPlayer1, fpPlayer2, fpScore1, fpScore2;
	
	@FXML 
	private Button submitButton;
	
	@Inject
	private MatchDvoConverter converter;
	
	@Inject
	private MatchService matchService;
	
	private Round round;
	private MatchesPageController matchesPageController;
	
	private static final String BUTTON_SUBMIT = "submit";
	private static final String BUTTON_REVOKE = "revoke";
	
	@FXML
	public void initialize() {
		fpPlayer1.setOnMouseClicked((MouseEvent event) -> {
			updateScore1(event);
		});
		fpScore1.setOnMouseClicked((MouseEvent event) -> {
			updateScore1(event);
		});
		fpPlayer2.setOnMouseClicked((MouseEvent event) -> {
			updateScore2(event);
		});
		fpScore2.setOnMouseClicked((MouseEvent event) -> {
			updateScore2(event);
		});
	}
	
	private void updateScore1(MouseEvent event) {
		if(event.getButton().equals(MouseButton.PRIMARY) && match.getWinsPlayer1() <2) {
			match.setWinsPlayer1(match.getWinsPlayer1() + 1);
		} else if (event.getButton().equals(MouseButton.SECONDARY) && match.getWinsPlayer1() > 0) {
			match.setWinsPlayer1(match.getWinsPlayer1() - 1);
		}
		lbScore1.setText(Integer.toString(match.getWinsPlayer1()));
	}
	
	private void updateScore2(MouseEvent event) {
		if(event.getButton().equals(MouseButton.PRIMARY) && match.getWinsPlayer2() <2) {
			match.setWinsPlayer2(match.getWinsPlayer2() + 1);
		} else if (event.getButton().equals(MouseButton.SECONDARY) && match.getWinsPlayer2() > 0) {
			match.setWinsPlayer2(match.getWinsPlayer2() - 1);
		}
		lbScore2.setText(Integer.toString(match.getWinsPlayer2()));
	}

	protected void updateMatch(MatchListDvo match) {
		this.match = match;
		lbPlayer1.setText(match.getPlayer1());
		lbPlayer2.setText(match.getPlayer2());
		lbScore1.setText(Integer.toString(match.getWinsPlayer1()));
		lbScore2.setText(Integer.toString(match.getWinsPlayer2()));
		if(match.isFinished()) {
			submitButton.setText(BUTTON_REVOKE);
		} else {
			submitButton.setText(BUTTON_SUBMIT);
		}
	}
	
	public void submitMatch() {
		if(!match.isFinished()) {
			finishMatch();
		} else {
			revokeMatch();
		}
	}

	public void init(MatchesPageController matchesPageController, Round round) {
		this.round = round;
		this.matchesPageController = matchesPageController;
	}
	
	private void finishMatch() {
		Match matchEntity = converter.convertToEntity(match, round);
		try {
			matchService.finishMatch(matchEntity);
		} catch(Exception e) {
			e.printStackTrace();
			return;
		}
		match.setFinished(true);
		submitButton.setText(BUTTON_REVOKE);
		matchesPageController.updateMatch(matchEntity, match);
	}
	
	private void revokeMatch() {
		Match matchEntity = converter.convertToEntity(match, round);
		match.setWinsPlayer1(0);
		match.setWinsPlayer2(0);
		matchService.revokeMatch(matchEntity);
		match.setFinished(false);
		submitButton.setText(BUTTON_SUBMIT);
		lbScore1.setText("0");
		lbScore2.setText("0");
		matchesPageController.updateMatch(matchEntity, match);
	}

}
