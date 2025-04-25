package user;

import database.DatabaseInitializer;
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

			// Initialize the database tables
			DatabaseInitializer initializer = new DatabaseInitializer();
			initializer.initializeDatabase();

			Parent root = FXMLLoader.load(getClass().getResource("/user/resources/Login.fxml"));

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
