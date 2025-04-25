package database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

public class DatabaseInitializer {

	private DbHelper dbHelper;

	public DatabaseInitializer() {
		this.dbHelper = new DbHelper();
	}

	public void initializeDatabase() {
		Connection connection = null;
		Statement statement = null;

		List<String> createTableStatements = Arrays.asList(
				"CREATE TABLE IF NOT EXISTS User (" + "id INT AUTO_INCREMENT PRIMARY KEY," + "firstName VARCHAR(225),"
						+ "lastName VARCHAR(255)," + "email VARCHAR(255)," + "password VARCHAR(255),"
						+ "timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP" + ")",
				"CREATE TABLE IF NOT EXISTS Plant (" + "plantId INT AUTO_INCREMENT PRIMARY KEY,"
						+ "plantType VARCHAR(225)," + "plantName VARCHAR(225)," + "datePlanted DATE,"
						+ "species VARCHAR(225)," + "canBeOutdoors BOOLEAN," + "winter BOOLEAN," + "spring BOOLEAN,"
						+ "summer BOOLEAN," + "fall BOOLEAN," + "isFullSun BOOLEAN," + "isPartSun BOOLEAN,"
						+ "isShade BOOLEAN," + "fruit VARCHAR(225)," + "vegetable VARCHAR(225),"
						+ "foodType VARCHAR(225)," + "watered VARCHAR(225),"
						+ "timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP" + ")",
				"CREATE TABLE IF NOT EXISTS Watered (" + "id INT AUTO_INCREMENT PRIMARY KEY," + "plantId VARCHAR(225),"
						+ "timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP" + ")",
				"CREATE TABLE IF NOT EXISTS Reminder (" + "reminderId INT AUTO_INCREMENT PRIMARY KEY,"
						+ "plantName VARCHAR(225)," + "reminderType VARCHAR(225)," + "date VARCHAR(225),"
						+ "details VARCHAR(225)," + "recurring VARCHAR(225)," + "intervals VARCHAR(225),"
						+ "timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP" + ")");

		try {
			connection = dbHelper.getConnection();
			statement = connection.createStatement();

			for (String sql : createTableStatements) {
				statement.executeUpdate(sql);
				System.out.println("Table created or already exists: " + sql.substring(26, sql.indexOf('('))); // Simple
																												// logging
			}

			System.out.println("Database initialization complete.");

		} catch (SQLException e) {
			System.err.println("Error initializing database: " + e.getMessage());
			e.printStackTrace();
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			dbHelper.closeConnection(connection);
		}
	}
}
