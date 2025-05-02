package user;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import util.ProjUtil;
import util.SessionManager;

public class UpdatePasswordController implements Initializable {

	@FXML
	private Label currentUser;

	@FXML
	private Label currentPasswordLabel;

	@FXML
	private PasswordField currentPassword;

	@FXML
	private Label newPasswordLabel;

	@FXML
	private PasswordField newPassword;

	@FXML
	private Label confirmNewPasswordLabel;

	@FXML
	private PasswordField confirmNewPassword;

	@FXML
	private Button save;

	@FXML
	private Button cancel;

	private UserDAO userDAO = new UserDAO();

	private User loggedInUser;

	@FXML
	public void save(ActionEvent event) {

		String currentPasswordString = currentPassword.getText();
		String newPasswordString = newPassword.getText();
		String confirmNewPasswordString = confirmNewPassword.getText();

		if (loggedInUser != null && loggedInUser.getPassword().equals(ProjUtil.getSHA(currentPasswordString))) {
			if (newPasswordString.equals(confirmNewPasswordString) && !newPasswordString.isEmpty()) {
				try {
					userDAO.updatePassword(loggedInUser.getEmail(), newPasswordString);
					showAlert("Password Reset", "Your password has been reset successfully.");
					switchScene(event, "/plantTracker/resources/PlantTracker.fxml", "Plant Tracker");
				} catch (SQLException e) {
					showAlert("Database Error", "Error updating password.");
					e.printStackTrace();
				}
			} else {
				showAlert("Mismatch Passwords", "Make sure your password fields match");
			}
		} else {
			showAlert("Invalid User Details", "Invalid Current Password");
		}
	}

	@FXML
	public void cancel(ActionEvent event) {
		switchScene(event, "/plantTracker/resources/PlantTracker.fxml", "Plant Tracker");
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

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		loggedInUser = SessionManager.getCurrentUser();

		if (loggedInUser != null) {
			currentUser.setText(String.format("You are logged in as %s %s", loggedInUser.getFirstName(),
					loggedInUser.getLastName()));
		} else {
			System.err.println("Error: loggedInUser is null in UpdatePasswordController!");
		}

	}

}
