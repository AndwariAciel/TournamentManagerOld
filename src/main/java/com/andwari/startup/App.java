package com.andwari.startup;

import java.net.URL;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.kohsuke.randname.RandomNameGenerator;

import com.andwari.core.tournamentcore.database.DatabaseManager;
import com.andwari.core.tournamentcore.player.control.PlayerController;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class App {
	
	@Inject
	FXMLLoader fxmlLoader;
	
	public void start(@Observes @StartupScene Stage stage) {
//		DatabaseManager.purgeDatabase();
		DatabaseManager.init();
//		test();
		try {

			URL fxmlRes = getClass().getResource("mainWindow.fxml");
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
	
	@SuppressWarnings("unused")
	private void test() {
		PlayerController playerController = new PlayerController();
		RandomNameGenerator rnd = new RandomNameGenerator(0);
		try {
			for(int x=0; x < 100; x++) {
				playerController.addPlayer(rnd.next(), Integer.toString((int)(Math.random()*1000000)));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
