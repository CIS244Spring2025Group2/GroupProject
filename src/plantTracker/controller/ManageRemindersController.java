package plantTracker.controller;

import java.net.URL;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import plantTracker.database.ReminderDAO;
import plantTracker.model.FertilizeReminder;
import plantTracker.model.HarvestReminder;
import plantTracker.model.MoveReminder;
import plantTracker.model.Reminder;
import plantTracker.model.RepotReminder;
import plantTracker.model.WaterReminder;
import util.IntegerTextField;
import util.ShowAlert;

public class ManageRemindersController implements Initializable {

	@FXML
	private Label sceneLabel;

	@FXML
	private Label plantNameLabel;

	@FXML
	private Label reminderTypeLabel;

	@FXML
	private Button saveButton;

	@FXML
	private Button cancelButton;

	// table fields;
	@FXML
	private TableView<Reminder> reminderTableView;
	@FXML
	private TableColumn<Reminder, String> reminderDayDate;
	@FXML
	private TableColumn<Reminder, String> reminderPlantNameColumn;
	@FXML
	private TableColumn<Reminder, String> reminderDescriptionColumn;
	@FXML
	private TableColumn<Reminder, String> reminderCompletedColumn;
	@FXML
	private TableColumn<Reminder, Boolean> reminderRepeatingColumn;
	@FXML
	private TableColumn<Reminder, Integer> reminderIntervalColumn;

	// General fields

	@FXML
	private HBox editBox;

	@FXML
	private DatePicker reminderDatePicker;

	@FXML
	private CheckBox recurringCheckBox;

	@FXML
	private ComboBox<Integer> intervalComboBox;

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
	private Button editReminder;

	@FXML
	private Button deleteReminder;

	@FXML
	private Button addReminder;

	private Reminder selectedReminder;

	private ReminderDAO reminderDAO = new ReminderDAO();
	private ObservableList<Reminder> data = FXCollections.observableArrayList();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		bottomBar.prefHeightProperty().bind(sceneLabel.heightProperty());

		try {
			data.addAll(reminderDAO.getAllReminders());
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
						|| String.valueOf(reminder.getCurrentDueDate()).contains(searchText)
						|| reminder.getDescription().toLowerCase().contains(searchText);
			});
		});

		// **Configure the TableView**
		reminderTableView.setItems(filteredData); // Set the filtered data to the TableView

		reminderDayDate.setCellValueFactory(cellData -> {
			if (cellData.getValue().getCurrentDueDate() != null) {
				LocalDate date = cellData.getValue().getCurrentDueDate();
				DayOfWeek dayOfWeek = date.getDayOfWeek();
				DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("EEEE, MMM dd");
				return new SimpleStringProperty(" " + date.format(dateFormatter));
			}
			return new SimpleStringProperty("");
		});

		reminderPlantNameColumn
				.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPlantName()));

		reminderDescriptionColumn
				.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));

		reminderCompletedColumn.setCellValueFactory(
				cellData -> new SimpleStringProperty(cellData.getValue().isComplete() ? "Complete" : "Not Complete"));

		// Configure "Repeating" column
		reminderRepeatingColumn
				.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().isRecurring()));

		// Configure "Interval" column
		reminderIntervalColumn.setCellValueFactory(cellData -> {
			Integer interval = cellData.getValue().getIntervals();
			return new SimpleIntegerProperty(interval).asObject();
		});

		reminderCompletedColumn.setCellValueFactory(
				cellData -> new SimpleStringProperty(cellData.getValue().isComplete() ? "Complete" : "Not Complete"));

		// Prints which reminder has been selected in list
		reminderTableView.setOnMouseClicked(e -> {
			selectedReminder = reminderTableView.getSelectionModel().getSelectedItem();
			if (selectedReminder != null) {
				ReminderDAO.setSelectedReminder(selectedReminder);
				System.out.println("Selected Reminder: " + ReminderDAO.getSelectedReminder().getPlantName() + " - "
						+ ReminderDAO.getSelectedReminder().getReminderType());
			}
		});

		// Stops search-bar from automatically being highlighted when scene is switched
		searchBar.setFocusTraversable(false);

		// Initialize interval options
		intervalComboBox.setItems(FXCollections.observableArrayList(1, 2, 3, 4, 5, 6, 7, 14, 28));
	}

	@FXML
	private void handleDeleteReminder(ActionEvent event) {
		selectedReminder = ReminderDAO.getSelectedReminder();

		if (selectedReminder != null) {

			boolean confirmed = true;
			confirmed = ShowAlert.showConfirmationAlert("Confirm Deletion",
					"Are you sure you want to delete this reminder?");
			if (confirmed) {
				try {
					reminderDAO.deleteReminder(selectedReminder.getReminderId());
					data.remove(selectedReminder); // Remove the deleted item from the ObservableList
					ReminderDAO.clearSelectedReminder(); // Clear the selected reminder after deletion
					ShowAlert.showAlert("Success", "Reminder deleted successfully.");
				} catch (SQLException e) {
					ShowAlert.showAlert("Error", "Error deleting reminder.");
					e.printStackTrace();
				}
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
	private void handleEditReminder(ActionEvent event) {

		editBox.setVisible(true);
		editBox.setManaged(true);

		selectedReminder = ReminderDAO.getSelectedReminder();

		System.out.println("Selected Reminder: " + ReminderDAO.getSelectedReminder().getPlantName() + " - "
				+ ReminderDAO.getSelectedReminder().getReminderType());

		try {
			if (selectedReminder == null) {
				throw new IllegalStateException();
			}

			updateDynamicFields(selectedReminder);

			// set fields to match current reminder
			plantNameLabel.setText(selectedReminder.getPlantName());
			reminderTypeLabel.setText(selectedReminder.getReminderType());
			reminderDatePicker.setValue(selectedReminder.getCurrentDueDate());
			recurringCheckBox.setSelected(selectedReminder.isRecurring());
			if (selectedReminder.isRecurring()) {
				intervalComboBox.setValue(selectedReminder.getIntervals());
			} else {
				intervalComboBox.setValue(0);
			}

			String selectedType = selectedReminder.getReminderType();

			if (selectedType != null) {
				switch (selectedType) {
				case "Water Reminder":
					if (selectedReminder instanceof WaterReminder) {
						waterAmountTextField.setValue(((WaterReminder) selectedReminder).getAmountInMl());
					}
					break;
				case "Fertilize Reminder":
					if (selectedReminder instanceof FertilizeReminder) {
						fertilizerTypeTextField.setText(((FertilizeReminder) selectedReminder).getFertilizerType());
						fertilizerAmountTextField.setValue(((FertilizeReminder) selectedReminder).getAmount());
					}
					break;
				case "Repot Reminder":
					if (selectedReminder instanceof RepotReminder) {
						newPotSizeTextField.setText(((RepotReminder) selectedReminder).getNewPotSize());
						soilTypeTextField.setText(((RepotReminder) selectedReminder).getSoilType());
					}
					break;
				case "Move Reminder":
					if (selectedReminder instanceof MoveReminder) {
						newLocationTextField.setText(((MoveReminder) selectedReminder).getNewLocation());
						moveReasonTextField.setText(((MoveReminder) selectedReminder).getReason());
					}
					break;

				case "Harvest Reminder":
					if (selectedReminder instanceof HarvestReminder) {
						harvestPartTextField.setText(((HarvestReminder) selectedReminder).getHarvestPart());
						harvestUseForTextField.setText(((HarvestReminder) selectedReminder).getUseFor());
					}
					break;
				}
			}

		} catch (IllegalStateException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setHeaderText(null);
			alert.setContentText("A reminder has not been selected");
			alert.show();
		}

	}

	@FXML
	void saveEdit() {

		selectedReminder = ReminderDAO.getSelectedReminder();

		System.out.println("Selected Reminder: " + ReminderDAO.getSelectedReminder().getPlantName() + " - "
				+ ReminderDAO.getSelectedReminder().getReminderType());

		Reminder newReminder = null;

		String selectedType = selectedReminder.getReminderType();
		System.out.println(selectedReminder.getReminderType());
		String plantName = selectedReminder.getPlantName();
		LocalDate localDate = reminderDatePicker.getValue();
		Integer interval;

		if (localDate == null) {
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
			case "Water Reminder":
				Integer amountInMl = waterAmountTextField.getValue();
				newReminder = new WaterReminder(plantName, localDate, isRecurring, interval, amountInMl);
				break;
			case "Fertilize Reminder":
				String fertilizerType = fertilizerTypeTextField.getText();
				Integer fertilizerAmount = fertilizerAmountTextField.getValue();
				newReminder = new FertilizeReminder(plantName, localDate, isRecurring, interval, fertilizerType,
						fertilizerAmount);
				break;
			case "Repot Reminder":
				String newPotSize = newPotSizeTextField.getText();
				String soilType = soilTypeTextField.getText();
				newReminder = new RepotReminder(plantName, localDate, isRecurring, interval, newPotSize, soilType);
				break;
			case "Move Reminder":
				String newLocation = newLocationTextField.getText();
				String reason = moveReasonTextField.getText();
				newReminder = new MoveReminder(plantName, localDate, isRecurring, interval, newLocation, reason);
				break;
			case "Harvest Reminder":
				String harvestPart = harvestPartTextField.getText();
				String useFor = harvestUseForTextField.getText();
				newReminder = new HarvestReminder(plantName, localDate, isRecurring, interval, harvestPart, useFor);
				break;
			}

			if (newReminder != null) { // Ensure newReminder is not null before proceeding
				newReminder.setCurrentDueDate(localDate);

				if (isRecurring && interval != null) {
					newReminder.setNextDueDate(localDate.plusDays(interval));
				} else if (isRecurring && interval == null) {
					util.ShowAlert.showAlert("Missing Information",
							"Please fill in the interval or set non-recurring.");
					return;
				}

				try {
					reminderDAO.updateReminder(newReminder, selectedReminder.getReminderId()); // Get the generated ID
					ShowAlert.showAlert("Success", "Reminder updated successfully!");

					editBox.setVisible(false);
					editBox.setManaged(false);

				} catch (SQLException e) {
					ShowAlert.showAlert("Error", "Error updating reminder in the database: " + e.getMessage());
					e.printStackTrace();
				}
			} else {
				ShowAlert.showAlert("Error", "Reminder is null.");
			}
		}

	}

	@FXML
	void closeEdit() {
		editBox.setVisible(false);
		editBox.setManaged(false);
	}

	private void updateDynamicFields(Reminder reminder) {
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
		switch (reminder.getReminderType()) { // Switch on the reminder type of the passed Reminder object
		case "Water Reminder":
			waterAmountLabel.setVisible(true);
			waterAmountLabel.setManaged(true);
			waterAmountTextField.setVisible(true);
			waterAmountTextField.setManaged(true);
			break;

		case "Fertilize Reminder":
			fertilizerTypeLabel.setVisible(true);
			fertilizerTypeLabel.setManaged(true);
			fertilizerTypeTextField.setVisible(true);
			fertilizerTypeTextField.setManaged(true);
			fertilizerAmountLabel.setVisible(true);
			fertilizerAmountLabel.setManaged(true);
			fertilizerAmountTextField.setVisible(true);
			fertilizerAmountTextField.setManaged(true);
			break;

		case "Repot Reminder":
			newPotSizeLabel.setVisible(true);
			newPotSizeLabel.setManaged(true);
			newPotSizeTextField.setVisible(true);
			newPotSizeTextField.setManaged(true);
			soilTypeLabel.setVisible(true);
			soilTypeLabel.setManaged(true);
			soilTypeTextField.setVisible(true);
			soilTypeTextField.setManaged(true);
			break;

		case "Move Reminder":
			newLocationLabel.setVisible(true);
			newLocationLabel.setManaged(true);
			newLocationTextField.setVisible(true);
			newLocationTextField.setManaged(true);
			moveReasonLabel.setVisible(true);
			moveReasonLabel.setManaged(true);
			moveReasonTextField.setVisible(true);
			moveReasonTextField.setManaged(true);
			break;

		case "Harvest Reminder":
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
	}
}
