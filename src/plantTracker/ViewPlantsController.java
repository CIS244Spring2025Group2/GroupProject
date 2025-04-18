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
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class ViewPlantsController implements Initializable {
	
	@FXML 
	private Label sceneLabel;
	
	@FXML 
	private HBox bottomBar;

	@FXML
	private Button addPlant;

	@FXML
	private Button back;
	
	@FXML
	private void toStats(ActionEvent event) {
		switchScene(event, "ViewPlantStats.fxml", "Plant Stats"); // replace with your actual file path if needed
	}

	@FXML
	public void handleAddPlant(ActionEvent event) {
		switchScene(event, "AddPlant.fxml", "Add Plant"); // replace with your actual file path if needed
	}

	@FXML
	public void handlePlantTracker(ActionEvent event) {
		switchScene(event, "PlantTracker.fxml", "Plant Tracker"); // replace with your actual file path if needed
	}
	
	@FXML
	private void handleViewReminders(ActionEvent event) {
		switchScene(event, "ViewReminders.fxml", "View Reminders"); // replace with your actual file path if needed
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

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//Allows for bottom-bar to always to be the same height as label at top
		bottomBar.prefHeightProperty().bind(sceneLabel.heightProperty());
	}
}
