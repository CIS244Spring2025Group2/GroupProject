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
import javafx.stage.Stage;

public class LoginController implements Initializable {

	@FXML
	Button login;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub

	}

	public void login(ActionEvent event) throws IOException {
		Parent plantTrackerParent = FXMLLoader.load(getClass().getResource("PlantTracker.fxml"));
		Scene plantTrackerScene = new Scene(plantTrackerParent);

		// This line gets the Stage information
		Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

		window.setScene(plantTrackerScene);
		window.setTitle("Plant Tracker");
		window.show();
	}
}
