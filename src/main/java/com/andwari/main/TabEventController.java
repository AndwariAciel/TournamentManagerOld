package com.andwari.main;

import java.net.URL;

import javax.inject.Inject;

import com.andwari.event.playerselection.PlayerSelectionController;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class TabEventController {
	
	@Inject 
	private FXMLLoader loader;
	
	public void createNewEvent() {
		try {

			URL fxmlRes = getClass().getResource("../event/EventPlayerSelection.fxml");
			loader.setLocation(fxmlRes);
			BorderPane root = loader.load();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("../startup/application.css").toExternalForm());
			Stage newWindow = new Stage();
			newWindow.setScene(scene);
			PlayerSelectionController controller = loader.getController();
			controller.setStage(newWindow);
			newWindow.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
