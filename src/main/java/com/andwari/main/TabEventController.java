package com.andwari.main;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.apache.log4j.Logger;

import com.andwari.event.playerselection.PlayerSelectionController;
import com.andwari.password.PasswordHandler;
import com.andwari.util.FxmlResource;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class TabEventController {

	@Inject
	private Instance<FXMLLoader> instance;

	@Inject
	private PasswordHandler password;

	@FXML
	private Button startTournamentBtn, startEliminationBtn;

	private Logger logger = Logger.getLogger(this.getClass());

	@FXML
	public void initialize() {
		Image tournamentImage = new Image(getClass().getClassLoader().getResourceAsStream("images/trophy.png"));
		startTournamentBtn.setGraphic(new ImageView(tournamentImage));
		startTournamentBtn.setText(null);
		startTournamentBtn.setOnAction(event -> createNewEvent());

		Image eliminationImage = new Image(getClass().getClassLoader().getResourceAsStream("images/elimination.png"));
		startEliminationBtn.setGraphic(new ImageView(eliminationImage));
		startEliminationBtn.setText(null);
		startEliminationBtn.setOnAction(event -> notYet());
	}

	private void notYet() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Not yet...");
		alert.setHeaderText("Not yet implemented. Coming soon...");
		alert.setContentText(null);
		alert.showAndWait();
	}

	public void createNewEvent() {
		if (!password.askForMasterPassword()) {
			return;
		}
		PlayerSelectionController controller = null;
		try {
			FXMLLoader loader = instance.get();
			loader.setLocation(FxmlResource.EVENT_PLAYER_SELECTION.getResourceUrl());
			BorderPane root = loader.load();
			Scene scene = new Scene(root);
			Stage newWindow = new Stage();
			newWindow.setScene(scene);
			controller = loader.getController();
			controller.setStage(newWindow);
			newWindow.show();
		} catch (Exception e) {
			logger.error(e);
		}
	}

}
