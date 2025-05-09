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
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import plantTracker.database.PlantDAO;
import plantTracker.model.CarnivorousPlant;
import plantTracker.model.DecorativePlant;
import plantTracker.model.FloweringPlant;
import plantTracker.model.FruitingPlant;
import plantTracker.model.Herb;
import plantTracker.model.Plant;
import plantTracker.model.Vegetable;
import user.User;
import util.SceneSwitcher;
import util.SessionManager;

public class ViewPlantsController implements Initializable {
	private User loggedInUser = SessionManager.getCurrentUser();
	private PlantDAO plantDAO = new PlantDAO();
	private ObservableList<Plant> data = FXCollections.observableArrayList();
	private String[] plantTypeOptions = { "Flowering Plant", "Fruiting Plant", "Vegetable", "Herb", "Decorative Plant",
			"Carnivorous Plant" };
	private String[] sunshineOptions = { "Full sun", "Part sun", "Shade" };
	private boolean editModeOn;
	private ChangeListener<String> typeLisener;
	
	@FXML
	private Parent root;
	
	@FXML
	private MenuItem logout;

	@FXML
	private MenuItem updatePassword;

	@FXML
	private MenuItem editUserInfo;

	@FXML
	private MenuItem manageUsers;

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
	private VBox editBox;

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
	private TextField searchBar;
	
	private void updateAdminButtonVisibility() {
		if (loggedInUser != null && loggedInUser.isAdmin()) {
			manageUsers.setVisible(true);
		} else {
			manageUsers.setVisible(false);
		}
	}
	
	@FXML
	private void handleManageUsers(ActionEvent event) {
		if (loggedInUser != null && loggedInUser.isAdmin()) {
			util.SceneSwitcher.switchScene(event, "/user/resources/ManageUsers.fxml", "Manage Users", root);
		} else {
			util.ShowAlert.showAlert("Access Denied", "You do not have administrator privileges.");
		}
	}

	@FXML
	public void handleUpdatePassword(ActionEvent event) {
		util.SceneSwitcher.switchScene(event, "/user/resources/UpdatePassword.fxml", "Update Password", root);
	}

	@FXML
	public void handleEditUserInfo(ActionEvent event) {
		util.SceneSwitcher.switchScene(event, "/user/resources/EditUser.fxml", "Edit User", root);
	}

	@FXML
	public void handleLogout(ActionEvent event) {
		SessionManager.logoutUser();
		util.SceneSwitcher.switchScene(event, "/user/resources/Login.fxml", "Login", root);
	}

	@FXML
	private void handleAddPlant(MouseEvent event) {
		SceneSwitcher.switchScene(event, "/plantTracker/resources/AddPlant.fxml", "Add Plant");
	}

	@FXML
	void handlePlantTracker(MouseEvent event) {
		SceneSwitcher.switchScene(event, "/plantTracker/resources/PlantTracker.fxml", "Sprout Home");
	}

	@FXML
	void handleViewReminders(MouseEvent event) {
		SceneSwitcher.switchScene(event, "/plantTracker/resources/ManageReminders.fxml", "Manage Reminders");
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
				}

				else if (results.getString("plantType").equals("Vegetable")) {
					typeSpecificLabel.setText("Vegetable");
					typeSpecificField.setText(results.getString("vegetable"));
				}

				else if (results.getString("plantType").equals("Carnivorous Plant")) {
					typeSpecificLabel.setText("Food Type");
					typeSpecificField.setText(results.getString("foodType"));
				}

				else {
					allowTypeSpecificGUI(false);
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

			typeLisener = (observableValue, oldValue, newValue) -> {
				if (!hasTypeSpecific(newValue)) {
					allowTypeSpecificGUI(false);
				} else {
					allowTypeSpecificGUI(true);
					allowTypeSpecificGUI(newValue);
				}
			};

			typeChoices.valueProperty().addListener(typeLisener);

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
		/*
		 * This function compares the original values of the selected Plant from the
		 * TableView to the values of the components in edit mode. If there is a
		 * difference, update queries and and changed values are added to two separate
		 * lists and are used to form the query for updating the database. The changes
		 * also modify the Plant object selected from the TableView.
		 */
		try {
			ArrayList<String> columnUpdates = new ArrayList<>();
			ArrayList<Object> changedValues = new ArrayList<>();
			Plant originalPlant = plantList.getSelectionModel().getSelectedItem();
			String originalName = originalPlant.getName();
			Plant changedPlant = null;
			boolean typeChanged = false;
			// Check to see if type has been changed and if new instance needs to be created
			if (!typeChoices.getValue().equals(originalPlant.getType())) {
				// { "Flowering", "Fruiting", "Vegetable","Herb", "Decorative", "Carnivorous" };
				// Create new instance depending on value in ChoiceBox and set typeChanged as
				// true
				typeChanged = true;
				if (typeChoices.getValue().equals("Flowering Plant")) {
					changedPlant = new FloweringPlant();
				} else if (typeChoices.getValue().equals("Fruiting Plant")) {
					changedPlant = new FruitingPlant();
				} else if (typeChoices.getValue().equals("Vegetable")) {
					changedPlant = new Vegetable();
				} else if (typeChoices.getValue().equals("Herb")) {
					changedPlant = new Herb();
				} else if (typeChoices.getValue().equals("Decorative Plant")) {
					changedPlant = new DecorativePlant();
				} else if (typeChoices.getValue().equals("Carnivorous Plant")) {
					changedPlant = new CarnivorousPlant();
				}
				// Sets values except ID
				setChanged(changedPlant, originalPlant, columnUpdates, changedValues);
				// Set ID
				changedPlant.setId(originalPlant.getId());
			} else { // Otherwise update the current plant
				// Check for change in name
				if (!nameField.getText().equals(originalPlant.getName())) {
					columnUpdates.add("plantName = ?");
					changedValues.add(nameField.getText());
					originalPlant.setName(nameField.getText());
				}
				// Check for change in species
				if (!speciesField.getText().equals(originalPlant.getSpecies())) {
					columnUpdates.add("species = ?");
					changedValues.add(speciesField.getText());
					originalPlant.setSpecies(speciesField.getText());
				}
				// If plant has type specific, check if the type specific field has been changed
				if (hasTypeSpecific(originalPlant.getType())) {
					if (originalPlant instanceof CarnivorousPlant
							&& !((CarnivorousPlant) originalPlant).getFoodType().equals(typeSpecificField.getText())) {
						columnUpdates.add("foodType = ?");
						changedValues.add(typeSpecificField.getText());
						((CarnivorousPlant) originalPlant).setFoodType(typeSpecificField.getText());
					} else if (originalPlant instanceof FruitingPlant
							&& !((FruitingPlant) originalPlant).getFruit().equals(typeSpecificField.getText())) {
						columnUpdates.add("fruit = ?");
						changedValues.add(typeSpecificField.getText());
						((FruitingPlant) originalPlant).setFruit(typeSpecificField.getText());
					} else if (originalPlant instanceof Vegetable
							&& !((Vegetable) originalPlant).getVegetable().equals(typeSpecificField.getText())) {
						columnUpdates.add("vegetable = ?");
						changedValues.add(typeSpecificField.getText());
						((Vegetable) originalPlant).setVegetable(typeSpecificField.getText());
					}
				}
				// If the sunlight need is specified, set as the selected and other options as
				// false
				if (sunlightChoices.getValue() != null) {
					if (sunlightChoices.getValue().equals("Full sun")) {
						columnUpdates.add("isFullSun = ?");
						changedValues.add(true);
						originalPlant.setFullSun(true);
						columnUpdates.add("isPartSun = ?");
						changedValues.add(false);
						originalPlant.setPartSun(false);
						columnUpdates.add("isShade = ?");
						changedValues.add(false);
						originalPlant.setShade(false);
					} else if (sunlightChoices.getValue().equals("Part sun")) {
						columnUpdates.add("isPartSun = ?");
						changedValues.add(true);
						originalPlant.setPartSun(true);
						columnUpdates.add("isFullSun = ?");
						changedValues.add(false);
						originalPlant.setFullSun(false);
						columnUpdates.add("isShade = ?");
						changedValues.add(false);
						originalPlant.setShade(false);
					} else if (sunlightChoices.getValue().equals("Shade")) {
						columnUpdates.add("isShade = ?");
						changedValues.add(true);
						originalPlant.setShade(true);
						columnUpdates.add("isFullSun = ?");
						changedValues.add(false);
						originalPlant.setFullSun(false);
						columnUpdates.add("isPartSun = ?");
						changedValues.add(false);
						originalPlant.setPartSun(false);
					}
				}
				// If a season is selected but the original plant could not be outdoors
				if ((spring.isSelected() || summer.isSelected() || fall.isSelected() || winter.isSelected())
						&& !originalPlant.isCanBeOutdoors()) {
					columnUpdates.add("canBeOutdoors = ?");
					changedValues.add(true);
					originalPlant.setCanBeOutdoors(true);
				}
				// If the plant can be outdoors,
				if (originalPlant.isCanBeOutdoors()) {
					// Check for change in spring outdoor capability
					if (spring.isSelected() != originalPlant.isSpring()) {
						columnUpdates.add("spring = ?");
						changedValues.add(spring.isSelected());
						originalPlant.setSpring(spring.isSelected());
					}
					// Check for change in summer outdoor capability
					if (summer.isSelected() != originalPlant.isSummer()) {
						columnUpdates.add("summer = ?");
						changedValues.add(summer.isSelected());
						originalPlant.setSummer(summer.isSelected());
					}
					// Check for change in fall outdoor capability
					if (fall.isSelected() != originalPlant.isFall()) {
						columnUpdates.add("fall = ?");
						changedValues.add(fall.isSelected());
						originalPlant.setFall(fall.isSelected());
					}
					// Check for change in winter outdoor capability
					if (winter.isSelected() != originalPlant.isWinter()) {
						columnUpdates.add("winter = ?");
						changedValues.add(winter.isSelected());
						originalPlant.setWinter(winter.isSelected());
					}
				}
				// If no season is selected but the original plant could be outdoors
				if (!spring.isSelected() && !summer.isSelected() && !fall.isSelected() && !winter.isSelected()
						&& originalPlant.isCanBeOutdoors()) {
					columnUpdates.add("canBeOutdoors = ?");
					changedValues.add(false);
					originalPlant.setCanBeOutdoors(false);
				}
				// Check for change in date
				java.sql.Date d = new java.sql.Date(originalPlant.getDatePlanted().getTime());
				LocalDate ld = d.toLocalDate();
				System.out.println(ld.toString());
				if (!datePlantedPicker.getValue().isEqual(ld)) {
					java.sql.Date date = java.sql.Date.valueOf(datePlantedPicker.getValue());
					columnUpdates.add("datePlanted = ?");
					changedValues.add(datePlantedPicker.getValue());
					originalPlant.setDatePlanted(date);
				}
			}
			// If there were changes
			if (!changedValues.isEmpty()) {
				// Update database
				String sql = "UPDATE plant SET ";
				sql += String.join(", ", columnUpdates) + " ";
				sql += "WHERE plantId = ?";
				System.out.println(sql);
				System.out.println(changedValues);

				DbHelper dbHelper = new DbHelper();
				Connection connection = dbHelper.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(sql);
				for (int i = 0; i < columnUpdates.size(); i++) {
					preparedStatement.setObject(i + 1, changedValues.get(i));
				}
				preparedStatement.setInt(columnUpdates.size() + 1, originalPlant.getId());
				preparedStatement.executeUpdate();

				// Update plantName in reminders if updates contain a name change
				if (columnUpdates.contains("plantName = ?")) {
					// Get and store all reminderId(s) where there is still the original name
					ArrayList<Integer> ids = new ArrayList<>();
					sql = "SELECT reminderId FROM reminder ";
					sql += "WHERE plantName = ?";
					preparedStatement = connection.prepareStatement(sql);
					preparedStatement.setString(1, originalName);
					ResultSet result = preparedStatement.executeQuery();
					while (result.next()) {
						ids.add(result.getInt("reminderId"));
					}
					// If an id was stored, go through reminderId(s) and set plant name as the one in the TextField
					if (!ids.isEmpty()) {
						// Setting up string for SQL IN operator
						StringBuilder s = new StringBuilder();
						for (int i = 0; i < ids.size(); i++) {
							s.append(ids.get(i));
							if (i < ids.size() - 1) {
								s.append(", ");
							}
						}
						String idSet = s.toString();
						sql = "UPDATE reminder SET ";
						sql += "plantName = ? ";
						sql += "WHERE reminderId IN (" + idSet + ")";
						System.out.println(sql);
						System.out.println(idSet);
						preparedStatement = connection.prepareStatement(sql);
						preparedStatement.setString(1, nameField.getText());
						preparedStatement.executeUpdate();
					}
				}

				dbHelper.closeConnection(connection);
				// If plant type was changed, replace the original plant with the changed plant
				// and have the TableView select the changed plant due to new instance
				if (typeChanged) {
					int selectedIndex = plantList.getSelectionModel().getSelectedIndex();
					data.set(selectedIndex, changedPlant);
					plantList.getSelectionModel().select(selectedIndex);
				}
				// Refresh list to reflect changes in TableView
				plantList.refresh();
				printSelectionStatus();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@FXML
	void closeEdit() {
		editModeOn = false;
		editBox.setVisible(false);
		editBox.setManaged(false);
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
				resetEditComponents();
				plantDAO.deletePlant(plantList.getSelectionModel().getSelectedItem().getName());
				data.remove(plantList.getSelectionModel().getSelectedItem());
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
			updateAdminButtonVisibility();
//			bottomBar.prefHeightProperty().bind(sceneLabel.heightProperty());
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
						&& !((FruitingPlant) cellData.getValue()).getFruit().equals(null)) {
					return new SimpleStringProperty(((FruitingPlant) cellData.getValue()).getFruit());
				} else if (cellData.getValue().getClass().getSimpleName().equals("Vegetable")
						&& !((Vegetable) cellData.getValue()).getVegetable().equals(null)) {
					return new SimpleStringProperty(((Vegetable) cellData.getValue()).getVegetable());
				} else if (cellData.getValue().getClass().getSimpleName().equals("CarnivorousPlant")
						&& !((CarnivorousPlant) cellData.getValue()).getFoodType().equals(null)) {
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

			FilteredList<Plant> filteredData = new FilteredList<>(data);
			searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
				filteredData.setPredicate(plant -> {
					if (newValue.isEmpty()) {
						return true;
					}

					return plant.getName().toLowerCase().contains(newValue.toLowerCase())
							|| plant.getSpecies().toLowerCase().contains(newValue.toLowerCase())
							|| plant.getType().toLowerCase().contains(newValue.toLowerCase());
				});
			});

			plantList.setItems(filteredData);

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
		System.out.println("****************************************");
		System.out.println("Edit Mode On: " + editModeOn);
		System.out.println("Selected Plant ID: " + plantList.getSelectionModel().getSelectedItem().getId());
		System.out.println("Selected Plant Name: " + plantList.getSelectionModel().getSelectedItem().getName());
		System.out.println("Selected Plant Species: " + plantList.getSelectionModel().getSelectedItem().getSpecies());
		System.out.println(
				"Selected Plant Type: " + plantList.getSelectionModel().getSelectedItem().getClass().getSimpleName());
		System.out.println(
				"Selected Plant Date Planted: " + plantList.getSelectionModel().getSelectedItem().getDatePlanted());
		System.out.println(
				"Selected Plant Outdoor Ability: " + plantList.getSelectionModel().getSelectedItem().isCanBeOutdoors());
		System.out.println(
				"Selected Plant Outdoor in Spring: " + plantList.getSelectionModel().getSelectedItem().isSpring());
		System.out.println(
				"Selected Plant Outdoor in Summer: " + plantList.getSelectionModel().getSelectedItem().isSummer());
		System.out
				.println("Selected Plant Outdoor in Fall: " + plantList.getSelectionModel().getSelectedItem().isFall());
		System.out.println(
				"Selected Plant Outdoor in Winter: " + plantList.getSelectionModel().getSelectedItem().isWinter());
		System.out.println("****************************************");
	}

	private boolean hasTypeSpecific(String type) {
		if (type.equals("Fruiting Plant") || type.equals("Vegetable") || type.equals("Carnivorous Plant")) {
			return true;
		}
		return false;
	}

	private void allowTypeSpecificGUI(String type) {
		if (type.equals("Fruiting Plant")) {
			typeSpecificLabel.setText("Fruit");
		} else if (type.equals("Vegetable")) {
			typeSpecificLabel.setText("Vegetable");
		} else if (type.equals("Carnivorous Plant")) {
			typeSpecificLabel.setText("Food Type");
		}
	}

	private void allowTypeSpecificGUI(boolean status) {
		typeSpecificLabel.setManaged(status);
		typeSpecificField.setManaged(status);
		typeSpecificLabel.setVisible(status);
		typeSpecificField.setVisible(status);
	}

	private void setChanged(Plant changedPlant, Plant originalPlant, ArrayList<String> columnUpdates,
			ArrayList<Object> changedValues) {
		// Update type in database
		columnUpdates.add("plantType = ?");
		changedValues.add(changedPlant.getType());

		// Check for change in name
		if (!nameField.getText().equals(changedPlant.getName())) {
			columnUpdates.add("plantName = ?");
			changedValues.add(nameField.getText());
			changedPlant.setName(nameField.getText());
		}
		// Check for change in species
		if (!speciesField.getText().equals(changedPlant.getSpecies())) {
			columnUpdates.add("species = ?");
			changedValues.add(speciesField.getText());
			changedPlant.setSpecies(speciesField.getText());
		}
		// If plant has type specific,
		if (hasTypeSpecific(changedPlant.getType())) {
			// Set original type specific as null
			if (originalPlant instanceof CarnivorousPlant) {
				columnUpdates.add("foodType = ?");
				changedValues.add(null);
			} else if (originalPlant instanceof FruitingPlant) {
				columnUpdates.add("fruit = ?");
				changedValues.add(null);
			} else if (originalPlant instanceof Vegetable) {
				columnUpdates.add("vegetable = ?");
				changedValues.add(null);
			}
			// Uh...
			if (changedPlant instanceof CarnivorousPlant
					&& !((CarnivorousPlant) changedPlant).getFoodType().equals(typeSpecificField.getText())) {
				columnUpdates.add("foodType = ?");
				changedValues.add(typeSpecificField.getText());
				((CarnivorousPlant) changedPlant).setFoodType(typeSpecificField.getText());
			} else if (changedPlant instanceof FruitingPlant
					&& !((FruitingPlant) changedPlant).getFruit().equals(typeSpecificField.getText())) {
				columnUpdates.add("fruit = ?");
				changedValues.add(typeSpecificField.getText());
				((FruitingPlant) changedPlant).setFruit(typeSpecificField.getText());
			} else if (changedPlant instanceof Vegetable
					&& !((Vegetable) changedPlant).getVegetable().equals(typeSpecificField.getText())) {
				columnUpdates.add("vegetable = ?");
				changedValues.add(typeSpecificField.getText());
				((Vegetable) changedPlant).setVegetable(typeSpecificField.getText());
			}
		}
		// If the sunlight need is specified, set as the selected and other options as
		// false
		if (sunlightChoices.getValue() != null) {
			if (sunlightChoices.getValue().equals("Full sun")) {
				columnUpdates.add("isFullSun = ?");
				changedValues.add(true);
				changedPlant.setFullSun(true);
				columnUpdates.add("isPartSun = ?");
				changedValues.add(false);
				changedPlant.setPartSun(false);
				columnUpdates.add("isShade = ?");
				changedValues.add(false);
				changedPlant.setShade(false);
			} else if (sunlightChoices.getValue().equals("Part sun")) {
				columnUpdates.add("isPartSun = ?");
				changedValues.add(true);
				changedPlant.setPartSun(true);
				columnUpdates.add("isFullSun = ?");
				changedValues.add(false);
				changedPlant.setFullSun(false);
				columnUpdates.add("isShade = ?");
				changedValues.add(false);
				changedPlant.setShade(false);
			} else if (sunlightChoices.getValue().equals("Shade")) {
				columnUpdates.add("isShade = ?");
				changedValues.add(true);
				changedPlant.setShade(true);
				columnUpdates.add("isFullSun = ?");
				changedValues.add(false);
				changedPlant.setFullSun(false);
				columnUpdates.add("isPartSun = ?");
				changedValues.add(false);
				changedPlant.setPartSun(false);
			}
		}
		// If a season is selected but the original plant could not be outdoors
		if ((spring.isSelected() || summer.isSelected() || fall.isSelected() || winter.isSelected())
				&& !changedPlant.isCanBeOutdoors()) {
			columnUpdates.add("canBeOutdoors = ?");
			changedValues.add(true);
			changedPlant.setCanBeOutdoors(true);
		}
		// If the plant can be outdoors,
		if (changedPlant.isCanBeOutdoors()) {
			// Check for change in spring outdoor capability
			if (spring.isSelected() != changedPlant.isSpring()) {
				columnUpdates.add("spring = ?");
				changedValues.add(spring.isSelected());
				changedPlant.setSpring(spring.isSelected());
			}
			// Check for change in summer outdoor capability
			if (summer.isSelected() != changedPlant.isSummer()) {
				columnUpdates.add("summer = ?");
				changedValues.add(summer.isSelected());
				changedPlant.setSummer(summer.isSelected());
			}
			// Check for change in fall outdoor capability
			if (fall.isSelected() != changedPlant.isFall()) {
				columnUpdates.add("fall = ?");
				changedValues.add(fall.isSelected());
				changedPlant.setFall(fall.isSelected());
			}
			// Check for change in winter outdoor capability
			if (winter.isSelected() != changedPlant.isWinter()) {
				columnUpdates.add("winter = ?");
				changedValues.add(winter.isSelected());
				changedPlant.setWinter(winter.isSelected());
			}
		}
		// If no season is selected but the original plant could be outdoors
		if (!spring.isSelected() && !summer.isSelected() && !fall.isSelected() && !winter.isSelected()
				&& changedPlant.isCanBeOutdoors()) {
			columnUpdates.add("canBeOutdoors = ?");
			changedValues.add(false);
			changedPlant.setCanBeOutdoors(false);
		}
		// Check for change in date
		java.sql.Date d = new java.sql.Date(changedPlant.getDatePlanted().getTime());
		LocalDate ld = d.toLocalDate();
		System.out.println(ld.toString());
		if (!datePlantedPicker.getValue().isEqual(ld)) {
			java.sql.Date date = java.sql.Date.valueOf(datePlantedPicker.getValue());
			columnUpdates.add("datePlanted = ?");
			changedValues.add(datePlantedPicker.getValue());
			changedPlant.setDatePlanted(date);
		}
	}

	private void resetEditComponents() {
		typeSpecificLabel.setManaged(true);
		typeSpecificField.setManaged(true);
		typeSpecificLabel.setVisible(true);
		typeSpecificField.setVisible(true);
		if (editModeOn) {
			typeChoices.valueProperty().removeListener(typeLisener);
		}
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
