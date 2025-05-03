package plantTracker.controller;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import plantTracker.database.ReminderDAO;
import plantTracker.model.Reminder;
import util.ShowAlert;

public class ViewRemindersController implements Initializable {

	@FXML
	private Label sceneLabel;

	@FXML
	private VBox reminderVBox;

	@FXML
	private HBox bottomBar;

	@FXML
	private TextField searchBar;

	@FXML
	private Button home;

	@FXML
	private Button viewPlants;

	@FXML
	private Button updateReminder;

	@FXML
	private Button deleteReminder;

	@FXML
	private Button addReminder;

	private ReminderDAO reminderDAO = new ReminderDAO();
	private ObservableList<Reminder> data = FXCollections.observableArrayList();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		bottomBar.prefHeightProperty().bind(sceneLabel.heightProperty());

		try {
			data.addAll(reminderDAO.populateList());
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// Real-time search functionality (adjust predicate for Reminder objects if
		// needed)
		FilteredList<Reminder> filteredData = new FilteredList<>(data, reminder -> true);
		searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredData.setPredicate(reminder -> {
				if (newValue.isEmpty()) {
					return true;
				}
				String searchText = newValue.toLowerCase();
				return reminder.getPlantName().toLowerCase().contains(searchText)
						|| reminder.getReminderType().toLowerCase().contains(searchText)
						|| String.valueOf(reminder.getDate()).contains(searchText)
						|| reminder.getDescription().toLowerCase().contains(searchText);
			});
		});

		// Adds list to GUI and sets the cell factory
		ListView<Reminder> listView = new ListView<>(filteredData);
		reminderVBox.getChildren().add(listView);
		listView.prefHeightProperty().bind(reminderVBox.heightProperty());

		// **Setting the Cell Factory**
		listView.setCellFactory(param -> new ListCell<Reminder>() {
			@Override
			protected void updateItem(Reminder item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
					setGraphic(null);
				} else {
					String displayText = String.format("%s | Date: %s", item.getDescription(), item.getDate());
					setText(displayText);

				}
			}
		});

		// Prints which plant has been selected in list
		listView.setOnMouseClicked(e -> {
			Reminder selectedReminder = listView.getSelectionModel().getSelectedItem();
			if (selectedReminder != null) {
				ReminderDAO.setSelectedReminder(selectedReminder);
				System.out.println("Selected Reminder: " + ReminderDAO.getSelectedReminder().getPlantName() + " - "
						+ ReminderDAO.getSelectedReminder().getReminderType());
			}
		});

		// Stops search-bar from automatically being highlighted when scene is switched
		searchBar.setFocusTraversable(false);
	}

	@FXML
	private void handleDeleteReminder(ActionEvent event) {
		Reminder selectedReminder = ReminderDAO.getSelectedReminder();
		if (selectedReminder != null) {
			try {
				reminderDAO.deleteReminder(selectedReminder.getReminderId());
				data.remove(selectedReminder); // Remove the deleted item from the ObservableList
				ReminderDAO.clearSelectedReminder(); // Clear the selected reminder after deletion
				ShowAlert.showAlert("Success", "Reminder deleted successfully.");
			} catch (SQLException e) {
				ShowAlert.showAlert("Error", "Error deleting reminder.");
				e.printStackTrace();
			}
		} else {
			ShowAlert.showAlert("Warning", "Please select a reminder to delete.");
		}
	}

	@FXML
	private void handlePlantTracker(ActionEvent event) {
		util.SceneSwitcher.switchScene(event, "/plantTracker/resources/PlantTracker.fxml", "Plant Tracker");
	}

	@FXML
	private void handleViewPlants(ActionEvent event) {
		util.SceneSwitcher.switchScene(event, "/plantTracker/resources/ViewPlants.fxml", "Plant List");
	}

	@FXML
	private void handleAddReminder(ActionEvent event) {
		util.SceneSwitcher.switchScene(event, "/plantTracker/resources/AddReminder.fxml", "Add Reminder");
	}

	@FXML
	private void handleUpdateReminder(ActionEvent event) {
		ReminderDAO.setSelectedReminder(ReminderDAO.getSelectedReminder());
		util.SceneSwitcher.switchScene(event, "/plantTracker/resources/UpdateReminder.fxml", "Update Reminder");
	}

}
