package com.andwari.password;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.GridPane;

public class PasswordDialog extends Dialog<String> {

	/**
	 * From:<br>
	 * https://code.makery.ch/blog/javafx-dialogs-official/
	 */
	public PasswordDialog() {
		super();
		setTitle("Password");
		setHeaderText("Organizer Password required");

		// Set the icon (must be included in the project).
//		dialog.setGraphic(new ImageView(this.getClass().getResource("login.png").toString()));

		// Set the button types.
		ButtonType loginButtonType = new ButtonType("OK", ButtonData.OK_DONE);
		getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

		// Create the username and password labels and fields.
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 100, 10, 10));

		PasswordField password = new PasswordField();
		password.setPromptText("Organizer Password");

		grid.add(new Label("Organizer Password:"), 0, 0);
		grid.add(password, 1, 0);

		getDialogPane().setContent(grid);

		// Request focus on the username field by default.
		Platform.runLater(() -> password.requestFocus());

		// Convert the result to a username-password-pair when the login button is
		// clicked.
		setResultConverter(dialogButton -> {
			if (dialogButton == loginButtonType) {
				return password.getText();
			}
			return null;
		});

	}

}
