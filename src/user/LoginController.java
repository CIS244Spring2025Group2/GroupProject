package user;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import util.ProjUtil;
import util.SessionManager;

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
	public void register(ActionEvent event) {
		switchScene(event, "/user/resources/Register.fxml", "Create Account");
	}

	@FXML
	public void login(ActionEvent event) {
		String enteredEmail = email.getText();
		String enteredPassword = password.getText();

		try {
			User user = userDAO.getUser(enteredEmail);

			if (user != null) {
				String storedHashedPassword = user.getPassword();
				String hashedEnteredPassword = ProjUtil.getSHA(enteredPassword); // Hash the entered password

				if (hashedEnteredPassword.equals(storedHashedPassword)) {

					SessionManager.setCurrentUser(user);
					// Login successful, switch to main application scene
					switchScene(event, "/plantTracker/resources/PlantTracker.fxml", "Plant Tracker");
				} else {
					showAlert("Invalid User Details", "Invalid email or password.");
				}
			} else {
				showAlert("Invalid User Details", "Invalid email or password.");
			}
		} catch (SQLException e) {
			showAlert("Databse Error", "Database error during login.");
			e.printStackTrace();
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
	public void forgotPassword(ActionEvent event) {
		switchScene(event, "/user/resources/ResetPassword.fxml", "Reset Password");
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
	public void initialize(URL location, ResourceBundle resources) {
		// Allows for side-bar to always take up one-third of screen when resizing
		sideBar.prefWidthProperty().bind(parentBox.widthProperty().divide(3));

	}
}
