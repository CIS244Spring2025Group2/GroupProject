package user;

import java.io.IOException;
import java.net.URL;
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
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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

	public void register(ActionEvent event) throws IOException {

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

	private void showAlert(String title, String content) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(content);
		alert.showAndWait();
	}

}
