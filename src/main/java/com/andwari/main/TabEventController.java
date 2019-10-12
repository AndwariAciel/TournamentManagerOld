package com.andwari.main;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.apache.log4j.Logger;

import com.andwari.event.playerselection.PlayerSelectionController;
import com.andwari.util.FxmlResource;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class TabEventController {

	@Inject
	private Instance<FXMLLoader> instance;

	@FXML
	private Button startEventBtn;

	private Logger logger = Logger.getLogger(this.getClass());

	public void createNewEvent() {
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
