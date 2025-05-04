package plantTracker.controller;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import plantTracker.database.ReminderDAO;
import plantTracker.model.FertilizeReminder;
import plantTracker.model.Reminder;
import plantTracker.model.WaterReminder;
import user.User;
import util.SessionManager;
import util.ShowAlert;

public class PlantTrackerController implements Initializable {

	@FXML
	private Parent root;

	@FXML
	private Button viewPlantsButton;

	@FXML
	private Button viewRemindersButton;

	@FXML
	private Button seeCompleteRemindersButton;

	@FXML
	private Button seeIncompleteRemindersButton;

	@FXML
	private MenuItem logout;

	@FXML
	private MenuItem updatePassword;

	@FXML
	private MenuItem editUserInfo;

	@FXML
	private MenuItem manageUsers;

	@FXML
	private ScrollPane upcomingRemindersScrollPane;

	@FXML
	private VBox upcomingRemindersVBox;

	private User loggedInUser = SessionManager.getCurrentUser();

	private ReminderDAO reminderDAO = new ReminderDAO();

	@Override
	public void initialize(URL url, ResourceBundle rb) {

		updateAdminButtonVisibility();
		loadUpcomingAndRecentIncompleteReminders();

		seeIncompleteRemindersButton.setVisible(false);
		seeIncompleteRemindersButton.setManaged(false);

		seeCompleteRemindersButton.setVisible(true);
		seeCompleteRemindersButton.setManaged(true);
	}

	private void loadUpcomingAndRecentIncompleteReminders() {
		if (loggedInUser != null) {
			try {
				LocalDate now = LocalDate.now(ZoneId.systemDefault());
				LocalDate nextWeek = now.plusDays(7);
				List<Reminder> upcoming = reminderDAO.getUpcomingIncompleteReminders(now, nextWeek);
				List<Reminder> recent = reminderDAO.getRecentIncompleteReminders(now, 5);
				List<Reminder> combined = new ArrayList<>();
				combined.addAll(upcoming);
				combined.addAll(recent);
				displayReminders(combined);
				displayReminders(upcoming);
			} catch (SQLException e) {
				ShowAlert.showAlert("Error", "Error loading upcoming reminders.");
				e.printStackTrace();
			}
		}
	}

	private void loadRecentCompleteReminders() {
		if (loggedInUser != null) {
			try {
				LocalDate now = LocalDate.now(ZoneId.systemDefault());
				List<Reminder> complete = reminderDAO.getRecentCompleteReminders(now, 5);
				displayReminders(complete);
			} catch (SQLException e) {
				ShowAlert.showAlert("Error", "Error loading upcoming reminders.");
				e.printStackTrace();
			}
		}
	}

	private void displayReminders(List<Reminder> reminders) {
		upcomingRemindersVBox.getChildren().clear();
		for (Reminder reminder : reminders) {
			HBox reminderRow = new HBox(10);
			Label reminderLabel = new Label(formatReminder(reminder));
			CheckBox completeCheckBox = new CheckBox("Complete");
			completeCheckBox.setSelected(reminder.isComplete()); // Set initial state

			completeCheckBox.setOnAction(event -> {
				if (completeCheckBox.isSelected()) {
					try {
						reminderDAO.markReminderComplete(reminder.getReminderId());
						reminderDAO.advanceRecurringReminders();
						ShowAlert.showAlert("Success!", "Reminder is marked complete");

						loadUpcomingAndRecentIncompleteReminders();
					} catch (SQLException e) {
						ShowAlert.showAlert("Error", "Error marking reminder as complete.");
						e.printStackTrace();
					}
				}
			});
			reminderRow.getChildren().addAll(reminderLabel, completeCheckBox);
			upcomingRemindersVBox.getChildren().add(reminderRow);
		}
	}

	private String formatReminder(Reminder reminder) {
		String formatted = String.format("%s - %s (%s)", reminder.getPlantName(), reminder.getReminderType(),
				reminder.getDate());
		if (reminder instanceof WaterReminder) {
			formatted += " - Water: " + ((WaterReminder) reminder).getAmountInMl() + "ml";
		} else if (reminder instanceof FertilizeReminder) {
			formatted += " - Fertilize: " + ((FertilizeReminder) reminder).getFertilizerType();
		}
		return formatted;
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

		seeIncompleteRemindersButton.setVisible(true);
		seeIncompleteRemindersButton.setManaged(true);

		seeCompleteRemindersButton.setVisible(false);
		seeCompleteRemindersButton.setManaged(false);
	}

	@FXML
	private void handleViewIncompleteReminders(ActionEvent event) {
		loadUpcomingAndRecentIncompleteReminders();

		seeIncompleteRemindersButton.setVisible(false);
		seeIncompleteRemindersButton.setManaged(false);

		seeCompleteRemindersButton.setVisible(true);
		seeCompleteRemindersButton.setManaged(true);
	}

	// Method to handle the "Manage Users" button click
	@FXML
	private void handleManageUsers(ActionEvent event) {
		// Load the ManageUsersController scene
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
		util.SceneSwitcher.switchScene(event, "/plantTracker/resources/ViewReminders.fxml", "View Reminders");
	}

}
