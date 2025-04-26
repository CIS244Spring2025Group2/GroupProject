package user;

import java.io.IOException;
import java.net.URL;
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
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ResetPasswordController implements Initializable {

	@FXML
	private TextField email;

	@FXML
	private Button submit;

	@FXML
	private Label securityQuestions;

	@FXML
	private TextField securityAnswer;

	@FXML
	private PasswordField password;

	@FXML
	private PasswordField confirmPassword;

	@FXML
	private Button save;

	@FXML
	private Button cancel;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub

	}

	private void showAlert(String title, String content) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(content);
		alert.showAndWait();
	}

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
