package user;

import java.io.IOException;
import java.sql.SQLException;

import database.ProjUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ResetPasswordController {

	@FXML
	private TextField email;

	@FXML
	private Button submit;

	@FXML
	private Label securityQuestion;

	@FXML
	private Label securityAnswerLabel;

	@FXML
	private TextField securityAnswer;

	@FXML
	private Label passwordLabel;

	@FXML
	private PasswordField password;

	@FXML
	private Label confirmPasswordLabel;

	@FXML
	private PasswordField confirmPassword;

	@FXML
	private Button save;

	@FXML
	private Button cancel;

	private UserDAO userDAO = new UserDAO();

	private User user = null;

	@FXML
	public void getSecurityQuestion(ActionEvent event) {
		String enteredEmail = email.getText();

		try {
			user = userDAO.getUser(enteredEmail);

			if (user != null) {

				securityQuestion.setVisible(true);
				securityQuestion.setManaged(true);

				securityAnswerLabel.setVisible(true);
				securityAnswerLabel.setManaged(true);

				securityAnswer.setVisible(true);
				securityAnswer.setManaged(true);

				passwordLabel.setVisible(true);
				passwordLabel.setManaged(true);

				password.setVisible(true);
				password.setManaged(true);

				confirmPasswordLabel.setVisible(true);
				confirmPasswordLabel.setManaged(true);

				confirmPassword.setVisible(true);
				confirmPassword.setManaged(true);

				save.setVisible(true);
				save.setManaged(true);

				securityQuestion.setText(user.getSecurityQuestion());

			} else {
				showAlert("Invalid User Details", "Invalid email or password.");
			}

		} catch (SQLException e) {
			showAlert("Databse Error", "Database error fetching user.");
			e.printStackTrace();
		}
	}

	@FXML
	public void save(ActionEvent event) {

		String securityAnswerString = securityAnswer.getText();
		String passwordString = password.getText();
		String confirmPasswordString = confirmPassword.getText();

		if (user != null && user.getSecurityAnswer().equals(ProjUtil.getSHA(securityAnswerString))) {
			if (passwordString.equals(confirmPasswordString) && !passwordString.isEmpty()) {
				try {
					userDAO.updatePassword(user.getEmail(), passwordString);
					showAlert("Password Reset",
							"Your password has been reset successfully. Please log in with your new password.");
					switchScene(event, "/user/resources/Login.fxml", "Login");
				} catch (SQLException e) {
					showAlert("Database Error", "Error updating password.");
					e.printStackTrace();
				}
			} else {
				showAlert("Mismatch Passwords", "Make sure your password fields match");
			}
		} else {
			showAlert("Invalid User Details", "Invalid Security Answer");
		}
	}

	@FXML
	public void cancel(ActionEvent event) {
		switchScene(event, "/user/resources/Login.fxml", "Login");
	}

	public void switchScene(ActionEvent event, String fxmlFile, String title) {
		try {
			Parent loader = FXMLLoader.load(getClass().getResource(fxmlFile));
			Scene newScene = new Scene(loader);
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setScene(newScene);
			stage.setTitle(title);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void showAlert(String title, String content) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(content);
		alert.showAndWait();
	}

}