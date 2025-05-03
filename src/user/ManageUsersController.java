package user;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import util.SceneSwitcher;
import util.SessionManager;
import util.ShowAlert;

public class ManageUsersController implements Initializable {

	@FXML
	private TextField searchBar;

	@FXML
	private Button home;

	@FXML
	private Button makeAdmin;

	@FXML
	private Button revokeAdmin;

	@FXML
	private Button delete;

	@FXML
	private VBox userList;

	private ObservableList<User> data = FXCollections.observableArrayList();

	private UserDAO userDAO = new UserDAO();

	private User loggedInUser = SessionManager.getCurrentUser();

	private User selectedUser;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		loadUserEmails(); // Call method to load User objects from the database

		// Real-time search functionality (filter based on User object properties)
		FilteredList<User> filteredData = new FilteredList<>(data, user -> true);
		searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredData.setPredicate(user -> {
				// If search bar is empty, display all items
				if (newValue.isEmpty()) {
					return true;
				}
				// Filter items that contain the search query in email or other relevant fields
				String searchText = newValue.toLowerCase();
				return user.getEmail().toLowerCase().contains(searchText)
						|| (user.getFirstName() != null && user.getFirstName().toLowerCase().contains(searchText))
						|| (user.getLastName() != null && user.getLastName().toLowerCase().contains(searchText));
			});
		});

		// Adds list to GUI and sets the cell factory to display User information
		ListView<User> listView = new ListView<>(filteredData);
		userList.getChildren().add(listView);
		listView.prefHeightProperty().bind(userList.heightProperty());

		// **Setting the Cell Factory to display User email and admin status**
		listView.setCellFactory(param -> new ListCell<User>() {
			@Override
			protected void updateItem(User user, boolean empty) {
				super.updateItem(user, empty);
				if (empty || user == null) {
					setText(null);
				} else {
					setText(user.getEmail() + (user.isAdmin() ? " (Admin)" : ""));
				}
			}
		});

		// Handles user selection in the ListView
		listView.setOnMouseClicked(e -> {
			User selectedUser = listView.getSelectionModel().getSelectedItem();
			if (selectedUser != null) {
				// Store the selected User object
				this.selectedUser = selectedUser;
				System.out.println("Selected User: " + this.selectedUser.getEmail());
				// Enable delete and makeAdmin buttons when a user is selected
				delete.setDisable(false);
				makeAdmin.setDisable(false);
				revokeAdmin.setDisable(false); // Enable revoke button
			} else {
				// Disable buttons if no user is selected
				delete.setDisable(true);
				makeAdmin.setDisable(true);
				revokeAdmin.setDisable(true); // Disable revoke button
			}
		});

		// Stops search-bar from automatically being highlighted when scene is switched
		searchBar.setFocusTraversable(false);

		// Initially disable delete and makeAdmin buttons
		delete.setDisable(true);
		makeAdmin.setDisable(true);
		revokeAdmin.setDisable(true);
	}

	private void loadUserEmails() {
		try {
			List<User> users = userDAO.getAllUsersWithAdminStatus(); // Get User objects
			data.clear();
			data.addAll(users);
		} catch (SQLException e) {
			ShowAlert.showAlert("Error", "Error loading user emails: " + e.getMessage());
			e.printStackTrace();
		}
	}

	@FXML
	private void handleMakeAdmin() {
		if (selectedUser != null) { // Check selectedUser
			makeAdmin(selectedUser.getEmail());
		} else {
			ShowAlert.showAlert("Warning", "Please select a user to make admin.");
		}
	}

	@FXML
	private void handleRevokeAdmin() {
		if (selectedUser != null) { // Check selectedUser
			revokeAdmin(selectedUser.getEmail());
		} else {
			ShowAlert.showAlert("Warning", "Please select a user to revoke admin.");
		}
	}

	private void makeAdmin(String email) {
		try {
			User selectedUser = userDAO.getUser(email);
			if (selectedUser != null && !selectedUser.isAdmin()) {
				userDAO.updateUserAdminStatus(email, true); // Create this method in UserDAO
				ShowAlert.showAlert("Success", "User '" + email + "' is now an admin.");
				loadUserEmails(); // Reload the list to reflect changes
			} else {
				ShowAlert.showAlert("Error", "User '" + email + "' is already an admin or not found.");
			}
		} catch (SQLException e) {
			ShowAlert.showAlert("Error", "Database error updating user: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private void revokeAdmin(String email) {
		if (loggedInUser != null && !loggedInUser.getEmail().equals(email)) {
			try {
				User selectedUser = userDAO.getUser(email);
				if (selectedUser != null && selectedUser.isAdmin()) {
					userDAO.updateUserAdminStatus(email, false); // Create this method in UserDAO
					ShowAlert.showAlert("Success", "User '" + email + "' is no longer an admin.");
					loadUserEmails(); // Reload the list to reflect changes
				} else {
					ShowAlert.showAlert("Error", "User '" + email + "' is not an admin or not found.");
				}
			} catch (SQLException e) {
				ShowAlert.showAlert("Error", "Database error updating user: " + e.getMessage());
				e.printStackTrace();
			}
		} else {
			ShowAlert.showAlert("Not Allowed", "You cannot revoke your own admin privilages.");
		}
	}

	@FXML
	private void handleDeleteUser() {
		if (selectedUser != null) { // Check selectedUser
			deleteUser(selectedUser.getEmail());
		} else {
			ShowAlert.showAlert("Warning", "Please select a user to delete.");
		}
	}

	private void deleteUser(String email) {
		if (loggedInUser != null && !loggedInUser.getEmail().equals(email)) {
			try {
				UserDAO userDAO = new UserDAO();
				userDAO.deleteUser(email); // Ensure this method exists in UserDAO
				ShowAlert.showAlert("Success", "User '" + email + "' deleted successfully.");
				loadUserEmails(); // Reload the list to reflect changes
			} catch (SQLException e) {
				ShowAlert.showAlert("Error", "Database error deleting user: " + e.getMessage());
				e.printStackTrace();
			}
		} else {
			ShowAlert.showAlert("Not Allowed", "You cannot delete yourself.");
		}
	}

	@FXML
	private void handlePlantTracker(ActionEvent event) {
		SceneSwitcher.switchScene(event, "/plantTracker/resources/PlantTracker.fxml", "Plant Tracker");
	}
}