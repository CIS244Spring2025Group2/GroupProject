package plantTracker;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		// TODO Auto-generated method stub

		try {

			Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));

			// Create Scene
			Scene homeScene = new Scene(root);

			// Set up the stage
			primaryStage.setTitle("Login");
			primaryStage.setScene(homeScene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
