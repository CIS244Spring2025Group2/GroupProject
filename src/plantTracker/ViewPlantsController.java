package plantTracker;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ViewPlantsController {

	@FXML
	private Button addPlant;

	@FXML
	private Button back;

	@FXML
	public void handleAddPlant(ActionEvent event) {
		switchScene(event, "AddPlant.fxml", "Add Plant"); // replace with your actual file path if needed
	}

	@FXML
	public void handlePlantTracker(ActionEvent event) {
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

//	// VBox that contains buttons in plant list
//	private static VBox vBox = new VBox();
//
//	ViewPlantsController() {
//		display();
//	}
//
//	private void display() {
//		// Setting styling of vBox containg buttons, affects spacing
//		vBox.setSpacing(5);
//		vBox.setPadding(new Insets(10));
//
//		// Initializing scrollPane with vBox which contains buttons in plant list
//		ScrollPane scrollPane = new ScrollPane();
//		scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
//		scrollPane.setContent(vBox);
//		// Allows vBox inside scrollPane which has the buttons to fill width
//		scrollPane.setFitToWidth(true);
//
//		// Initializing comboBox
//		ComboBox<String> comboBox = new ComboBox<String>();
//		comboBox.setPromptText("Add Plant");
//		comboBox.getItems().add("Carnivorous");
//		comboBox.getItems().add("Decorative");
//		comboBox.getItems().add("Flowering");
//		comboBox.getItems().add("Fruiting");
//		comboBox.getItems().add("Vegetable");
//
//		// Problem: the same plant type is unable to be added twice in a row if clicked
//		// from drop down
//		// Note: unsure about how to implement action event with AddPlant class
//		comboBox.setOnAction(e -> add(comboBox.getValue()));
//
//		// Initializing VBox that contains everything in the scene
//		VBox vBox2 = new VBox(10);
//		vBox2.setPadding(new Insets(20));
//		vBox2.getChildren().addAll(scrollPane, comboBox);
//
//		// Initializing Scene
//		Scene scene = new Scene(vBox2, 300, 300);
//
//		// Initializing Stage
//		Stage stage = new Stage();
//		stage.setScene(scene);
//		stage.show();
//
//		// For testing purposes. Comment out or delete if desired.
//		System.out.println("vBox.isResizable(): " + vBox.isResizable());
//		System.out.println("Called: this.ViewPlant.display() | " + "Hash Code: " + System.identityHashCode(this));
//	}
//
//	private void add(String name) {
//		// Adds a button for each plant to the plant list when option from drop down is
//		// clicked
//		// Note: unsure how to proceed with additional functionality when button is
//		// pressed
//
//		Button button = new Button();
//		button.setText(name);
//		button.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
//		// Setting Style Properties
//		button.setStyle("-fx-font-size: 20; -fx-font-weight: bold");
//		// Adding button to vBox
//		vBox.getChildren().add(button);
//		// For testing purposes. Comment out or delete if desired.
//		button.setOnAction(e -> new AddPlantController());
//	}
}
