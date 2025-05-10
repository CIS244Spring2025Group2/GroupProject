package user;

import database.DatabaseInitializer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * MainApp is the main entry point for Sprout Plant Tracker It check if the
 * database tables exist and creates them if they do not It checks if the
 * default admin user exists and creates them if they do not It then launches
 * the login screen
 */

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

			UserDAO userDAO = new UserDAO();
			userDAO.createDefaultAdminUser(); // Call the method here

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
