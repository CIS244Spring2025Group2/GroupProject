package plantTracker;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
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
	private Button back;

	@FXML
	private Button addReminder;

	private List<Reminder> reminderList;

	// Optional setter if you're dynamically loading the reminders
	public void setReminderList(List<Reminder> reminders) {
		this.reminderList = reminders;
		populateReminders();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		bottomBar.prefHeightProperty().bind(sceneLabel.heightProperty());
		populateReminders(); // call it here if reminderList is already initialized
	}

	private void populateReminders() {
		if (reminderList == null)
			return;

		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");

		for (Reminder r : reminderList) {
			String plantName = r.getPlant().getName(); // assumes getName() exists
			Calendar date = r.getCalendar();
			String formatted = format.format(date.getTime());

			Button btn = new Button("Plant: " + plantName + " — Date: " + formatted);
			btn.setMaxWidth(Double.MAX_VALUE);
			btn.setStyle("-fx-font-size: 14; -fx-font-weight: bold");
			btn.setOnAction(e -> System.out.println("Clicked: " + plantName));

			reminderVBox.getChildren().add(btn);
		}
	}

	@FXML
	private void handlePlantTracker(ActionEvent event) {
		switchScene(event, "PlantTracker.fxml", "Plant Tracker");
	}

	@FXML
	private void handleViewPlants(ActionEvent event) {
		switchScene(event, "ViewPlants.fxml", "Plant List");
	}

	@FXML
	private void handleAddReminder(ActionEvent event) {
		switchScene(event, "AddReminder.fxml", "Add Reminder");
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
