package plantTracker;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

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
				// Add more fields to search if needed
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
				ReminderDAO.setSelectedReminder(selectedReminder); // Assuming you create a static setSelectedReminder
																	// in ReminderDAO
				System.out.println("Selected Reminder: " + ReminderDAO.getSelectedReminder().getPlantName() + " - "
						+ ReminderDAO.getSelectedReminder().getReminderType());
			}
		});

		// Stops search-bar from automatically being highlighted when scene is switched
		searchBar.setFocusTraversable(false);
	}

	@FXML
	private void handlePlantTracker(ActionEvent event) {
		switchScene(event, "/plantTracker/resources/PlantTracker.fxml", "Plant Tracker");
	}

	@FXML
	private void handleViewPlants(ActionEvent event) {
		switchScene(event, "/plantTracker/resources/ViewPlants.fxml", "Plant List");
	}

	@FXML
	private void handleAddReminder(ActionEvent event) {
		switchScene(event, "/plantTracker/resources/AddReminder.fxml", "Add Reminder");
	}

	@FXML
	private void handleUpdateReminder(ActionEvent event) {
		ReminderDAO.setSelectedReminder(ReminderDAO.getSelectedReminder());
		switchScene(event, "/plantTracker/resources/UpdateReminder.fxml", "Update Reminder");
	}

	private void switchScene(ActionEvent event, String fxmlFile, String title) {
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
