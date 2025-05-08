package plantTracker.controller;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import plantTracker.database.ReminderDAO;
import plantTracker.model.Reminder;
import user.User;
import util.SessionManager;
import util.ShowAlert;

public class PlantTrackerController implements Initializable {

	@FXML
	private Label sceneLabel;

	@FXML
	private Parent root;

	@FXML
	private Button viewPlantsButton;

	@FXML
	private Button viewRemindersButton;

	@FXML
	private Button viewCompleteRemindersButton;

	@FXML
	private Button viewIncompleteRemindersButton;

	@FXML
	private MenuItem logout;

	@FXML
	private MenuItem updatePassword;

	@FXML
	private MenuItem editUserInfo;

	@FXML
	private MenuItem manageUsers;

	@FXML
	private TableView<Reminder> incompleteRemindersTableView;

	@FXML
	private TableColumn<Reminder, String> incompletePlantNameColumn;

	@FXML
	private TableColumn<Reminder, String> incompleteTypeColumn;

	@FXML
	private TableColumn<Reminder, String> incompleteDescriptionColumn;

	@FXML
	private TableColumn<Reminder, String> incompleteDueDateColumn;

	@FXML
	private TableColumn<Reminder, Boolean> incompleteCompletedColumn;

	@FXML
	private TableView<Reminder> completeRemindersTableView;

	@FXML
	private TableColumn<Reminder, String> completePlantNameColumn;

	@FXML
	private TableColumn<Reminder, String> completeTypeColumn;

	@FXML
	private TableColumn<Reminder, String> completeDescriptionColumn;

	@FXML
	private TableColumn<Reminder, String> completeDueDateColumn;

	private User loggedInUser = SessionManager.getCurrentUser();

	@FXML
	private Pane bottomBar;

	private ReminderDAO reminderDAO = new ReminderDAO();
	private ObservableList<Reminder> incompleteRemindersData = FXCollections.observableArrayList();
	private ObservableList<Reminder> completeRemindersData = FXCollections.observableArrayList();

	@Override
	public void initialize(URL url, ResourceBundle rb) {

//		bottomBar.prefHeightProperty().bind(sceneLabel.heightProperty());

		updateAdminButtonVisibility();
		loadUpcomingAndRecentIncompleteReminders();

		viewIncompleteRemindersButton.setVisible(false);
		viewIncompleteRemindersButton.setManaged(false);

		viewCompleteRemindersButton.setVisible(true);
		viewCompleteRemindersButton.setManaged(true);

		incompleteRemindersTableView.setVisible(true);
		incompleteRemindersTableView.setManaged(true);
		completeRemindersTableView.setVisible(false);
		completeRemindersTableView.setManaged(false);

		incompleteRemindersTableView.setItems(incompleteRemindersData);
		completeRemindersTableView.setItems(completeRemindersData);

		setupIncompleteRemindersTableView();
		setupCompleteRemindersTableView();

		// Initially set VBox.vgrow for the incomplete table
		VBox.setVgrow(incompleteRemindersTableView, Priority.ALWAYS);
		VBox.setVgrow(completeRemindersTableView, Priority.NEVER);
	}

	private void setupIncompleteRemindersTableView() {
		incompletePlantNameColumn
				.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPlantName()));
		incompleteTypeColumn
				.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getReminderType()));
		incompleteDescriptionColumn
				.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));
		incompleteDueDateColumn.setCellValueFactory(cellData -> {
			if (cellData.getValue().getCurrentDueDate() != null) {
				LocalDate date = cellData.getValue().getCurrentDueDate();
				DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("EEEE, MMM dd");
				return new SimpleStringProperty(" " + date.format(dateFormatter));
			}
			return new SimpleStringProperty("");
		});

		incompleteDueDateColumn.setCellFactory(column -> new TableCell<Reminder, String>() {
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				setText(item);
				// Styling is handled in the RowFactory now
			}
		});

		incompleteCompletedColumn
				.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().isComplete()));

		incompleteCompletedColumn.setCellFactory(column -> new TableCell<Reminder, Boolean>() {
			private final CheckBox checkBox = new CheckBox();

			{
				checkBox.setOnAction(event -> {
					Reminder reminder = getTableView().getItems().get(getIndex());
					boolean isSelected = checkBox.isSelected();

					LocalDate now = LocalDate.now(ZoneId.systemDefault());
					LocalDate reminderDueDate = reminder.getCurrentDueDate();

					if (isSelected) {
						boolean isEarlyCompletion = reminderDueDate != null && reminderDueDate.isAfter(now);
						boolean confirmed = true;

						if (isEarlyCompletion) {
							confirmed = ShowAlert.showConfirmationAlert("Confirm Early Completion",
									"Are you sure you want to mark this reminder as complete before its due date?");
						}

						if (confirmed) {
							try {
								reminderDAO.markReminderComplete(reminder.getReminderId());
								if (reminder.isRecurring()) {
									reminderDAO.advanceRecurringReminder(reminder.getReminderId());
								}
								ShowAlert.showAlert("Success!", "Reminder marked complete.");
								loadUpcomingAndRecentIncompleteReminders();
								loadRecentCompleteReminders();
							} catch (SQLException e) {
								ShowAlert.showAlert("Error", "Error marking reminder as complete.");
								e.printStackTrace();
								checkBox.setSelected(false);
							}
						} else {
							checkBox.setSelected(false);
						}
					}
				});
			}

			@Override
			protected void updateItem(Boolean item, boolean empty) {
				super.updateItem(item, empty);
				if (empty) {
					setGraphic(null);
				} else {
					checkBox.setSelected(item);
					setGraphic(checkBox);
				}
			}
		});

		// Set the row factory to apply styling to the entire row
		incompleteRemindersTableView.setRowFactory(tv -> new javafx.scene.control.TableRow<Reminder>() {
			@Override
			protected void updateItem(Reminder item, boolean empty) {
				super.updateItem(item, empty);
				if (item == null || empty) {
					setStyle(""); // Clear style for empty rows
				} else {
					LocalDate now = LocalDate.now(ZoneId.systemDefault());
					LocalDate reminderDueDate = item.getCurrentDueDate();
					if (!item.isComplete() && reminderDueDate != null && reminderDueDate.isBefore(now)) {
						setStyle("-fx-background-color: #F08080; -fx-text-fill: white;");
					} else {
						setStyle(""); // Clear style for non-past-due or complete reminders
					}
				}
			}
		});
	}

	private void setupCompleteRemindersTableView() {
		completePlantNameColumn
				.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPlantName()));
		completeTypeColumn
				.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getReminderType()));
		completeDescriptionColumn
				.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));
		completeDueDateColumn.setCellValueFactory(cellData -> {
			if (cellData.getValue().getCurrentDueDate() != null) {
				LocalDate date = cellData.getValue().getCurrentDueDate();
				DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("EEEE, MMM dd");
				return new SimpleStringProperty(" " + date.format(dateFormatter));
			}
			return new SimpleStringProperty("");
		});
	}

	private void loadUpcomingAndRecentIncompleteReminders() {
		if (loggedInUser != null) {
			try {
				LocalDate now = LocalDate.now(ZoneId.systemDefault());
				LocalDate nextWeek = now.plusDays(7);

				List<Reminder> upcoming = reminderDAO.getUpcomingIncompleteReminders(now, nextWeek);

				List<Reminder> recentPastDue = reminderDAO.getRecentIncompleteReminders(now, 10);

				List<Reminder> combined = new ArrayList<>();
				combined.addAll(upcoming);
				// Add only those recent past-due reminders that are NOT in the upcoming list
				for (Reminder recent : recentPastDue) {
					boolean isDuplicate = false;
					for (Reminder upcomingItem : upcoming) {
						if (recent.getReminderId() == upcomingItem.getReminderId()) {
							isDuplicate = true;
							break;
						}
					}
					if (!isDuplicate) {
						combined.add(recent);
					}
				}

				incompleteRemindersData.clear();
				incompleteRemindersData.addAll(combined);

			} catch (SQLException e) {
				ShowAlert.showAlert("Error", "Error loading incomplete reminders.");
				e.printStackTrace();
			}
		}
	}

	private void loadRecentCompleteReminders() {
		if (loggedInUser != null) {
			try {
				LocalDate fiveDaysAgo = LocalDate.now(ZoneId.systemDefault()).minusDays(5);
				List<Reminder> complete = reminderDAO.getRecentCompleteReminders(fiveDaysAgo, 5);
				completeRemindersData.clear();
				completeRemindersData.addAll(complete);
			} catch (SQLException e) {
				ShowAlert.showAlert("Error", "Error loading complete reminders.");
				e.printStackTrace();
			}
		}
	}

	private void updateAdminButtonVisibility() {
		if (loggedInUser != null && loggedInUser.isAdmin()) {
			manageUsers.setVisible(true);
		} else {
			manageUsers.setVisible(false);
		}
	}

	@FXML
	private void handleViewCompleteReminders(ActionEvent event) {
		loadRecentCompleteReminders();
		incompleteRemindersTableView.setVisible(false);
		incompleteRemindersTableView.setManaged(false);
		completeRemindersTableView.setVisible(true);
		completeRemindersTableView.setManaged(true);
		viewIncompleteRemindersButton.setVisible(true);
		viewIncompleteRemindersButton.setManaged(true);
		viewCompleteRemindersButton.setVisible(false);
		viewCompleteRemindersButton.setManaged(false);

		VBox.setVgrow(incompleteRemindersTableView, Priority.NEVER);
		VBox.setVgrow(completeRemindersTableView, Priority.ALWAYS);
	}

	@FXML
	private void handleViewIncompleteReminders(ActionEvent event) {
		loadUpcomingAndRecentIncompleteReminders();
		incompleteRemindersTableView.setVisible(true);
		incompleteRemindersTableView.setManaged(true);
		completeRemindersTableView.setVisible(false);
		completeRemindersTableView.setManaged(false);
		viewIncompleteRemindersButton.setVisible(false);
		viewIncompleteRemindersButton.setManaged(false);
		viewCompleteRemindersButton.setVisible(true);
		viewCompleteRemindersButton.setManaged(true);

		VBox.setVgrow(incompleteRemindersTableView, Priority.ALWAYS);
		VBox.setVgrow(completeRemindersTableView, Priority.NEVER);
	}

	@FXML
	private void handleManageUsers(ActionEvent event) {
		if (loggedInUser != null && loggedInUser.isAdmin()) {
			util.SceneSwitcher.switchScene(event, "/user/resources/ManageUsers.fxml", "Manage Users", root);
		} else {
			util.ShowAlert.showAlert("Access Denied", "You do not have administrator privileges.");
		}
	}

	@FXML
	public void handleUpdatePassword(ActionEvent event) {
		util.SceneSwitcher.switchScene(event, "/user/resources/UpdatePassword.fxml", "Update Password", root);
	}

	@FXML
	public void handleEditUserInfo(ActionEvent event) {
		util.SceneSwitcher.switchScene(event, "/user/resources/EditUser.fxml", "Edit User", root);
	}

	@FXML
	public void handleLogout(ActionEvent event) {
		SessionManager.logoutUser();
		util.SceneSwitcher.switchScene(event, "/user/resources/Login.fxml", "Login", root);
	}

	@FXML
	public void handleViewPlants(ActionEvent event) {
		util.SceneSwitcher.switchScene(event, "/plantTracker/resources/ViewPlants.fxml", "View Plants");
	}

	@FXML
	public void handleViewReminders(ActionEvent event) {
		util.SceneSwitcher.switchScene(event, "/plantTracker/resources/ManageReminders.fxml", "Manage Reminders");
	}
}