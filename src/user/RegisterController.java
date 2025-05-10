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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * Register controller creates a new user by creating a user object and then
 * calling UserDAO.addUser(User user)
 */
public class RegisterController implements Initializable {

	private String[] securityQuestions = { "What was your imaginary friend's name?", "What's your favorite candy?",
			"What was your first pet's name?", "What was your first best friend's name?" };

	@FXML
	private TextField email;

	@FXML
	private TextField firstName;

	@FXML
	private TextField lastName;

	@FXML
	private ComboBox<String> securityQuestion;

	@FXML
	private TextField securityAnswer;

	@FXML
	private PasswordField password;

	@FXML
	private PasswordField confirmPassword;

	@FXML
	private Button register;

	@FXML
	private Button cancel;

	public void register(ActionEvent event) throws IOException {

		String emailString = email.getText();
		String fistNameString = firstName.getText();
		String lastNameString = lastName.getText();
		String securityQuestionString = securityQuestion.getValue();
		String securityAnswerString = securityAnswer.getText();
		String passwordString = password.getText();
		String confirmPasswordString = confirmPassword.getText();

		User newUser = null;

		if (!emailString.isEmpty() && !fistNameString.isEmpty() && !lastNameString.isEmpty()
				&& securityQuestionString != null && !securityAnswerString.isEmpty() && !passwordString.isEmpty()) {
			if (passwordString.equals(confirmPasswordString)) {
				try {
					UserDAO userDAO = new UserDAO();
					newUser = new User(emailString, fistNameString, lastNameString, securityQuestionString,
							securityAnswerString, passwordString);
					userDAO.addUser(newUser);

					login(event);

				} catch (SQLException e) {
					// Handle general database errors
					util.ShowAlert.showAlert("Error", "Database error during registration: " + e.getMessage());
					e.printStackTrace();
				} catch (UserDAO.UserAlreadyExistsException e) {
					// Handle the specific case of the username already existing
					util.ShowAlert.showAlert("Error", e.getMessage());
				}
			} else {
				util.ShowAlert.showAlert("Mismatch Passwords", "Make sure your password fields match");
			}
		} else {
			util.ShowAlert.showAlert("Empty inputs", "Please fill in all input boxes.");
		}
	}

	@FXML
	public void login(ActionEvent event) {
		util.SceneSwitcher.switchScene(event, "/user/resources/Login.fxml", "Login");
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		securityQuestion.getItems().addAll(FXCollections.observableArrayList(securityQuestions));

	}

}
