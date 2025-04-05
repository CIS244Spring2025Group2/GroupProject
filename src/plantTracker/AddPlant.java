package plantTracker;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AddPlant {

	private String title;
	private String[] outsideOptions = { "Spring", "Summer", "Fall", "Winter" };
	private String[] sunshineOptions = { "Full sun", "Part sun", "Shade" };
	private String[] plantTypeOptions = { "Flowering Plant", "Fruiting Plant", "Vegetable", "Herb", "Decorative Plant",
			"Carnivorous Plant" };
	private Plant plant;
	private boolean canBeOutside;
	private String sunshineNeed;

	public AddPlant() {
		display();
	}

	public void display() {

		Stage stage = new Stage();

		stage.initModality(Modality.APPLICATION_MODAL);

		// Border pane to organize window
		BorderPane border = new BorderPane();

		VBox vBox = new VBox();
		vBox.setAlignment(Pos.CENTER);
		vBox.setPadding(new Insets(10, 5, 5, 10));

		HBox hBox = new HBox();
		hBox.setAlignment(Pos.CENTER);
		hBox.setPadding(new Insets(10, 5, 5, 10));

		VBox dateField = new VBox();

		Pane pane = new Pane();

		ComboBox<String> plantType = new ComboBox<>();
		plantType.getItems().addAll(FXCollections.observableArrayList(plantTypeOptions));

		// get plant species, name, and date planted
		TextField plantName = new TextField("Plant Name");
		TextField species = new TextField("Species");

		// label and datePicker in a VBox
		Label datePlantedLabel = new Label("Date Planted");
		DatePicker datePlanted = new DatePicker();

		// convert date picker local date into a Date() object
		LocalDate ld = datePlanted.getValue();
		Calendar c = Calendar.getInstance();
		c.set(ld.getYear(), ld.getMonthValue() - 1, ld.getDayOfMonth());
		Date date = c.getTime();

		// outside seasons
		ListView<String> outside = new ListView<>(FXCollections.observableArrayList(outsideOptions));
		outside.setPrefSize(400, 400);
		outside.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		outside.getSelectionModel().selectionModeProperty().addListener(ov -> {
			outside.getSelectionModel().getSelectedIndices();
			outside.getSelectionModel().getSelectedItems();
		});

		// sunshine options
		ComboBox<String> sunshine = new ComboBox<>();

		// save button instantiates an object
		Button save = new Button("Save"); // on save, creates a plant object and saves to the database

		// need a way to get plant type when opening this window - this is a stand in
		// for now
		save.setOnAction(e -> {
			if (plantType.getValue() == "Flowering Plant") {
				plant = new FloweringPlant(plantName.getText(), species.getText(), date);
			} else if (plantType.getValue() == "Fruiting Plant") {
				plant = new FruitingPlant(plantName.getText(), species.getText(), date);
			} else if (plantType.getValue() == "Vegetable") {
				plant = new Vegetable(plantName.getText(), species.getText(), date);
			} else if (plantType.getValue() == "Carnivorous Plant") {
				plant = new CarnivorousPlant(plantName.getText(), species.getText(), date);
			} else if (plantType.getValue() == "Decorative Plant") {
				plant = new DecorativePlant(plantName.getText(), species.getText(), date);
			} else if (plantType.getValue() == "Herb") {
				plant = new Herb(plantName.getText(), species.getText(), date);
			}
		});

		// need to add if/else statements for extra variables for each plant type
		sunshine.getItems().addAll(FXCollections.observableArrayList(sunshineOptions));
		// sunshine.setOnAction(e −> sunshineNeed =
		// sunshineOptions.indexOf(sunshine.getValue()));

		dateField.getChildren().addAll(datePlantedLabel, datePlanted);

		vBox.getChildren().addAll(plantName, species);
		hBox.getChildren().addAll(dateField, outside, sunshine);

		pane.getChildren().add(save);

		border.setTop(hBox);
		border.setCenter(vBox);
		border.setBottom(pane);

		// Create a scene and place it in the stage
		Scene scene = new Scene(border, 400, 250);
		stage.setTitle(title); // Set the stage title
		stage.setScene(scene); // Place the scene in the stage
		stage.show(); // Display the stage
	}
}
