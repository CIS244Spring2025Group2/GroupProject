package user;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import util.ProjUtil;
import util.SceneSwitcher;
import util.SessionManager;
import util.ShowAlert;

/**
 * LoginController handles user login, and redirects to register or
 * ResetPassword
 */
public class LoginController implements Initializable {

	@FXML
	private HBox parentBox;

	@FXML
	private VBox sideBar;

	@FXML
	private TextField email;

	@FXML
	private PasswordField password;

	@FXML
	private Button login;

	@FXML
	private Button register;

	@FXML
	private Button forgotPassword;

	private UserDAO userDAO = new UserDAO();

	@FXML
	// redirects to the register scene
	public void register(ActionEvent event) {
		SceneSwitcher.switchScene(event, "/user/resources/Register.fxml", "Create Account");
	}

	@FXML
	// when login is pressed, it checks inputs and verifies login
	public void login(ActionEvent event) {
		String enteredEmail = email.getText();
		String enteredPassword = password.getText();

		try {
			// gets the user by email
			User user = userDAO.getUser(enteredEmail);

			if (user != null) {
				// checks the password against the stored password
				String storedHashedPassword = user.getPassword();
				String hashedEnteredPassword = ProjUtil.getSHA(enteredPassword); // Hash the entered password

				if (hashedEnteredPassword.equals(storedHashedPassword)) {

					// sets the current user in the Session Manager
					SessionManager.setCurrentUser(user);
					// Login successful, switch to main application scene
					SceneSwitcher.switchScene(event, "/plantTracker/resources/PlantTracker.fxml", "Sprout Home");
				} else {
					ShowAlert.showAlert("Invalid User Details", "Invalid email or password.");
				}
			} else {
				ShowAlert.showAlert("Invalid User Details", "Invalid email or password.");
			}
		} catch (SQLException e) {
			ShowAlert.showAlert("Databse Error", "Database error during login.");
			e.printStackTrace();
		}
	}

	@FXML
	// redirects to Reset Password scene
	public void forgotPassword(ActionEvent event) {
		SceneSwitcher.switchScene(event, "/user/resources/ResetPassword.fxml", "Reset Password");
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Allows for side-bar to always take up one-third of screen when resizing
		sideBar.prefWidthProperty().bind(parentBox.widthProperty().divide(3));

	}
}
