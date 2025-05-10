package user;

import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import util.ProjUtil;

/**
 * Reset Password Controller handles cases when the user forgets their password
 * It uses their email to fetch their reminder question from the database Then
 * check their reminder answer against the one stored (hashed) in the database
 * If the answer matches, it checks that the two password fields match Then
 * updates the password
 */
public class ResetPasswordController {

	@FXML
	private TextField email;

	@FXML
	private Button submit;

	@FXML
	private Label securityQuestion;

	@FXML
	private Label securityAnswerLabel;

	@FXML
	private TextField securityAnswer;

	@FXML
	private Label passwordLabel;

	@FXML
	private PasswordField password;

	@FXML
	private Label confirmPasswordLabel;

	@FXML
	private PasswordField confirmPassword;

	@FXML
	private Button save;

	@FXML
	private Button cancel;

	private UserDAO userDAO = new UserDAO();

	private User user = null;

	@FXML
	// Displays all the fields if the email exists
	// and gets the security question associated with the email from the database
	public void getSecurityQuestion(ActionEvent event) {
		String enteredEmail = email.getText();

		try {
			user = userDAO.getUser(enteredEmail);

			if (user != null) {

				securityQuestion.setVisible(true);
				securityQuestion.setManaged(true);

				securityAnswerLabel.setVisible(true);
				securityAnswerLabel.setManaged(true);

				securityAnswer.setVisible(true);
				securityAnswer.setManaged(true);

				passwordLabel.setVisible(true);
				passwordLabel.setManaged(true);

				password.setVisible(true);
				password.setManaged(true);

				confirmPasswordLabel.setVisible(true);
				confirmPasswordLabel.setManaged(true);

				confirmPassword.setVisible(true);
				confirmPassword.setManaged(true);

				save.setVisible(true);
				save.setManaged(true);

				securityQuestion.setText(user.getSecurityQuestion());

			} else {
				util.ShowAlert.showAlert("Invalid User Details", "Invalid email or password.");
			}

		} catch (SQLException e) {
			util.ShowAlert.showAlert("Databse Error", "Database error fetching user.");
			e.printStackTrace();
		}
	}

	@FXML
	// checks security answer against the database
	// and checks that the password fields match
	// then updates the user password in the database
	public void save(ActionEvent event) {

		String securityAnswerString = securityAnswer.getText();
		String passwordString = password.getText();
		String confirmPasswordString = confirmPassword.getText();

		if (user != null && user.getSecurityAnswer().equals(ProjUtil.getSHA(securityAnswerString))) {
			if (passwordString.equals(confirmPasswordString) && !passwordString.isEmpty()) {
				try {
					userDAO.updatePassword(user.getEmail(), passwordString);
					util.ShowAlert.showAlert("Password Reset",
							"Your password has been reset successfully. Please log in with your new password.");
					util.SceneSwitcher.switchScene(event, "/user/resources/Login.fxml", "Login");
				} catch (SQLException e) {
					util.ShowAlert.showAlert("Database Error", "Error updating password.");
					e.printStackTrace();
				}
			} else {
				util.ShowAlert.showAlert("Mismatch Passwords", "Make sure your password fields match");
			}
		} else {
			util.ShowAlert.showAlert("Invalid User Details", "Invalid Security Answer");
		}
	}

	@FXML
	public void cancel(ActionEvent event) {
		util.SceneSwitcher.switchScene(event, "/user/resources/Login.fxml", "Login");
	}

}