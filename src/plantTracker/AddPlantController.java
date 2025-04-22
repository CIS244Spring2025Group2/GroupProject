package plantTracker;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
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
	private DbHelper dbHelper = new DbHelper();

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
	private TextField fruit; // Added TextField for fruit input
	@FXML
	private TextField vegetable; // Added TextField for vegetable input
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

		// Add listener to enable/disable fruit field based on plant type
		plantType.valueProperty().addListener((observable, oldValue, newValue) -> {
			fruit.setDisable(!newValue.equals("Fruiting Plant"));
			vegetable.setDisable(!newValue.equals("Vegetable"));
			if (!newValue.equals("Fruiting Plant")) {
				fruit.clear();
			}
			if (!newValue.equals("Vegetable")) {
				vegetable.clear();
			}
		});
	}

	public void save(ActionEvent event) throws IOException {
		String name = plantName.getText();
		String plantSpecies = species.getText();
		java.util.Date plantedDateUtil = Date
				.from(datePlanted.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
		Date plantedDateSql = new Date(plantedDateUtil.getTime());
		List<String> outsideSeasons = outside.getSelectionModel().getSelectedItems();
		String sunshineNeed = sunshine.getValue();
		String selectedPlantType = plantType.getValue();
		String fruitName = fruit.getText(); // Get fruit text
		String vegetableName = vegetable.getText(); // Get vegetable text

		String sql = "INSERT INTO Plant (plantType, plantName, datePlanted, species, canBeOutdoors, winter, spring, summer, fall, isFullSun, isPartSun, isShade, fruit, vegetable, watered) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		if (selectedPlantType != null) {
			try {
				connection = dbHelper.getConnection();
				preparedStatement = connection.prepareStatement(sql);

				preparedStatement.setString(1, selectedPlantType);
				preparedStatement.setString(2, name);
				preparedStatement.setDate(3, plantedDateSql);
				preparedStatement.setString(4, plantSpecies);
				preparedStatement.setBoolean(5, !outsideSeasons.isEmpty());
				preparedStatement.setBoolean(6, outsideSeasons.contains("Winter"));
				preparedStatement.setBoolean(7, outsideSeasons.contains("Spring"));
				preparedStatement.setBoolean(8, outsideSeasons.contains("Summer"));
				preparedStatement.setBoolean(9, outsideSeasons.contains("Fall"));
				preparedStatement.setBoolean(10, sunshineNeed != null && sunshineNeed.equals("Full sun"));
				preparedStatement.setBoolean(11, sunshineNeed != null && sunshineNeed.equals("Part sun"));
				preparedStatement.setBoolean(12, sunshineNeed != null && sunshineNeed.equals("Shade"));
				preparedStatement.setString(13, selectedPlantType.equals("Fruiting Plant") ? fruitName : null);
				preparedStatement.setString(14, selectedPlantType.equals("Vegetable") ? vegetableName : null);
				preparedStatement.setString(15, "false"); // Default watered status

				preparedStatement.executeUpdate();
				showAlert("Success", "Plant added successfully!");
				clearInputFields();

			} catch (SQLException e) {
				showAlert("Error", "Error adding plant to the database: " + e.getMessage());
				e.printStackTrace(); // Log the error
			} finally {
				if (preparedStatement != null) {
					try {
						preparedStatement.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				dbHelper.closeConnection(connection);
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
