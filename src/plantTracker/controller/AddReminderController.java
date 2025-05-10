package plantTracker.controller;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import plantTracker.database.PlantDAO;
import plantTracker.database.ReminderDAO;
import plantTracker.model.FertilizeReminder;
import plantTracker.model.HarvestReminder;
import plantTracker.model.MoveReminder;
import plantTracker.model.Reminder;
import plantTracker.model.RepotReminder;
import plantTracker.model.WaterReminder;
import util.IntegerTextField;
import util.SceneSwitcher;
import util.ShowAlert;

/**
 * Add Reminder Controller creates reminder objects and adds them to the
 * database
 */
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
	private ComboBox<Integer> intervalComboBox;

	@FXML
	private Button saveButton;

	@FXML
	private Button clearButton;

	@FXML
	private Button backButton;

	// Fields for Water
	@FXML
	private Label waterAmountLabel;
	@FXML
	private IntegerTextField waterAmountTextField;

	// Fields for Fertilize
	@FXML
	private Label fertilizerTypeLabel;
	@FXML
	private TextField fertilizerTypeTextField;
	@FXML
	private Label fertilizerAmountLabel;
	@FXML
	private IntegerTextField fertilizerAmountTextField;

	// Fields for Repot
	@FXML
	private Label newPotSizeLabel;
	@FXML
	private TextField newPotSizeTextField;
	@FXML
	private Label soilTypeLabel;
	@FXML
	private TextField soilTypeTextField;

	// Fields for Move
	@FXML
	private Label newLocationLabel;
	@FXML
	private TextField newLocationTextField;
	@FXML
	private Label moveReasonLabel;
	@FXML
	private TextField moveReasonTextField;

	// Fields for Harvest
	@FXML
	private Label harvestPartLabel;
	@FXML
	private TextField harvestPartTextField;
	@FXML
	private Label harvestUseForLabel;
	@FXML
	private TextField harvestUseForTextField;

	// Store dynamic fields for later access
	private Map<String, Control> dynamicFields = new HashMap<>();

	private PlantDAO plantDAO = new PlantDAO();
	private ReminderDAO reminderDAO = new ReminderDAO();
	private ObservableList<String> data = FXCollections.observableArrayList();

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		// Fetches plants from database for list
		try {
			plantDAO.populateList(data);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// Initialize reminder types
		reminderTypeComboBox
				.setItems(FXCollections.observableArrayList("Water", "Fertilize", "Repot", "Move", "Harvest"));

		// Initialize plant options
		plantComboBox.setItems(FXCollections.observableArrayList(data));

		// Initialize interval options
		intervalComboBox.setItems(FXCollections.observableArrayList(1, 2, 3, 4, 5, 6, 7, 14, 28));

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
		// Make all dynamic fields initially invisible and unmanaged
		waterAmountLabel.setVisible(false);
		waterAmountLabel.setManaged(false);
		waterAmountTextField.setVisible(false);
		waterAmountTextField.setManaged(false);
		fertilizerTypeLabel.setVisible(false);
		fertilizerTypeLabel.setManaged(false);
		fertilizerTypeTextField.setVisible(false);
		fertilizerAmountLabel.setVisible(false);
		fertilizerAmountLabel.setManaged(false);
		fertilizerAmountTextField.setVisible(false);
		fertilizerAmountTextField.setManaged(false);
		newPotSizeLabel.setVisible(false);
		newPotSizeLabel.setManaged(false);
		newPotSizeTextField.setVisible(false);
		newPotSizeTextField.setManaged(false);
		soilTypeLabel.setVisible(false);
		soilTypeLabel.setManaged(false);
		soilTypeTextField.setVisible(false);
		soilTypeTextField.setManaged(false);
		newLocationLabel.setVisible(false);
		newLocationLabel.setManaged(false);
		newLocationTextField.setVisible(false);
		newLocationTextField.setManaged(false);
		moveReasonLabel.setVisible(false);
		moveReasonLabel.setManaged(false);
		moveReasonTextField.setVisible(false);
		moveReasonTextField.setManaged(false);
		harvestPartLabel.setVisible(false);
		harvestPartLabel.setManaged(false);
		harvestPartTextField.setVisible(false);
		harvestPartTextField.setManaged(false);
		harvestUseForLabel.setVisible(false);
		harvestUseForLabel.setManaged(false);
		harvestUseForTextField.setVisible(false);
		harvestUseForTextField.setManaged(false);

		// Make the relevant fields visible and managed based on reminder type
		switch (reminderType) {
		case "Water":
			waterAmountLabel.setVisible(true);
			waterAmountLabel.setManaged(true);
			waterAmountTextField.setVisible(true);
			waterAmountTextField.setManaged(true);
			break;

		case "Fertilize":
			fertilizerTypeLabel.setVisible(true);
			fertilizerTypeLabel.setManaged(true);
			fertilizerTypeTextField.setVisible(true);
			fertilizerTypeTextField.setManaged(true);
			fertilizerAmountLabel.setVisible(true);
			fertilizerAmountLabel.setManaged(true);
			fertilizerAmountTextField.setVisible(true);
			fertilizerAmountTextField.setManaged(true);
			break;

		case "Repot":
			newPotSizeLabel.setVisible(true);
			newPotSizeLabel.setManaged(true);
			newPotSizeTextField.setVisible(true);
			newPotSizeTextField.setManaged(true);
			soilTypeLabel.setVisible(true);
			soilTypeLabel.setManaged(true);
			soilTypeTextField.setVisible(true);
			soilTypeTextField.setManaged(true);
			break;

		case "Move":
			newLocationLabel.setVisible(true);
			newLocationLabel.setManaged(true);
			newLocationTextField.setVisible(true);
			newLocationTextField.setManaged(true);
			moveReasonLabel.setVisible(true);
			moveReasonLabel.setManaged(true);
			moveReasonTextField.setVisible(true);
			moveReasonTextField.setManaged(true);
			break;

		case "Harvest":
			harvestPartLabel.setVisible(true);
			harvestPartLabel.setManaged(true);
			harvestPartTextField.setVisible(true);
			harvestPartTextField.setManaged(true);
			harvestUseForLabel.setVisible(true);
			harvestUseForLabel.setManaged(true);
			harvestUseForTextField.setVisible(true);
			harvestUseForTextField.setManaged(true);
			break;
		}

		// Request stage to resize after fields are shown/hidden
		Stage stage = (Stage) reminderTypeComboBox.getScene().getWindow();
		if (stage != null) {
			stage.sizeToScene();
		}
	}

	@FXML
	// creates a reminder object and adds it to the database if all fields are
	// filled out
	private void handleSaveButton(ActionEvent event) {

		Reminder newReminder = null;

		String selectedType = reminderTypeComboBox.getValue();
		String plantName = plantComboBox.getValue();
		LocalDate localDate = reminderDatePicker.getValue();
		Integer interval;

		if (selectedType == null || plantName == null || localDate == null) {
			util.ShowAlert.showAlert("Missing Information", "Please fill all required fields.");
			return;
		}
		boolean isRecurring = recurringCheckBox.isSelected();
		if (isRecurring) {
			interval = intervalComboBox.getValue();
		} else {
			interval = 0;
		}

		if (selectedType != null) {
			switch (selectedType) {
			case "Water":
				Integer amountInMl = waterAmountTextField.getValue();
				newReminder = new WaterReminder(plantName, localDate, isRecurring, interval, amountInMl);
				break;
			case "Fertilize":
				String fertilizerType = fertilizerTypeTextField.getText();
				Integer fertilizerAmount = fertilizerAmountTextField.getValue();
				newReminder = new FertilizeReminder(plantName, localDate, isRecurring, interval, fertilizerType,
						fertilizerAmount);
				break;
			case "Repot":
				String newPotSize = newPotSizeTextField.getText();
				String soilType = soilTypeTextField.getText();
				newReminder = new RepotReminder(plantName, localDate, isRecurring, interval, newPotSize, soilType);
				break;
			case "Move":
				String newLocation = newLocationTextField.getText();
				String reason = moveReasonTextField.getText();
				newReminder = new MoveReminder(plantName, localDate, isRecurring, interval, newLocation, reason);
				break;
			case "Harvest":
				String harvestPart = harvestPartTextField.getText();
				String useFor = harvestUseForTextField.getText();
				newReminder = new HarvestReminder(plantName, localDate, isRecurring, interval, harvestPart, useFor);
				break;
			}

			newReminder.setCurrentDueDate(localDate);

			if (isRecurring && interval != null) {
				newReminder.setNextDueDate(localDate.plusDays(interval));
			} else if (isRecurring && interval == null) {
				util.ShowAlert.showAlert("Missing Information", "Please fill in the interval or set non-recurring.");
				return;
			}
			try {
				int generatedId = reminderDAO.addReminder(newReminder); // Get the generated ID
				newReminder.setReminderId(generatedId); // Set the ID on the new Reminder object
				ShowAlert.showAlert("Success", "Reminder added successfully!");
				SceneSwitcher.switchScene(event, "/plantTracker/resources/ManageReminders.fxml", "Manage Reminder");
			} catch (SQLException e) {
				ShowAlert.showAlert("Error", "Error adding reminder to the database: " + e.getMessage());
				e.printStackTrace();
			}
		}
	}

	@FXML
	private void handleClearButton() {
		clearForm();
	}

	@FXML
	private void handleBackButton(ActionEvent event) {
		SceneSwitcher.switchScene(event, "/plantTracker/resources/ManageReminders.fxml", "Manage Reminder");
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

}