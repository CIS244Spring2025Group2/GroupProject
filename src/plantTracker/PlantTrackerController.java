package plantTracker;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import user.SessionManager;
import user.User;

public class PlantTrackerController implements Initializable {

	@FXML
	private Parent root;

	@FXML
	private TextArea remindersArea;

	@FXML
	private Button viewPlantsButton;

	@FXML
	private Button viewRemindersButton;

	@FXML
	private MenuItem logout;

	@FXML
	private MenuItem updatePassword;

	@FXML
	private MenuItem editUserInfo;

	@FXML
	private MenuItem manageUsers;

	private User loggedInUser = SessionManager.getCurrentUser();

	@Override
	public void initialize(URL url, ResourceBundle rb) {

		updateAdminButtonVisibility();
	}

	private void updateAdminButtonVisibility() {
		if (loggedInUser != null && loggedInUser.isAdmin()) {
			manageUsers.setVisible(true);
		} else {
			manageUsers.setVisible(false);
		}
	}

	// Method to handle the "Manage Users" button click
	@FXML
	private void handleManageUsers(ActionEvent event) {
		// Load the ManageUsersController scene
		switchScene(event, "/user/resources/ManageUsers.fxml", "Manage Users");
		if (loggedInUser != null && loggedInUser.isAdmin()) {
			switchScene(event, "/user/ManageUsers.fxml", "Manage Users");
		} else {
			showAlert("Access Denied", "You do not have administrator privileges.");
		}
	}

	@FXML
	public void handleUpdatePassword(ActionEvent event) {
		SessionManager.logoutUser();
		switchScene(event, "/user/resources/ResetPassword.fxml", "Login");
	}

	@FXML
	public void handleEditUserInfo(ActionEvent event) {
		SessionManager.logoutUser();
		switchScene(event, "/user/resources/EditUser.fxml", "Login");
	}

	@FXML
	public void handleLogout(ActionEvent event) {
		SessionManager.logoutUser();
		switchScene(event, "/user/resources/Login.fxml", "Login");
	}

	@FXML
	public void handleViewPlants(ActionEvent event) {
		switchScene(event, "/plantTracker/resources/ViewPlants.fxml", "View Plants");
	}

	@FXML
	public void handleViewReminders(ActionEvent event) {
		switchScene(event, "/plantTracker/resources/ViewReminders.fxml", "View Reminders");
	}

	public void switchScene(ActionEvent event, String fxmlFile, String title) {
		try {
			Parent loader = FXMLLoader.load(getClass().getResource(fxmlFile));
			Scene newScene = new Scene(loader);
			Stage stage = (Stage) root.getScene().getWindow();
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
