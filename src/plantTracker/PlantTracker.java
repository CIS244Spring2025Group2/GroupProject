package plantTracker;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PlantTracker extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		// TODO Auto-generated method stub

		primaryStage = new Stage();

		// Create buttons
		Button viewPlantsButton = new Button("View Current Plants");
		Button viewRemindersButton = new Button("View All Reminders");

		// Add event handlers for navigation
		viewPlantsButton.setOnAction(e -> new ViewPlants());
		viewRemindersButton.setOnAction(e -> new ViewReminders());

		// Create TextArea for upcoming reminders
		TextArea remindersArea = new TextArea();
		remindersArea.setEditable(false);
		remindersArea.setPromptText("Upcoming reminders will appear here...");

		// Layout setup
		VBox layout = new VBox(15);
		layout.setPadding(new Insets(20));
		layout.getChildren().addAll(viewPlantsButton, viewRemindersButton, remindersArea);

		// Create Scene
		Scene homeScene = new Scene(layout, 400, 300);

		// Set up the stage
		primaryStage.setTitle("Plant Tracker - Home");
		primaryStage.setScene(homeScene);
		primaryStage.show();

	}

}
