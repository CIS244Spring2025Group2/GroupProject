package user;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import util.SessionManager;

public class ManageUsersController implements Initializable {

	@FXML
	private TextField searchBar;

	@FXML
	private Button home;

	@FXML
	private Button makeAdmin;

	@FXML
	private Button delete;

	@FXML
	private VBox userList;

	private String selectedEmail;

	private ObservableList<String> data = FXCollections.observableArrayList();

	private UserDAO userDAO = new UserDAO();

	private User loggedInUser = SessionManager.getCurrentUser();

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		loadUserEmails(); // Call method to load emails from the database

		// Real-time search functionality
		FilteredList<String> filteredData = new FilteredList<>(data, s -> true);
		searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredData.setPredicate(item -> {
				// If search bar is empty, display all items
				if (newValue.isEmpty()) {
					return true;
				}
				// Filter items that contain the search query
				return item.toLowerCase().contains(newValue.toLowerCase());
			});
		});
		// Adds list to GUI
		ListView<String> listView = new ListView<>(filteredData);
		userList.getChildren().add(listView);
		// Have listView grow with VBox it is in
		listView.prefHeightProperty().bind(userList.heightProperty());

		// Prints which plant has been selected in list
		listView.setOnMouseClicked(e -> {
			selectedEmail = listView.getSelectionModel().getSelectedItem();
			if (selectedEmail != null) {
				System.out.println("Selected User: " + selectedEmail);
				// Enable delete and makeAdmin buttons when a user is selected
				delete.setDisable(false);
				makeAdmin.setDisable(false);
			} else {
				// Disable buttons if no user is selected
				delete.setDisable(true);
				makeAdmin.setDisable(true);
			}
		});

		// Stops search-bar from automatically being highlighted when scene is switched
		searchBar.setFocusTraversable(false);

		// Initially disable delete and makeAdmin buttons
		delete.setDisable(true);
		makeAdmin.setDisable(true);
	}

	private void loadUserEmails() {
		try {
			List<String> emails = userDAO.getAllUserEmails(); // Create this method in UserDAO
			data.clear();
			data.addAll(emails);
		} catch (SQLException e) {
			showAlert("Error", "Error loading user emails: " + e.getMessage());
			e.printStackTrace();
		}
	}

	@FXML
	private void handleMakeAdmin() {
		if (selectedEmail != null) {
			makeAdmin(selectedEmail);
		} else {
			showAlert("Warning", "Please select a user to make admin.");
		}
	}

	private void makeAdmin(String email) {
		try {
			UserDAO userDAO = new UserDAO();
			userDAO.updateUserAdminStatus(email, true); // Create this method in UserDAO
			showAlert("Success", "User '" + email + "' is now an admin.");
			loadUserEmails(); // Reload the list to reflect changes
		} catch (SQLException e) {
			showAlert("Error", "Database error updating user: " + e.getMessage());
			e.printStackTrace();
		}
	}

	@FXML
	private void handleDeleteUser() {
		if (selectedEmail != null) {
			deleteUser(selectedEmail);
		} else {
			showAlert("Warning", "Please select a user to delete.");
		}
	}

	private void deleteUser(String email) {
		if (!loggedInUser.getEmail().equals(email)) {
			try {
				UserDAO userDAO = new UserDAO();
				userDAO.deleteUser(email); // Ensure this method exists in UserDAO
				showAlert("Success", "User '" + email + "' deleted successfully.");
				loadUserEmails(); // Reload the list to reflect changes
			} catch (SQLException e) {
				showAlert("Error", "Database error deleting user: " + e.getMessage());
				e.printStackTrace();
			}
		} else {
			showAlert("Not Allowed", "You cannot delete yourself.");

		}
	}

	@FXML
	private void handlePlantTracker(ActionEvent event) {
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
}