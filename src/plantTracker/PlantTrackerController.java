package plantTracker;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import user.SessionManager;
import user.User;

public class PlantTrackerController implements Initializable {

	@FXML
	private TextArea remindersArea;

	@FXML
	private Button viewPlantsButton;

	@FXML
	private Button viewRemindersButton;

	@FXML
	private Button manageUsersButton;

	@FXML
	private Button logout;

	private User loggedInUser = SessionManager.getCurrentUser();

	@Override
	public void initialize(URL url, ResourceBundle rb) {

		updateAdminButtonVisibility();
	}

	private void updateAdminButtonVisibility() {
		if (loggedInUser != null && loggedInUser.isAdmin()) {
			manageUsersButton.setVisible(true);
			manageUsersButton.setManaged(true); // Ensures it takes up layout space
		} else {
			manageUsersButton.setVisible(false);
			manageUsersButton.setManaged(false); // Ensures it doesn't take up layout space
		}
	}

	// Method to handle the "Manage Users" button click
	@FXML
	private void handleManageUsers(ActionEvent event) {
		// Load the ManageUsersController scene
		switchScene(event, "/user/resources/ManageUsers.fxml", "Manage Users");
	}

	@FXML
	public void handleViewPlants(ActionEvent event) {
		switchScene(event, "/plantTracker/resources/ViewPlants.fxml", "View Plants"); // replace with your actual file
																						// path if needed
	}

	@FXML
	public void handleViewReminders(ActionEvent event) {
		switchScene(event, "/plantTracker/resources/ViewReminders.fxml", "View Reminders"); // replace with your actual
																							// file path if needed
	}

	@FXML
	public void handleLogout(ActionEvent event) {
		SessionManager.logoutUser();
		switchScene(event, "/user/resources/Login.fxml", "Login"); // replace with your actual file path if
																	// needed
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

}
