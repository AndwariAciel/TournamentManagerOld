package com.andwari.password;

import java.util.Optional;

import javax.inject.Inject;

import com.andwari.core.tournamentcore.utils.PasswordManager;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.util.Pair;

public class PasswordHandler {

	@Inject
	private PasswordManager pwManager;

	public boolean askForMasterPassword() {
		PasswordDialog dialog = new PasswordDialog();

		Optional<String> password = dialog.showAndWait();
		if (password.isPresent()) {
			if (pwManager.verifyMasterPassword(password.get())) {
				return true;
			} else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText("Wrong Password");
				alert.showAndWait();
				return false;
			}
		} else {
			return false;
		}

	}

	public void initDefaultMasterPassword() {
		pwManager.initDefaulMasterPassword();
	}

	public void changeMasterPassword() {
		if (!askForMasterPassword()) {
			return;
		}
		boolean passwordsValid = false;
		while (!passwordsValid) {
			NewPasswordDialog dialog = new NewPasswordDialog();
			Optional<Pair<String, String>> result = dialog.showAndWait();
			if (result.isPresent() && result.get().getKey().equals(result.get().getValue())) {
				if (result.get().getKey().length() >= 4) {
					passwordsValid = true;
					pwManager.updateMasterPassword(result.get().getKey());
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Password set");
					alert.setHeaderText(null);
					alert.setContentText("The new organizer password is set.");
					alert.showAndWait();
				} else {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Error");
					alert.setHeaderText("Password too short. Needs to be longer than 4 characters.");
					alert.showAndWait();
				}

			} else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText("Passwords not matching!");
				alert.showAndWait();
			}
		}

	}

}
