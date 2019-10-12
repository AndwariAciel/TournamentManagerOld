package com.andwari.main;

import java.io.IOException;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import com.andwari.core.tournamentcore.player.exceptions.NameNotUniqueException;
import com.andwari.playermanagement.EditPlayerPageController;
import com.andwari.playermanagement.PlayerDVO;
import com.andwari.playermanagement.TabPlayerService;
import com.andwari.util.FxmlResource;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

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
	private CheckBox cbMember;

	@FXML
	private Button btnDeletePlayer;

	@Inject
	private Instance<FXMLLoader> fxmlLoaderInst;

	@Inject
	private TabPlayerService tabPlayerService;

	@FXML
	private TableView<PlayerDVO> tvListOfPlayers;
	private ObservableList<PlayerDVO> listOfPlayers;

	@FXML
	private TableColumn<PlayerDVO, String> tcName, tcDci, tcMember;

	@FXML
	public void initialize() {
		listOfPlayers = FXCollections.observableArrayList(tabPlayerService.getAllPlayersFromDatabase());

		tvListOfPlayers.setItems(listOfPlayers);
		tcName.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
		tcDci.setCellValueFactory(cellData -> cellData.getValue().dciProperty());
		tcMember.setCellValueFactory(cellData -> cellData.getValue().memberProperty());

		tfSearch.textProperty().addListener((observable, oldValue, newValue) -> {
			updateListOfPlayers(newValue);
		});

		tvListOfPlayers.setOnMouseClicked((MouseEvent event) -> {
			if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
				editPlayer(tvListOfPlayers.getSelectionModel().getSelectedItem());
			}
		});
	}

	private void updateListOfPlayers(String searchValue) {
		listOfPlayers.clear();
		for (PlayerDVO player : tabPlayerService.filterList(searchValue)) {
			listOfPlayers.add(player);
		}

	}

	public void addPlayer() {
		lbWarning.setVisible(false);
		String name = tfPlayername.getText();
		String dci = tfDciNumber.getText();
		Boolean membership = cbMember.isSelected();
		PlayerDVO dvo;
		try {
			dvo = tabPlayerService.createNewPlayer(name, dci, membership);
			listOfPlayers.add(dvo);
			tfPlayername.setText("");
			tfDciNumber.setText("");
			cbMember.setSelected(true);
		} catch (NameNotUniqueException e) {
			lbWarning.setVisible(true);
		}
	}

	public void deletePlayer() {
//		PlayerDVO player = tvListOfPlayers.getSelectionModel().getSelectedItem();
//		tabPlayerService.deletePlayer(player);
//		listOfPlayers.remove(player);
	}

	private void editPlayer(PlayerDVO playerDVO) {
		FXMLLoader fxmlLoader = fxmlLoaderInst.get();
		try {
			fxmlLoader.setLocation(FxmlResource.PLAYERMANAGEMENT_EDIT.getResourceUrl());
			BorderPane root = fxmlLoader.load();
			Scene scene = new Scene(root);
			Stage newWindow = new Stage();
			newWindow.setScene(scene);
			EditPlayerPageController controller = fxmlLoader.getController();
			controller.init(playerDVO);
			newWindow.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
