package plantTracker;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ViewPlantsController implements Initializable {
	
	private PlantDAO plantDAO = new PlantDAO();
	private ObservableList<String> data = FXCollections.observableArrayList();

	@FXML
	private Label sceneLabel;

	@FXML
	private HBox bottomBar;

	@FXML
	private TextField searchBar;

	@FXML
	private VBox plantList;

	@FXML
	public void handleAddPlant(MouseEvent event) {
		switchScene(event, "/plantTracker/resources/AddPlant.fxml", "Add Plant");
	}

	@FXML
	private void removePlant() {
		try {
			plantDAO.deletePlant(PlantDAO.getSelectedName());
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		data.remove(PlantDAO.getSelectedName());
	}
	
	@FXML
	private void toStats(MouseEvent e) {
		PlantDAO.setSelectedName(PlantDAO.getSelectedName());
		switchScene(e, "/plantTracker/resources/ViewPlantStats.fxml", "Plant Stats");
	}

	@FXML
	public void handlePlantTracker(MouseEvent event) {
		switchScene(event, "/plantTracker/resources/PlantTracker.fxml", "Plant Tracker");
	}

	@FXML
	private void handleViewReminders(MouseEvent event) {
		switchScene(event, "/plantTracker/resources/ViewReminders.fxml", "View Reminders");
	}

	public void switchScene(MouseEvent event, String fxmlFile, String title) {
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

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		// Allows for bottom-bar to always to be the same height as label at top
		bottomBar.prefHeightProperty().bind(sceneLabel.heightProperty());

		// Fetches plants from database for list
		try {
			plantDAO.populateList(data);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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
		plantList.getChildren().add(listView);
		listView.prefHeightProperty().bind(plantList.heightProperty()); // Have listView grow with VBox it is in

		// Prints which plant has been selected in list
		listView.setOnMouseClicked(e -> {
			String selectedItem = listView.getSelectionModel().getSelectedItem();
			PlantDAO.setSelectedName(selectedItem);
			if (selectedItem != null) {
				System.out.println("Selected Plant: " + PlantDAO.getSelectedName());
			}
		});

		// Stops search-bar from automatically being highlighted when scene is switched
		searchBar.setFocusTraversable(false);
	}
}
