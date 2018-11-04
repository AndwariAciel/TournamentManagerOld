package com.andwari.main.player;

import javax.inject.Inject;

import com.andwari.core.tournamentcore.player.exceptions.NameNotUniqueException;
import com.andwari.util.ColumnsFactory;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class TabPlayerController {
	
	@FXML
	private TextField tfSearch;
	
	@FXML
	private TextField tfPlayername;
	
	@FXML
	private TextField tfDciNumber;
	
	@FXML 
	private Button btnAddPlayer;
	
	@FXML
	private Label lbWarning;
	
	@FXML
	private Button btnDeletePlayer;
	
	private ObservableList<PlayerDVO> listOfPlayers;
	
	@Inject
	private TabPlayerService tabPlayerService;
	
	@FXML
	private TableView<PlayerDVO> tvListOfPlayers;
		
	@FXML
    public void initialize() {
		listOfPlayers = FXCollections.observableArrayList(tabPlayerService.getAllPlayersFromDatabase());		

		tvListOfPlayers.setItems(listOfPlayers);
		tvListOfPlayers.getColumns().setAll(ColumnsFactory.getColumnsPlayerWithId());

		tfSearch.textProperty().addListener((observable, oldValue, newValue) -> {
		    updateListOfPlayers(newValue);
		});
    }
	
	private void updateListOfPlayers(String searchValue) {		
		listOfPlayers.clear();
		for(PlayerDVO player : tabPlayerService.filterList(searchValue)) {
			listOfPlayers.add(player);
		}
		
	}

	public void addPlayer() {
		lbWarning.setVisible(false);
		String name = tfPlayername.getText();
		String dci = tfDciNumber.getText();
		
		PlayerDVO dvo;
		try {
			dvo = tabPlayerService.createNewPlayer(name, dci);
			listOfPlayers.add(dvo);
			tfPlayername.setText("");
			tfDciNumber.setText("");
		} catch (NameNotUniqueException e) {
			lbWarning.setVisible(true);
		}
	}
	
	public void deletePlayer() {
		PlayerDVO player = tvListOfPlayers.getSelectionModel().getSelectedItem();
		tabPlayerService.deletePlayer(player);
		listOfPlayers.remove(player);
	}
	
	
}
