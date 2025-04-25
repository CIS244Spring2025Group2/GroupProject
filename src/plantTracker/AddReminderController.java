package plantTracker;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AddReminderController implements Initializable {

	@FXML
	private ComboBox<String> reminderTypeComboBox;

	@FXML
	private ComboBox<String> plantComboBox;

	@FXML
	private DatePicker reminderDatePicker;

	@FXML
	private VBox dynamicFieldsContainer;

	@FXML
	private CheckBox recurringCheckBox;

	@FXML
	private ComboBox<String> intervalComboBox;

	@FXML
	private Button saveButton;

	@FXML
	private Button clearButton;

	// Store dynamic fields for later access
	private Map<String, Control> dynamicFields = new HashMap<>();

	private DbHelper dbHelper = new DbHelper();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Initialize reminder types
		reminderTypeComboBox
				.setItems(FXCollections.observableArrayList("Water", "Fertilize", "Repot", "Move", "Harvest"));

		// Initialize plant options
		plantComboBox.setItems(FXCollections.observableArrayList("Monstera", "Orchid", "Snake Plant", "Fiddle Leaf Fig",
				"Basil", "Pothos", "ZZ Plant", "Peace Lily", "Spider Plant", "Aloe Vera"));

		// Initialize interval options
		intervalComboBox.setItems(FXCollections.observableArrayList("Every day", "Every 3 days", "Every week",
				"Every 2 weeks", "Every month"));

		// Set default date to today
		reminderDatePicker.setValue(LocalDate.now());

		// Set interval combo box to be disabled initially
		intervalComboBox.setDisable(true);

		// Enable/disable interval combo box based on recurring checkbox
		recurringCheckBox.selectedProperty().addListener((obs, oldVal, newVal) -> {
			intervalComboBox.setDisable(!newVal);
		});

		// Add listener to reminder type selection
		reminderTypeComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
			if (newVal != null) {
				updateDynamicFields(newVal);
			}
		});
	}

	private void updateDynamicFields(String reminderType) {
		// Clear existing fields
		dynamicFieldsContainer.getChildren().clear();
		dynamicFields.clear();

		// Add specific fields based on reminder type
		switch (reminderType) {
		case "Water":
			addTextField("amountInMl", "Water Amount (ml)");
			break;

		case "Fertilize":
			addTextField("fertilizerType", "Fertilizer Type");
			addTextField("amount", "Amount");
			break;

		case "Repot":
			addTextField("newPotSize", "New Pot Size");
			addTextField("soilType", "Soil Type");
			break;

		case "Move":
			addTextField("newLocation", "New Location");
			addTextField("reason", "Reason");
			break;

		case "Harvest":
			addTextField("harvestPart", "Part to Harvest");
			addTextField("useFor", "Use For");
			break;
		}
	}

	private void addTextField(String id, String promptText) {
		Label label = new Label(promptText);
		TextField textField = new TextField();
		textField.setPromptText(promptText);
		textField.setPrefWidth(300);

		dynamicFieldsContainer.getChildren().addAll(label, textField);
		dynamicFields.put(id, textField);
	}

	@FXML
	private void handleSaveButton(ActionEvent event) {

		String selectedType = reminderTypeComboBox.getValue();
		String plantName = plantComboBox.getValue();
		LocalDate date = reminderDatePicker.getValue();
		if (selectedType == null || plantName == null || date == null) {
			showAlert("Missing Information", "Please fill all required fields.");
			return;
		}
		String formattedDate = date.format(DateTimeFormatter.ISO_LOCAL_DATE);
		boolean isRecurring = recurringCheckBox.isSelected();
		String interval = intervalComboBox.getValue();
		// Build details string from dynamic fields
		StringBuilder detailsBuilder = new StringBuilder();
		for (Map.Entry<String, Control> entry : dynamicFields.entrySet()) {
			TextField tf = (TextField) entry.getValue();
			detailsBuilder.append(entry.getKey()).append(": ").append(tf.getText()).append("; ");
		}
		String details = detailsBuilder.toString();
		String sql = "INSERT INTO Reminder (plantName, reminderType, date, details, recurring, intervals) VALUES (?, ?, ?, ?, ?, ?)";
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = dbHelper.getConnection();
			statement = connection.prepareStatement(sql);
			statement.setString(1, plantName);
			statement.setString(2, selectedType);
			statement.setString(3, formattedDate);
			statement.setString(4, details);
			statement.setBoolean(5, isRecurring);
			statement.setString(6, isRecurring ? interval : null);
			statement.executeUpdate();
			showAlert("Success", "Reminder saved to database!");
			clearForm();
		} catch (SQLException e) {
			showAlert("Database Error", "Could not save reminder: " + e.getMessage());
			e.printStackTrace();
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			dbHelper.closeConnection(connection);
		}

		switchScene(event, "/plantTracker/resources/ViewReminders.fxml", "View Reminder");

//		String selectedType = reminderTypeComboBox.getValue();
//		String plantName = plantComboBox.getValue();
//		LocalDate date = reminderDatePicker.getValue();
//
//		if (selectedType == null || plantName == null || date == null) {
//			showAlert("Missing Information", "Please fill all required fields.");
//			return;
//		}
//
//		// Format date for our reminder classes
//		String formattedDate = date.format(DateTimeFormatter.ISO_LOCAL_DATE);

		// Create appropriate reminder based on type
//		Reminder reminder = null;

//		try {
//			switch (selectedType) {
//			case "Water":
//				TextField amountField = (TextField) dynamicFields.get("amountInMl");
//				int amount = Integer.parseInt(amountField.getText());
//				reminder = new WaterReminder(plantName, formattedDate, amount);
//				break;
//
//			case "Fertilize":
//				TextField fertilizerField = (TextField) dynamicFields.get("fertilizerType");
//				TextField fertilizerAmountField = (TextField) dynamicFields.get("amount");
//				double fertAmount = Double.parseDouble(fertilizerAmountField.getText());
//				reminder = new FertilizeReminder(plantName, formattedDate, fertilizerField.getText(), fertAmount);
//				break;
//
//			case "Repot":
//				TextField potSizeField = (TextField) dynamicFields.get("newPotSize");
//				TextField soilTypeField = (TextField) dynamicFields.get("soilType");
//				reminder = new RepotReminder(plantName, formattedDate, potSizeField.getText(), soilTypeField.getText());
//				break;
//
//			case "Move":
//				TextField locationField = (TextField) dynamicFields.get("newLocation");
//				TextField reasonField = (TextField) dynamicFields.get("reason");
//				reminder = new MoveReminder(plantName, formattedDate, locationField.getText(), reasonField.getText());
//				break;
//
//			case "Harvest":
//				TextField partField = (TextField) dynamicFields.get("harvestPart");
//				TextField useField = (TextField) dynamicFields.get("useFor");
//				reminder = new HarvestReminder(plantName, formattedDate, partField.getText(), useField.getText());
//				break;
//			}
//
//			// Here we would save the reminder to a storage mechanism
//			if (reminder != null) {
//				// For demonstration purposes, just print the reminder
//				System.out.println("Created reminder: " + reminder);
//				showAlert("Success", "Reminder created: " + reminder.getDescription());
//
//				// Handle recurring logic if needed
//				if (recurringCheckBox.isSelected()) {
//					String interval = intervalComboBox.getValue();
//					System.out.println("Set as recurring: " + interval);
//					// Additional logic for recurring reminders would go here
//				}
//
//				// Clear form after successful save
//				clearForm();
//			}
//
//		} catch (
//
//		NumberFormatException e) {
//			showAlert("Invalid Input", "Please enter valid numeric values where required.");
//		} catch (Exception e) {
//			showAlert("Error", "An error occurred: " + e.getMessage());
//		}	

	}

	@FXML
	private void handleClearButton() {
		clearForm();
	}

	private void clearForm() {
		reminderTypeComboBox.getSelectionModel().clearSelection();
		plantComboBox.getSelectionModel().clearSelection();
		reminderDatePicker.setValue(LocalDate.now());
		dynamicFieldsContainer.getChildren().clear();
		dynamicFields.clear();
		recurringCheckBox.setSelected(false);
		intervalComboBox.getSelectionModel().clearSelection();
	}

	private void showAlert(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
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