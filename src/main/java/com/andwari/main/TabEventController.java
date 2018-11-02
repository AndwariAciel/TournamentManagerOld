package com.andwari.main;

import com.andwari.event.pagecontroller.EventPlayerSelectionController;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class TabEventController {
	
	public void createNewEvent() {
		try {

			FXMLLoader loader = new FXMLLoader(getClass().getResource("../event/EventPlayerSelection.fxml"));
			BorderPane root = (BorderPane) loader.load();
			Scene scene = new Scene(root);
			
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			Stage newWindow = new Stage();
			newWindow.setScene(scene);
			EventPlayerSelectionController controller = loader.getController();
			controller.setStage(newWindow);
			newWindow.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
