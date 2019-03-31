package com.andwari.playermanagement;

import javax.inject.Inject;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditPlayerPageController {

	@FXML
	private TextField tfName, tfDci;
	
	@FXML
	private CheckBox cbMember;
	
	@Inject
	private TabPlayerService playerService;
	
	private PlayerDVO player;
	
	
	public void init(PlayerDVO player) {
		this.player = player;
		tfName.setText(player.getName());
		tfDci.setText(player.getDci());
		cbMember.setSelected(player.getMember());
	}
	
	public void save() {
		player.setName(tfName.getText());
		player.setDci(tfDci.getText());
		player.setMember(cbMember.isSelected());
		playerService.updatePlayer(player);
		Stage stage = (Stage) tfName.getScene().getWindow();
		stage.close();
	}
}
