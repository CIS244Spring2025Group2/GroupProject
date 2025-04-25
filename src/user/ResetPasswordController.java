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
import javafx.stage.Stage;

public class ResetPasswordController {

	@FXML
	private PasswordField password;

	@FXML
	private PasswordField confirmPassword;

	@FXML
	private Button save;

	public void save(ActionEvent event) throws IOException {

		// navigate to new screen
		Parent loginParent = FXMLLoader.load(getClass().getResource("/user/resources/Login.fxml"));
		Scene loginScene = new Scene(loginParent);
		Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		window.setScene(loginScene);
		window.setTitle("Login");
		window.show();
	}
}
