package plantTracker;

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
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginController implements Initializable {
	
	@FXML
	private HBox parentBox;
	
	@FXML
	private VBox sideBar;

	@FXML
	private TextField userName;

	@FXML
	private TextField password;

	@FXML
	private Button login;

	@FXML
	private Button register;

	@FXML
	public void register(ActionEvent event) {
		switchScene(event, "Register.fxml", "Register"); // replace with your actual file path if needed
	}

	@FXML
	public void login(ActionEvent event) {
		switchScene(event, "PlantTracker.fxml", "Plant Tracker"); // replace with your actual file path if needed
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
		//Allows for side-bar to always take up one-third of screen when resizing
		sideBar.prefWidthProperty().bind(parentBox.widthProperty().divide(3));
		
	}
}
