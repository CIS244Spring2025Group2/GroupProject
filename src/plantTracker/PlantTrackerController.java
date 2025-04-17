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
import javafx.stage.Stage;

public class PlantTrackerController implements Initializable {

	@FXML
	Button addPlant;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub

	}

	public void goToAddPlant(ActionEvent event) throws IOException {
		Parent addPlantParent = FXMLLoader.load(getClass().getResource("AddPlant.fxml"));
		Scene addPlantScene = new Scene(addPlantParent);

		// This line gets the Stage information
		Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

		window.setScene(addPlantScene);
		window.setTitle("Add Plant");
		window.show();
	}

//	public PlantTracker() {
//		display();
//	}
//
//	public void display() {
//		// TODO Auto-generated method stub
//
//		Stage stage = new Stage();
//
//		// Create buttons
//		Button viewPlantsButton = new Button("View Current Plants");
//		Button viewRemindersButton = new Button("View All Reminders");
//
//		// Add event handlers for navigation
//		viewPlantsButton.setOnAction(e -> new ViewPlants());
//		viewRemindersButton.setOnAction(e -> new ViewReminders());
//
//		// Create TextArea for upcoming reminders
//		TextArea remindersArea = new TextArea();
//		remindersArea.setEditable(false);
//		remindersArea.setPromptText("Upcoming reminders will appear here...");
//
//		// Layout setup
//		VBox layout = new VBox(15);
//		layout.setPadding(new Insets(20));
//		layout.getChildren().addAll(viewPlantsButton, viewRemindersButton, remindersArea);
//
//		// Create Scene
//		Scene homeScene = new Scene(layout, 400, 300);
//
//		// Set up the stage
//		stage.setTitle("Plant Tracker - Home");
//		stage.setScene(homeScene);
//		stage.show();
//
//	}

}
