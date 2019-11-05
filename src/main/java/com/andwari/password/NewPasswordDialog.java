package com.andwari.password;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

public class NewPasswordDialog extends Dialog<Pair<String, String>> {

	public NewPasswordDialog() {
		super();
		setTitle("New Password");
		setHeaderText("Enter a new password.");

		// Set the icon (must be included in the project).
//		dialog.setGraphic(new ImageView(this.getClass().getResource("login.png").toString()));

		// Set the button types.
		ButtonType loginButtonType = new ButtonType("OK", ButtonData.OK_DONE);
		getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

		// Create the username and password labels and fields.
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));

		PasswordField password = new PasswordField();
		password.setPromptText("Password");
		PasswordField passwordRepeat = new PasswordField();
		passwordRepeat.setPromptText("Repeat");

		grid.add(new Label("Password:"), 0, 0);
		grid.add(password, 1, 0);
		grid.add(new Label("Repeat:"), 0, 1);
		grid.add(passwordRepeat, 1, 1);

		getDialogPane().setContent(grid);

		// Request focus on the username field by default.
		Platform.runLater(() -> password.requestFocus());

		// Convert the result to a username-password-pair when the login button is
		// clicked.
		setResultConverter(dialogButton -> {
			if (dialogButton == loginButtonType) {
				return new Pair<>(password.getText(), passwordRepeat.getText());
			}
			return null;
		});

	}

}
