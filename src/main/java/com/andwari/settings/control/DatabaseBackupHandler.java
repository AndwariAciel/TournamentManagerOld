package com.andwari.settings.control;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;

import com.andwari.core.tournamentcore.database.DatabaseManager;
import com.andwari.password.PasswordHandler;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class DatabaseBackupHandler {

	@Inject
	private PasswordHandler pwHandler;

	public void restoreDatabase(Stage stage) {
		if (!pwHandler.askForMasterPassword()) {
			return;
		}
		FileChooser fileChooser = new FileChooser();
		File selectedFile = fileChooser.showOpenDialog(stage);
		if (selectedFile != null) {
			if (DatabaseManager.restore(selectedFile)) {
				showDialogSucces("Database successfully restored");
			} else {
				showDialogFailed("Error restoring Database! Please conatct Admin");
			}
		}
	}

	public void backupDatabase(String path) {
		File backupDir = new File(path);
		boolean backupSuccesful = false;
		if (backupDir.exists()) {
			File backup = new File(backupDir, getBackupFilename());
			try {
				backup.createNewFile();
				backupSuccesful = DatabaseManager.backup(backup);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (backupSuccesful) {
				showDialogSucces("Backup successful!\n" + backup.getAbsolutePath());
			} else {
				showDialogFailed("Backup failed! Contact Admin");
			}
		} else {
			showDialogFailed("Please enter a path to an existing folder.");
		}
	}

	private void showDialogFailed(String message) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Failed");
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.show();
		return;
	}

	private void showDialogSucces(String message) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Success");
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.show();
		return;

	}

	private String getBackupFilename() {
		StringBuilder sb = new StringBuilder();
		sb.append("backup-");
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_hh-mm");
		Date date = new Date();
		sb.append(dateFormat.format(date));
		sb.append(".zip");
		return sb.toString();
	}

}
