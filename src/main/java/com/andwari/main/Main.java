package com.andwari.main;

import org.kohsuke.randname.RandomNameGenerator;

import com.andwari.core.tournamentcore.database.DatabaseManager;
import com.andwari.core.tournamentcore.player.control.PlayerController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		DatabaseManager.purgeDatabase();
		DatabaseManager.init();
		test();
		try {

			TabPane root = (TabPane) FXMLLoader.load(getClass().getResource("mainWindow.fxml"));
			Scene scene = new Scene(root);

			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

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

	public static void main(String[] args) {
		launch(args);
	}

	
	
}
