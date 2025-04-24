package plantTracker;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.List;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class AddPlantController implements Initializable {

	private String[] outsideOptions = { "Spring", "Summer", "Fall", "Winter" };
	private String[] sunshineOptions = { "Full sun", "Part sun", "Shade" };
	private String[] plantTypeOptions = { "Flowering Plant", "Fruiting Plant", "Vegetable", "Herb", "Decorative Plant",
			"Carnivorous Plant" };
	private PlantDAO plantDAO = new PlantDAO(); // Create an instance of PlantDAO

	@FXML
	private ComboBox<String> plantType;
	@FXML
	private TextField plantName;
	@FXML
	private TextField species;
	@FXML
	private DatePicker datePlanted;
	@FXML
	private ListView<String> outside;
	@FXML
	private ComboBox<String> sunshine;
	@FXML
	private TextField fruit;
	@FXML
	private TextField vegetable;
	@FXML
	private TextField foodType;
	@FXML
	private Button save;
	@FXML
	private Label sceneLabel;
	@FXML
	private HBox bottomBar;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		plantType.getItems().addAll(FXCollections.observableArrayList(plantTypeOptions));
		sunshine.getItems().addAll(FXCollections.observableArrayList(sunshineOptions));
		outside.getItems().addAll(FXCollections.observableArrayList(outsideOptions));
		outside.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		bottomBar.prefHeightProperty().bind(sceneLabel.heightProperty());

		// Initially disable fruit and vegetable fields
		fruit.setDisable(true);
		vegetable.setDisable(true);
		foodType.setDisable(true);

		// Add listener to enable/disable fruit field based on plant type
		plantType.valueProperty().addListener((observable, oldValue, newValue) -> {
			fruit.setDisable(!newValue.equals("Fruiting Plant"));
			vegetable.setDisable(!newValue.equals("Vegetable"));
			foodType.setDisable(!newValue.equals("Carnivorous Plant"));
			if (!newValue.equals("Fruiting Plant")) {
				fruit.clear();
			}
			if (!newValue.equals("Vegetable")) {
				vegetable.clear();
			}
			if (!newValue.equals("Carnivorous Plant")) {
				foodType.clear();
			}
		});
	}

	public void save(ActionEvent event) throws IOException {
		String name = plantName.getText();
		String plantSpecies = species.getText();
		java.util.Date plantedDateUtil = Date
				.from(datePlanted.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
		List<String> outsideSeasons = outside.getSelectionModel().getSelectedItems();
		String sunshineNeed = sunshine.getValue();
		String selectedPlantType = plantType.getValue();
		String fruitName = fruit.getText();
		String vegetableName = vegetable.getText();
		String foodTypeName = foodType.getText();
		boolean isWatered = false; // Consider adding a UI element for this

		Plant newPlant = null;

		if (selectedPlantType != null) {
			switch (selectedPlantType) {
			case "Fruiting Plant":
				newPlant = new FruitingPlant(name, plantSpecies, plantedDateUtil, fruitName);
				break;
			case "Vegetable":
				newPlant = new Vegetable(name, plantSpecies, plantedDateUtil, vegetableName);
				break;
			case "Carnivorous Plant":
				newPlant = new CarnivorousPlant(name, plantSpecies, plantedDateUtil, foodTypeName);
				break;
			default:
				newPlant = new Plant(name, plantSpecies, plantedDateUtil) {
				};
				break;
			}

			if (newPlant != null) {
				newPlant.setCanBeOutdoors(!outsideSeasons.isEmpty());
				newPlant.setSpring(outsideSeasons.contains("Spring"));
				newPlant.setSummer(outsideSeasons.contains("Summer"));
				newPlant.setFall(outsideSeasons.contains("Fall"));
				newPlant.setWinter(outsideSeasons.contains("Winter"));
				newPlant.setWatered(isWatered);
				if (sunshineNeed != null) {
					newPlant.setFullSun(sunshineNeed.equals("Full sun"));
					newPlant.setPartSun(sunshineNeed.equals("Part sun"));
					newPlant.setShade(sunshineNeed.equals("Shade"));
				}

				try {
					plantDAO.addPlant(newPlant); // Use the PlantDAO to save
					showAlert("Success", "Plant added successfully!");
					clearInputFields();
				} catch (SQLException e) {
					showAlert("Error", "Error adding plant to the database: " + e.getMessage());
					e.printStackTrace();
				}
			} else {
				showAlert("Warning", "Error creating plant object.");
			}
		} else {
			showAlert("Warning", "Please select a plant type.");
		}

		// After attempting to save, navigate to the View Plants screen
		Parent viewPlantsParent = FXMLLoader.load(getClass().getResource("ViewPlants.fxml"));
		Scene viewPlantsScene = new Scene(viewPlantsParent);
		Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		window.setScene(viewPlantsScene);
		window.setTitle("View Plants");
		window.show();
	}

	private void showAlert(String title, String content) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(content);
		alert.showAndWait();
	}

	private void clearInputFields() {
		plantName.clear();
		species.clear();
		datePlanted.setValue(null);
		outside.getSelectionModel().clearSelection();
		sunshine.setValue(null);
		plantType.setValue(null);
	}
}
