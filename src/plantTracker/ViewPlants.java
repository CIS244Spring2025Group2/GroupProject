

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

class ViewPlants {
	// VBox that contains buttons in plant list
	private static VBox vBox = new VBox();
	
	ViewPlants() {
		display();
	}
	
	private void display() {
		// Setting styling of vBox containg buttons, affects spacing
		vBox.setSpacing(5);
		vBox.setPadding(new Insets(10));
		
		// Initializing scrollPane with vBox which contains buttons in plant list
		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
		scrollPane.setContent(vBox);
		// Allows vBox inside scrollPane which has the buttons to fill width
		scrollPane.setFitToWidth(true);
		
		// Initializing comboBox 
		ComboBox<String> comboBox = new ComboBox<String>();
		comboBox.setPromptText("Add Plant");
		comboBox.getItems().add("Carnivorous");
		comboBox.getItems().add("Decorative");
		comboBox.getItems().add("Flowering");
		comboBox.getItems().add("Fruiting");
		comboBox.getItems().add("Vegetable");
		// Problem: the same plant type is unable to be added twice in a row if clicked from drop down
		// Note: unsure about how to implement action event with AddPlant class
		comboBox.setOnAction(e -> add(comboBox.getValue()));
		
		// Initializing VBox that contains everything in the scene
		VBox vBox2 = new VBox(10);
		vBox2.setPadding(new Insets(20));
		vBox2.getChildren().addAll(scrollPane, comboBox);
		
		// Initializing Scene
		Scene scene = new Scene(vBox2, 300, 300);
		
		// Initializing Stage
		Stage stage = new Stage();
		stage.setScene(scene);
		stage.show();
		
		// For testing purposes. Comment out or delete if desired.
		System.out.println("vBox.isResizable(): " + vBox.isResizable());
		System.out.println("Called: this.ViewPlant.display() | " + 
							"Hash Code: " + System.identityHashCode(this));
	}
	
	private void add(String name) {
		// Adds a button for each plant to the plant list when option from drop down is clicked
		// Note: unsure how to proceed with additional functionality when button is pressed
		Button button = new Button();
		button.setText(name);
		button.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		// Setting Style Properties
		button.setStyle("-fx-font-size: 20; -fx-font-weight: bold");
		// Adding button to vBox
		vBox.getChildren().add(button);
		// For testing purposes. Comment out or delete if desired.
		button.setOnAction(e -> System.out.println("Pressed: "+ name + " Plant Button | " +
													"Hash Code: " + System.identityHashCode(button)));
	}
}
