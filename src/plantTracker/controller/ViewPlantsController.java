package plantTracker.controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

import database.DbHelper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import plantTracker.database.PlantDAO;
import plantTracker.model.CarnivorousPlant;
import plantTracker.model.FruitingPlant;
import plantTracker.model.Plant;
import plantTracker.model.Vegetable;

public class ViewPlantsController implements Initializable {

	private PlantDAO plantDAO = new PlantDAO();
	private ObservableList<Plant> data = FXCollections.observableArrayList();
	private String[] plantTypeOptions = { "Flowering Plant", "Fruiting Plant", "Vegetable", "Herb", "Decorative Plant",
			"Carnivorous Plant" };
	private String[] sunshineOptions = { "Full sun", "Part sun", "Shade" };
	private boolean editModeOn;

	@FXML
	private TableColumn<Plant, String> names;

	@FXML
	private TableColumn<Plant, String> outdoorSeaons;

	@FXML
	private TableColumn<Plant, String> plantedDates;

	@FXML
	private TableColumn<Plant, String> species;

	@FXML
	private TableColumn<Plant, String> sunlightNeeds;

	@FXML
	private TableColumn<Plant, String> types;

	@FXML
	private TableColumn<Plant, String> typeSpecifics;

	@FXML
	private TableView<Plant> plantList;

	@FXML
	private HBox editBox;

	@FXML
	private TextField nameField;

	@FXML
	private TextField speciesField;

	@FXML
	private Label typeSpecificLabel;

	@FXML
	private TextField typeSpecificField;

	@FXML
	private ChoiceBox<String> typeChoices;

	@FXML
	private ChoiceBox<String> sunlightChoices;

	@FXML
	private CheckBox spring;

	@FXML
	private CheckBox summer;

	@FXML
	private CheckBox fall;

	@FXML
	private CheckBox winter;

	@FXML
	private DatePicker datePlantedPicker;

	@FXML
	private Label sceneLabel;

	@FXML
	private HBox bottomBar;

	@FXML
	private void handleAddPlant(MouseEvent event) {
		util.SceneSwitcher.switchScene(event, "/plantTracker/resources/AddPlant.fxml", "Add Plant");
	}

	@FXML
	void handlePlantTracker(MouseEvent event) {
		util.SceneSwitcher.switchScene(event, "/plantTracker/resources/PlantTracker.fxml", "Plant Tracker");
	}

	@FXML
	void handleViewReminders(MouseEvent event) {
		util.SceneSwitcher.switchScene(event, "/plantTracker/resources/ManageReminders.fxml", "Manage Reminders");
	}

	@FXML
	void enableEdit(MouseEvent event) {
		try {
			if (plantList.getSelectionModel().getSelectedItem() == null) {
				throw new IllegalStateException();
			}

			editBox.setVisible(true);
			editBox.setManaged(true);
			editModeOn = true;
			printSelectionStatus();

			String sql = "SELECT ";
			sql += "plantName, species, ";
			sql += "plantType, fruit, vegetable, foodType, ";
			sql += "isFullSun, isPartSun, isShade, ";
			sql += "canBeOutdoors, winter, spring, summer, fall, ";
			sql += "datePlanted ";
			sql += "FROM plant WHERE ";
			sql += "plantName = ?";

			DbHelper dbHelper = new DbHelper();
			Connection connection = dbHelper.getConnection();
			PreparedStatement preparedStatement;
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, plantList.getSelectionModel().getSelectedItem().getName());
			ResultSet results = preparedStatement.executeQuery();

			if (results.next()) {
				nameField.setText(results.getString("plantName"));
				speciesField.setText(results.getString("species"));
				typeChoices.setValue(results.getString("plantType"));

				if (results.getString("plantType").equals("Fruiting Plant")) {
					typeSpecificLabel.setText("Fruit");
					typeSpecificField.setText(results.getString("fruit"));
				} else if (results.getString("plantType").equals("Vegetable")) {
					typeSpecificLabel.setText("Vegetable");
					typeSpecificField.setText(results.getString("vegetable"));
				} else if (results.getString("plantType").equals("CarnivorousPlant")) {
					typeSpecificLabel.setText("Food Type");
					typeSpecificField.setText(results.getString("foodType"));
				} else if (results.getString("plantType").equals("DecorativePlant")
						|| results.getString("plantType").equals("Herb")
						|| results.getString("plantType").equals("FloweringPlant")) {
					setTypeSpecific(false);
				}

				if (results.getBoolean("isFullSun") == true) {
					sunlightChoices.setValue("Full Sun");
				}
				if (results.getBoolean("isPartSun") == true) {
					sunlightChoices.setValue("Partial Sun");
				}
				if (results.getBoolean("isShade") == true) {
					sunlightChoices.setValue("Shade");
				}

				if (results.getBoolean("canBeOutdoors") == false) {
					spring.setSelected(false);
					summer.setSelected(false);
					fall.setSelected(false);
					winter.setSelected(false);
				} else {
					if (results.getBoolean("spring") == true) {
						spring.setSelected(true);
					}
					if (results.getBoolean("summer") == true) {
						summer.setSelected(true);
					}
					if (results.getBoolean("fall") == true) {
						fall.setSelected(true);
					}
					if (results.getBoolean("winter") == true) {
						winter.setSelected(true);
					}
				}

				// The line below is done because toLocalDate is undefined in java.util.Date
				java.sql.Date d = results.getDate("datePlanted");
				LocalDate ld = d.toLocalDate();
				datePlantedPicker.setValue(ld);
			}

			dbHelper.closeConnection(connection);

			System.out.println("************* In Edit Mode *************");
			System.out.println("Manage status for type specific label and field");
			System.out.println("typeSpecificLabel.isManged(): " + typeSpecificLabel.isManaged());
			System.out.println("typeSpecificField.isManged(): " + typeSpecificField.isManaged());
			System.out.println("Visiblity status for type specific label and field");
			System.out.println("typeSpecificLabel.isManged(): " + typeSpecificLabel.isVisible());
			System.out.println("typeSpecificField.isManged(): " + typeSpecificField.isVisible());
			System.out.println("*****************************************");

		} catch (IllegalStateException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setHeaderText(null);
			alert.setContentText("A plant has not been selected");
			alert.show();
		} catch (SQLException e2) {
			e2.printStackTrace();
		}
	}

	@FXML
	void saveEdit(MouseEvent event) {
		try {
			String sql = "UPDATE plant ";
			sql += "SET plantName ? ";
			sql += "WHERE plantName = ?";

			DbHelper dbHelper = new DbHelper();
			Connection connection = dbHelper.getConnection();
			PreparedStatement preparedStatement;
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, nameField.getText());
			preparedStatement.setString(2, plantList.getSelectionModel().getSelectedItem().getName());
			preparedStatement.executeUpdate();
			dbHelper.closeConnection(connection);
			closeEdit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@FXML
	void closeEdit() {
		editModeOn = false;
		editBox.setVisible(false);
		editBox.setManaged(false);
		printSelectionStatus();
	}

	@FXML
	void removePlant(MouseEvent event) {
		try {
			if (plantList.getSelectionModel().getSelectedItem() == null) {
				throw new IllegalStateException();
			}

			Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
			alert.setHeaderText(null);
			alert.setContentText("You are about to delete " + plantList.getSelectionModel().getSelectedItem().getName()
					+ "." + "\nAre you sure?");
			Optional<ButtonType> confirm = alert.showAndWait();
			if (confirm.get() == ButtonType.OK) {
				plantDAO.deletePlant(plantList.getSelectionModel().getSelectedItem().getName());
				data.remove(plantList.getSelectionModel().getSelectedItem());
			}

			if (editModeOn) {
				resetEditComponents();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IllegalStateException e2) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setHeaderText(null);
			alert.setContentText("A plant has not been selected");
			alert.show();
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			bottomBar.prefHeightProperty().bind(sceneLabel.heightProperty());
			editBox.setVisible(false);
			editBox.setManaged(false);
			typeChoices.getItems().addAll(FXCollections.observableArrayList(plantTypeOptions));
			sunlightChoices.getItems().addAll(FXCollections.observableArrayList(sunshineOptions));

			plantDAO.populateWithPlants(data);

			// Note for TableView:
			// In this case, the Plant class is used as the data model
			// Since the plant methods do not return an ObservablueValue,
			// wrapping will be done for the corresponding return values using lambda
			// functions
			// to initialize the cells in the TableView.
			// Casting is another method to consider, but not sure which is better or if it
			// matters in this scenario
			names.setCellValueFactory(cellData -> {
				return new SimpleStringProperty(cellData.getValue().getName());
			});

			species.setCellValueFactory(cellData -> {
				return new SimpleStringProperty(cellData.getValue().getSpecies());
			});

			types.setCellValueFactory(cellData -> {
				if (cellData.getValue().getClass().getSimpleName().equals("FloweringPlant")) {
					return new SimpleStringProperty("Flowering Plant");
				} else if (cellData.getValue().getClass().getSimpleName().equals("FruitingPlant")) {
					return new SimpleStringProperty("Fruiting Plant");
				} else if (cellData.getValue().getClass().getSimpleName().equals("Vegetable")) {
					return new SimpleStringProperty("Vegetable");
				} else if (cellData.getValue().getClass().getSimpleName().equals("Herb")) {
					return new SimpleStringProperty("Herb");
				} else if (cellData.getValue().getClass().getSimpleName().equals("DecorativePlant")) {
					return new SimpleStringProperty("Decorative Plant");
				} else if (cellData.getValue().getClass().getSimpleName().equals("CarnivorousPlant")) {
					return new SimpleStringProperty("Carnivorous Plant");
				}
				return new SimpleStringProperty(cellData.getValue().getClass().getSimpleName());
			});

			typeSpecifics.setCellValueFactory(cellData -> {
				// Plants with type specifics: FruitingPlant, Vegetable, CarnivorousPlant
				// Corresponding to: fruit, vegetable, foodType
				if (cellData.getValue().getClass().getSimpleName().equals("FruitingPlant")
						&& !((FruitingPlant) cellData.getValue()).getFruit().equals("")) {
					return new SimpleStringProperty(((FruitingPlant) cellData.getValue()).getFruit());
				} else if (cellData.getValue().getClass().getSimpleName().equals("Vegetable")
						&& !((Vegetable) cellData.getValue()).getVegetable().equals("")) {
					return new SimpleStringProperty(((Vegetable) cellData.getValue()).getVegetable());
				} else if (cellData.getValue().getClass().getSimpleName().equals("CarnivorousPlant")
						&& !((CarnivorousPlant) cellData.getValue()).getFoodType().equals("")) {
					return new SimpleStringProperty(((CarnivorousPlant) cellData.getValue()).getFoodType());
				}
				return new SimpleStringProperty(null);
			});

			sunlightNeeds.setCellValueFactory(cellData -> {
				if (cellData.getValue().isFullSun()) {
					return new SimpleStringProperty("Full Sun");
				} else if (cellData.getValue().isPartSun()) {
					return new SimpleStringProperty("Partial Sun");
				} else if (cellData.getValue().isShade()) {
					return new SimpleStringProperty("Shade");
				}
				return new SimpleStringProperty(null);
			});

			outdoorSeaons.setCellValueFactory(cellData -> {
				if (cellData.getValue().isCanBeOutdoors()) {
					ArrayList<String> seasons = new ArrayList<>();
					if (cellData.getValue().isSpring()) {
						seasons.add("Spring");
					}
					if (cellData.getValue().isSummer()) {
						seasons.add("Summer");
					}
					if (cellData.getValue().isFall()) {
						seasons.add("Fall");
					}
					if (cellData.getValue().isWinter()) {
						seasons.add("Winter");
					}
					return new SimpleStringProperty(String.join(", ", seasons));
				}
				return new SimpleStringProperty(null);
			});

			plantedDates.setCellValueFactory(cellData -> {
				Date date = cellData.getValue().getDatePlanted();
				return new SimpleStringProperty(new SimpleDateFormat("yyyy-MM-dd").format(date));
			});

			plantList.setItems(data);

			plantList.setOnMouseClicked(click -> {
				if (plantList.getSelectionModel().getSelectedItem() != null) {
					printSelectionStatus();
					if (editModeOn) {
						resetEditComponents();
						enableEdit(click);
					}
				}
			});

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void printSelectionStatus() {
		String selectedPlantName = plantList.getSelectionModel().getSelectedItem().getName();
		System.out.println("****************************************");
		System.out.println("Selected Plant: " + selectedPlantName);
		System.out.println("Edit Mode On: " + editModeOn);
		System.out.println("****************************************");
	}

	private void setTypeSpecific(boolean status) {
		typeSpecificLabel.setManaged(status);
		typeSpecificField.setManaged(status);
		typeSpecificLabel.setVisible(status);
		typeSpecificField.setVisible(status);
	}

	private void resetEditComponents() {
		typeSpecificLabel.setManaged(true);
		typeSpecificField.setManaged(true);
		typeSpecificLabel.setVisible(true);
		typeSpecificField.setVisible(true);

		nameField.clear();
		speciesField.clear();
		typeChoices.setValue(null);
		typeSpecificLabel.setText(null);
		typeSpecificField.clear();
		sunlightChoices.setValue(null);
		spring.setSelected(false);
		summer.setSelected(false);
		fall.setSelected(false);
		winter.setSelected(false);
		datePlantedPicker.setValue(null);
	}

}
