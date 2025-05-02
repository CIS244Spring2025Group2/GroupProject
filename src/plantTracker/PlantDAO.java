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

	// Methods for adding, retrieving, updating, deleting plants
	public void addPlant(Plant plant) throws SQLException, PlantAlreadyExistsException {
		Connection connection = null;
		PreparedStatement checkStatement = null;
		PreparedStatement preparedStatement = null;

		try {
			connection = dbHelper.getConnection();

			// 1. Check if the plantName already exists
			String checkSql = "SELECT COUNT(*) FROM Plant WHERE plantName = ?";
			checkStatement = connection.prepareStatement(checkSql);
			checkStatement.setString(1, plant.getName());
			ResultSet resultSet = checkStatement.executeQuery();

			if (resultSet.next() && resultSet.getInt(1) > 0) {
				throw new PlantAlreadyExistsException("Plant with name '" + plant.getName() + "' already exists.");
			}

			// 2. If the plantName doesn't exist, proceed with insertion
			String sql = "INSERT INTO Plant (plantType, plantName, datePlanted, species, canBeOutdoors, winter, spring, summer, fall, isFullSun, isPartSun, isShade, fruit, vegetable, foodType, watered) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
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
	
	public void updatePlant(Plant plant, ObservableList<Plant> data) throws SQLException {
//		for (Plant obj: data) {
//			if (obj.equals(obj))
//		}
	}

	public void deletePlant(String plantName) throws SQLException {
		String sql = "DELETE FROM plant WHERE plantName = ?";
		Connection connection = dbHelper.getConnection();
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		preparedStatement.setString(1, plantName);
		preparedStatement.executeUpdate();
		
		sql = "DELETE FROM reminder WHERE plantName = ?";
		preparedStatement = connection.prepareStatement(sql);
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

		while (results.next()) {
			data.add(results.getString("plantName"));
		}

		dbHelper.closeConnection(connection);
	}

	public void populateWithPlants(ObservableList<Plant> data) throws SQLException {
		String sql = "SELECT ";
		sql += "plantName, plantType, ";
		sql += "datePlanted, species, ";
		sql += "canBeOutdoors, winter, spring, summer, fall, ";
		sql += "isFullSun, isPartSun, isShade, ";
		sql += "fruit, vegetable, foodType ";
		sql += "FROM plant";
		
		Connection connection = dbHelper.getConnection();
		PreparedStatement preparedStatement;
		preparedStatement = connection.prepareStatement(sql);
		ResultSet results = preparedStatement.executeQuery();
		
		while (results.next()) {
			// For fruiting plants 
			if(results.getString("plantType").equals("Fruiting Plant")) {
				Plant p = new FruitingPlant(
						results.getString("plantName"),
						results.getString("species"),
						results.getDate("datePlanted"),
						results.getString("Fruit")
						);
				p.setCanBeOutdoors(results.getBoolean("canBeOutdoors"));
				p.setSpring(results.getBoolean("spring"));
				p.setSummer(results.getBoolean("summer"));
				p.setFall(results.getBoolean("fall"));
				p.setWinter(results.getBoolean("winter"));
				p.setFullSun(results.getBoolean("isFullSun"));
				p.setPartSun(results.getBoolean("isPartSun"));
				p.setShade(results.getBoolean("isShade"));
				data.add(p);
			}
			// For vegetables 
			if(results.getString("plantType").equals("Vegetable")) {
				Plant p = new Vegetable(
						results.getString("plantName"),
						results.getString("species"),
						results.getDate("datePlanted"),
						results.getString("vegetable")
						);
				p.setCanBeOutdoors(results.getBoolean("canBeOutdoors"));
				p.setSpring(results.getBoolean("spring"));
				p.setSummer(results.getBoolean("summer"));
				p.setFall(results.getBoolean("fall"));
				p.setWinter(results.getBoolean("winter"));
				p.setFullSun(results.getBoolean("isFullSun"));
				p.setPartSun(results.getBoolean("isPartSun"));
				p.setShade(results.getBoolean("isShade"));
				data.add(p);
			}
			// For carnivorous plants
			if(results.getString("plantType").equals("CarnivorousPlant")) {
				Plant p = new CarnivorousPlant(
						results.getString("plantName"),
						results.getString("species"),
						results.getDate("datePlanted"),
						results.getString("foodType")
						);
				p.setCanBeOutdoors(results.getBoolean("canBeOutdoors"));
				p.setSpring(results.getBoolean("spring"));
				p.setSummer(results.getBoolean("summer"));
				p.setFall(results.getBoolean("fall"));
				p.setWinter(results.getBoolean("winter"));
				p.setFullSun(results.getBoolean("isFullSun"));
				p.setPartSun(results.getBoolean("isPartSun"));
				p.setShade(results.getBoolean("isShade"));
				data.add(p);
			}
			// For flowering plants
			if(results.getString("plantType").equals("FloweringPlant")) {
				Plant p = new FloweringPlant(
						results.getString("plantName"),
						results.getString("species"),
						results.getDate("datePlanted")
						);
				p.setCanBeOutdoors(results.getBoolean("canBeOutdoors"));
				p.setSpring(results.getBoolean("spring"));
				p.setSummer(results.getBoolean("summer"));
				p.setFall(results.getBoolean("fall"));
				p.setWinter(results.getBoolean("winter"));
				p.setFullSun(results.getBoolean("isFullSun"));
				p.setPartSun(results.getBoolean("isPartSun"));
				p.setShade(results.getBoolean("isShade"));
				data.add(p);
			}
			// For herbs
			if(results.getString("plantType").equals("Herb")) {
				Plant p = new Herb(
						results.getString("plantName"),
						results.getString("species"),
						results.getDate("datePlanted")
						);
				p.setCanBeOutdoors(results.getBoolean("canBeOutdoors"));
				p.setSpring(results.getBoolean("spring"));
				p.setSummer(results.getBoolean("summer"));
				p.setFall(results.getBoolean("fall"));
				p.setWinter(results.getBoolean("winter"));
				p.setFullSun(results.getBoolean("isFullSun"));
				p.setPartSun(results.getBoolean("isPartSun"));
				p.setShade(results.getBoolean("isShade"));
				data.add(p);
			}
			// For decorative plants
			if(results.getString("plantType").equals("DecorativePlant")) {
				Plant p = new DecorativePlant(
						results.getString("plantName"),
						results.getString("species"),
						results.getDate("datePlanted")
						);
				p.setCanBeOutdoors(results.getBoolean("canBeOutdoors"));
				p.setSpring(results.getBoolean("spring"));
				p.setSummer(results.getBoolean("summer"));
				p.setFall(results.getBoolean("fall"));
				p.setWinter(results.getBoolean("winter"));
				p.setFullSun(results.getBoolean("isFullSun"));
				p.setPartSun(results.getBoolean("isPartSun"));
				p.setShade(results.getBoolean("isShade"));
				data.add(p);
			}
		}
		dbHelper.closeConnection(connection);
	}

	public static class PlantAlreadyExistsException extends Exception {
		public PlantAlreadyExistsException(String message) {
			super(message);
		}
	}
}
