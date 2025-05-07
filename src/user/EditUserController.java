package user;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import util.ProjUtil;
import util.SceneSwitcher;
import util.SessionManager;
import util.ShowAlert;

public class EditUserController implements Initializable {

	private String[] securityQuestions = { "What was your imaginary friend's name?", "What's your favorite candy?",
			"What was your first pet's name?", "What was your first best friend's name?" };

	@FXML
	private TextField firstName;

	@FXML
	private TextField lastName;

	@FXML
	private ComboBox<String> securityQuestion;

	@FXML
	private TextField securityAnswer;

	@FXML
	private Button update;

	@FXML
	private Button cancel;

	private UserDAO userDAO = new UserDAO();
	private User loggedInUser;

	public void register(ActionEvent event) throws IOException {

	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		loggedInUser = SessionManager.getCurrentUser();

		if (loggedInUser != null) {
			firstName.setText(loggedInUser.getFirstName());
			lastName.setText(loggedInUser.getLastName());
			securityQuestion.setItems(FXCollections.observableArrayList(securityQuestions));
			securityQuestion.setValue(loggedInUser.getSecurityQuestion()); // Set current value
		} else {
			// Handle case where no user is logged in (shouldn't happen)
			System.err.println("Error: No user logged in on Edit User Info page.");
			// Optionally navigate back to login
			ActionEvent event = null;
			logout(event);
		}

	}

	@FXML
	private void handleSave(ActionEvent event) {
		if (loggedInUser != null) {
			String newFirstName = firstName.getText();
			String newLastName = lastName.getText();
			String newSecurityQuestion = securityQuestion.getValue();
			String newSecurityAnswer = securityAnswer.getText();

			if (!newFirstName.isEmpty() && !newLastName.isEmpty() && newSecurityQuestion != null
					&& !newSecurityAnswer.isEmpty()) {
				try {
					String hashedSecurityAnswer = ProjUtil.getSHA(newSecurityAnswer);
					userDAO.updateUserInfo(loggedInUser.getEmail(), newFirstName, newLastName, newSecurityQuestion,
							hashedSecurityAnswer);
					loggedInUser.setFirstName(newFirstName);
					loggedInUser.setLastName(newLastName);
					loggedInUser.setSecurityQuestion(newSecurityQuestion);
					loggedInUser.setSecurityAnswer(hashedSecurityAnswer); // Update in SessionManager?
					ShowAlert.showAlert("Success", "User information updated successfully.");
					SceneSwitcher.switchScene(event, "/plantTracker/resources/PlantTracker.fxml", "Sprout Home");
				} catch (SQLException e) {
					ShowAlert.showAlert("Error", "Database error updating user info.");
					e.printStackTrace();
				}
			} else {
				ShowAlert.showAlert("Warning", "Please fill in all fields.");
			}
		}
	}

	@FXML
	private void handleCancel(ActionEvent event) {
		SceneSwitcher.switchScene(event, "/plantTracker/resources/PlantTracker.fxml", "Sprout Home");
	}

	@FXML
	private void logout(ActionEvent event) {
		SceneSwitcher.switchScene(event, "/user/resources/Login.fxml", "Login");
	}

}
