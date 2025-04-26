package plantTracker;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import database.DbHelper;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ViewPlantStatsController implements Initializable {
	
	private PlantDAO plantDAO = new PlantDAO(); 

	@FXML
	private DatePicker date;

	@FXML
	private Button edit;

	@FXML
	private Text editStatus;

	@FXML
	private Button home;

	@FXML
	private TextField name;

	@FXML
	private TextField outdoor;
	
	@FXML
	private HBox seasonsBox;

	@FXML
	private Button plantList;

	@FXML
	private Button reminders;

	@FXML
	private Button save;

	@FXML
	private TextField species;

	@FXML
	private TextField sunlight;
	
	@FXML
	private ComboBox<String> sunlightBox;
	private String[] sunshineOptions = { "Full sun", "Part sun", "Shade" };

	@FXML
	private TextField type;

	@FXML
	private TextField typeSpecific;

	@FXML
	private Label typeSpecificLabel;

	@FXML
	private Label sceneLabel;

	@FXML
	private HBox bottomBar;

	@FXML
	private void enableEdit(ActionEvent e) {
		setDisable(false);
		seasonsBox.setVisible(true);
		date.getEditor().addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
			date.show();
		});
		outdoor.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
			seasonsBox.setStyle("-fx-border-color: red;");
		});
		sunlight.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
			sunlightBox.setStyle("-fx-border-color: red;");
		});
		editStatus.setText("Plant attributes are now open for edit. Press save to set changes.");
	}

	@FXML
	private void saveEdit(ActionEvent e) {
		setDisable(true);
		sunlightBox.setStyle("-fx-border-color: transparent;");
		seasonsBox.setVisible(false);
		editStatus.setText("Plant attributes above are now closed for edit and changes are saved. Press edit again to change.");
	}

	@FXML
	private void toPlantTracker(ActionEvent e) {
		switchScene(e, "/plantTracker/resources/PlantTracker.fxml", "Plant Tracker");
	}

	@FXML
	private void toPlantList(ActionEvent e) {
		switchScene(e, "/plantTracker/resources/ViewPlants.fxml", "Plant List");
	}

	@FXML
	private void toReminders(ActionEvent e) {
		switchScene(e, "/plantTracker/resources/ViewReminders.fxml", "Reminders");
	}

	private void setDisable(boolean state) {
		name.setDisable(state);
		date.setDisable(state);
		type.setDisable(state);
		species.setDisable(state);
		typeSpecific.setDisable(state);
		sunlight.setDisable(state);
		sunlightBox.setDisable(state);
		outdoor.setDisable(state);
		seasonsBox.setDisable(state);
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

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		bottomBar.prefHeightProperty().bind(sceneLabel.heightProperty());
		sunlightBox.getItems().addAll(FXCollections.observableArrayList(sunshineOptions));
		
		try {
			DbHelper dbHelper = new DbHelper();
			String sql = "SELECT ";
			sql += "plantType, plantName, ";
			sql += "datePlanted, species, ";
			sql += "canBeOutdoors, winter, spring, summer, fall, ";
			sql += "isFullSun, isPartSun, isShade, ";
			sql += "fruit, vegetable, foodType ";
			sql += "FROM plant WHERE ";
			sql += "plantName = ?" ;
			System.out.println(sql);
			Connection connection = dbHelper.getConnection();
			PreparedStatement preparedStatement;
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, PlantDAO.getSelectedName());
			ResultSet results = preparedStatement.executeQuery();
			while (results.next()) {
				// Set type and name
				type.setText(results.getString("plantType"));
				name.setText(results.getString("plantName"));
				// Set date
				Date d = results.getDate("datePlanted");
				LocalDate ld = d.toLocalDate();
				date.setValue(ld);
				// Set species
				species.setText(results.getString("species"));
				// Set outdoor seasons
				String outdoorSeasons;
				if(results.getBoolean("canBeOutdoors") == false) {
					outdoorSeasons = "Cannot be outdoors";
					outdoor.setText(outdoorSeasons);
				} else {
					ArrayList<String> seasons = new ArrayList<>();
					if(results.getBoolean("winter") == true) {seasons.add("Winter");}
					if(results.getBoolean("spring") == true) {seasons.add("Spring");}
					if(results.getBoolean("summer") == true) {seasons.add("Summer");}
					if(results.getBoolean("fall") == true) {seasons.add("Fall");}
					outdoorSeasons = String.join(", ", seasons);
					outdoor.setText(outdoorSeasons);
				}
				// Set sunlight need
				if(results.getBoolean("isFullSun") == true) {sunlight.setText("Full Sun");}
				if(results.getBoolean("isPartSun") == true) {sunlight.setText("Partial Sun");}
				if(results.getBoolean("isShade") == true) {sunlight.setText("Shade");}
				// Set type specific 
				if(results.getString("plantType").equals("FruitingPlant")) {
					typeSpecificLabel.setText("Fruit");
					typeSpecific.setText(results.getString("fruit"));
				} else if(results.getString("plantType").equals("Vegetable")) {
					typeSpecificLabel.setText("Vegetable");
					typeSpecific.setText(results.getString("vegetable"));
				} else if(results.getString("plantType").equals("CarnivorousPlant")) {
					typeSpecificLabel.setText("Food Type");
					typeSpecific.setText(results.getString("foodType"));
				} else if(results.getString("plantType").equals("DecorativePlant") || 
						results.getString("plantType").equals("Herb") || 
						results.getString("plantType").equals("FloweringPlant")) {
					typeSpecificLabel.setVisible(false);
					typeSpecific.setVisible(false);
				} 
			}
			dbHelper.closeConnection(connection);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//To make DatePicker darker
		date.setOpacity(1);
	}

}
