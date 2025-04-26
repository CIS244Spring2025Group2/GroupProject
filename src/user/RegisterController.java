package user;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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

		String userNameString = email.getText();
		String fistNameString = firstName.getText();
		String lastNameString = lastName.getText();
		String securityQuestionString = securityQuestion.getValue();
		String securityAnswerString = securityAnswer.getText();
		String passwordString = password.getText();
		String confirmPasswordString = confirmPassword.getText();

		User newUser = null;

		if (!userNameString.isEmpty() && !fistNameString.isEmpty() && !lastNameString.isEmpty()
				&& securityQuestionString != null && !securityAnswerString.isEmpty() && !passwordString.isEmpty()) {
			if (passwordString.equals(confirmPasswordString)) {
				try {
					UserDAO userDAO = new UserDAO();
					newUser = new User(userNameString, fistNameString, lastNameString, securityQuestionString,
							securityAnswerString, passwordString);
					userDAO.addUser(newUser);

					login(event);

				} catch (SQLException e) {
					// Handle general database errors
					showAlert("Error", "Database error during registration: " + e.getMessage());
					e.printStackTrace();
				} catch (UserDAO.UserAlreadyExistsException e) {
					// Handle the specific case of the username already existing
					showAlert("Error", e.getMessage());
				}
			} else {
				showAlert("Mismatch Passwords", "Make sure your password fields match");
			}
		} else {
			showAlert("Empty inputs", "Please fill in all input boxes.");
		}
	}

	private void showAlert(String title, String content) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(content);
		alert.showAndWait();
	}

	@FXML
	public void login(ActionEvent event) {
		switchScene(event, "/user/resources/Login.fxml", "Login");
	}

	public void switchScene(ActionEvent event, String fxmlFile, String title) {
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

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		securityQuestion.getItems().addAll(FXCollections.observableArrayList(securityQuestions));

	}

}
