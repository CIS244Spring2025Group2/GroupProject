package plantTracker;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import database.DbHelper;
import javafx.collections.ObservableList;

public class PlantDAO {

	private DbHelper dbHelper = new DbHelper();
	private static String selectedName;

	public void addPlant(Plant plant) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		String sql = "INSERT INTO Plant (plantType, plantName, datePlanted, species, canBeOutdoors, winter, spring, summer, fall, isFullSun, isPartSun, isShade, fruit, vegetable, foodType, watered) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try {
			connection = dbHelper.getConnection();
			preparedStatement = connection.prepareStatement(sql);

			preparedStatement.setString(1, getPlantTypeString(plant));
			preparedStatement.setString(2, plant.getName());
			preparedStatement.setDate(3, new Date(plant.getDatePlanted().getTime()));
			preparedStatement.setString(4, plant.getSpecies());
			preparedStatement.setBoolean(5, plant.isCanBeOutdoors());
			preparedStatement.setBoolean(6, plant.isWinter());
			preparedStatement.setBoolean(7, plant.isSpring());
			preparedStatement.setBoolean(8, plant.isSummer());
			preparedStatement.setBoolean(9, plant.isFall());
			preparedStatement.setBoolean(10, plant.isFullSun());
			preparedStatement.setBoolean(11, plant.isPartSun());
			preparedStatement.setBoolean(12, plant.isShade());
			preparedStatement.setString(13, plant instanceof FruitingPlant ? ((FruitingPlant) plant).getFruit() : null);
			preparedStatement.setString(14, plant instanceof Vegetable ? ((Vegetable) plant).getVegetable() : null);
			preparedStatement.setString(15,
					plant instanceof CarnivorousPlant ? ((CarnivorousPlant) plant).getFoodType() : null);
			preparedStatement.setString(16, String.valueOf(plant.isWatered()));

			preparedStatement.executeUpdate();

		} finally {
			if (preparedStatement != null) {
				preparedStatement.close();
			}
			dbHelper.closeConnection(connection);
		}
	}

	// Helper method to determine the plantType string for the database
	private String getPlantTypeString(Plant plant) {
		if (plant instanceof FruitingPlant) {
			return "Fruiting Plant";
		} else if (plant instanceof Vegetable) {
			return "Vegetable";
		} else if (plant instanceof CarnivorousPlant) {
			return "CarnivorousPlant";
		} else if (plant instanceof FloweringPlant) {
			return "FloweringPlant";
		} else if (plant instanceof Herb) {
			return "Herb";
		} else if (plant instanceof DecorativePlant) {
			return "DecorativePlant";
		} else {
			// Handle other plant types as needed
			return plant.getClass().getSimpleName(); // Or a more specific logic
		}
	}
	
	// public Plant getPlant(int plantId) throws SQLException { ... }
	
	 public void updatePlant(Plant plant) throws SQLException {
		 
	 }
	 
	 public void deletePlant(String plantName) throws SQLException {
		 String sql = "DELETE FROM plant WHERE plantName = ?";
		 Connection connection = dbHelper.getConnection();
		 PreparedStatement preparedStatement = connection.prepareStatement(sql);
		 preparedStatement.setString(1, plantName);
		 preparedStatement.executeUpdate();
		 dbHelper.closeConnection(connection);
	 }
	 
	 public void populateList(ObservableList<String> data) throws SQLException {
			String sql = "SELECT plantName FROM plant";
			Connection connection = dbHelper.getConnection();
			PreparedStatement preparedStatement;
			preparedStatement = connection.prepareStatement(sql);
			ResultSet results = preparedStatement.executeQuery();
				
			while(results.next()) {
				data.add(results.getString("plantName"));
			}
				
			dbHelper.closeConnection(connection);
		}

	public static String getSelectedName() {
		return selectedName;
	}

	public static void setSelectedName(String selectedName) {
		PlantDAO.selectedName = selectedName;
	}
}
