package user;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RegisterController {

	@FXML
	private TextField userName;

	@FXML
	private PasswordField password;

	@FXML
	private PasswordField confirmPassword;

	@FXML
	private Button register;

	public void goToPlantTracker(ActionEvent event) throws IOException {
		Parent loginParent = FXMLLoader.load(getClass().getResource("/user/resources/Login.fxml"));
		Scene loginScene = new Scene(loginParent);

		// This line gets the Stage information
		Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

		window.setScene(loginScene);
		window.setTitle("Plant Tracker");
		window.show();
	}

}
