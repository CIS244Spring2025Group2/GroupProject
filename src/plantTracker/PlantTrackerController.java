package plantTracker;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class PlantTrackerController {
	@FXML
	private TextArea remindersArea;
	@FXML
	private Button viewPlantsButton;
	@FXML
	private Button viewRemindersButton;

	@FXML
	private Button logout;

	@FXML
	public void handleViewPlants(ActionEvent event) {
		switchScene(event, "ViewPlants.fxml", "View Plants"); // replace with your actual file path if needed
	}

	@FXML
	public void handleViewReminders(ActionEvent event) {
		switchScene(event, "ViewReminders.fxml", "View Reminders"); // replace with your actual file path if needed
	}

	@FXML
	public void handleLogout(ActionEvent event) {
		switchScene(event, "Login.fxml", "Login"); // replace with your actual file path if needed
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
