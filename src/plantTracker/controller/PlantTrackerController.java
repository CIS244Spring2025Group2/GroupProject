package plantTracker.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import user.User;
import util.SessionManager;

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
		util.SceneSwitcher.switchScene(event, "/user/resources/ManageUsers.fxml", "Manage Users", root);
		if (loggedInUser != null && loggedInUser.isAdmin()) {
			util.SceneSwitcher.switchScene(event, "/user/ManageUsers.fxml", "Manage Users");
		} else {
			util.ShowAlert.showAlert("Access Denied", "You do not have administrator privileges.");
		}
	}

	@FXML
	public void handleUpdatePassword(ActionEvent event) {
		util.SceneSwitcher.switchScene(event, "/user/resources/UpdatePassword.fxml", "Update Password", root);
	}

	@FXML
	public void handleEditUserInfo(ActionEvent event) {
		util.SceneSwitcher.switchScene(event, "/user/resources/EditUser.fxml", "Edit User", root);
	}

	@FXML
	public void handleLogout(ActionEvent event) {
		SessionManager.logoutUser();
		util.SceneSwitcher.switchScene(event, "/user/resources/Login.fxml", "Login", root);
	}

	@FXML
	public void handleViewPlants(ActionEvent event) {
		util.SceneSwitcher.switchScene(event, "/plantTracker/resources/ViewPlants.fxml", "View Plants");
	}

	@FXML
	public void handleViewReminders(ActionEvent event) {
		util.SceneSwitcher.switchScene(event, "/plantTracker/resources/ViewReminders.fxml", "View Reminders");
	}

}
