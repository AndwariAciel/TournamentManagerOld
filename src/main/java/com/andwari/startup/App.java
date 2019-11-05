package com.andwari.startup;

import java.net.URL;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

import com.andwari.core.tournamentcore.database.DatabaseManager;
import com.andwari.password.PasswordHandler;
import com.andwari.util.FxmlResource;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class App {

	@Inject
	FXMLLoader fxmlLoader;

	@Inject
	private PasswordHandler pwHandler;

	public void start(@Observes @StartupScene Stage stage) {
//		DatabaseManager.purgeDatabase();
		DatabaseManager.init();
		pwHandler.initDefaultMasterPassword();
		try {

			URL fxmlRes = getClass().getClassLoader().getResource(FxmlResource.MAIN.getPath());
			fxmlLoader.setLocation(fxmlRes);
			TabPane root = fxmlLoader.load();
			Scene scene = new Scene(root);

			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
