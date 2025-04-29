package plantTracker;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import database.DbHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ReminderDAO {

	private DbHelper dbHelper = new DbHelper();
	private static Reminder selectedReminder;

	public void addReminder(Reminder reminder) throws SQLException {

		Connection connection = dbHelper.getConnection();
		String sql = "INSERT INTO reminder (plantName, reminderType, date, details, recurring, intervals, waterAmountInMl, fertilizerType, fertalizerAmount, newPotSize, soilType, newLocation, reason, harvestPart, useFor) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement preparedStatement = connection.prepareStatement(sql);

		preparedStatement.setString(1, reminder.getPlantName());
		preparedStatement.setString(2, getReminderTypeString(reminder));
		preparedStatement.setDate(3, new Date(reminder.getDate().getTime()));
		preparedStatement.setString(4, reminder.getDescription());
		preparedStatement.setBoolean(5, reminder.isRecurring());
		preparedStatement.setInt(6, reminder.getIntervals());
		preparedStatement.setInt(7, reminder instanceof WaterReminder ? ((WaterReminder) reminder).getAmountInMl() : 0);
		preparedStatement.setString(8,
				reminder instanceof FertilizeReminder ? ((FertilizeReminder) reminder).getFertilizerType() : null);
		preparedStatement.setInt(9,
				reminder instanceof FertilizeReminder ? ((FertilizeReminder) reminder).getAmount() : 0);
		preparedStatement.setString(10,
				reminder instanceof RepotReminder ? ((RepotReminder) reminder).getNewPotSize() : null);
		preparedStatement.setString(11,
				reminder instanceof RepotReminder ? ((RepotReminder) reminder).getSoilType() : null);
		preparedStatement.setString(12,
				reminder instanceof MoveReminder ? ((MoveReminder) reminder).getNewLocation() : null);
		preparedStatement.setString(13,
				reminder instanceof MoveReminder ? ((MoveReminder) reminder).getReason() : null);
		preparedStatement.setString(14,
				reminder instanceof HarvestReminder ? ((HarvestReminder) reminder).getHarvestPart() : null);
		preparedStatement.setString(15,
				reminder instanceof HarvestReminder ? ((HarvestReminder) reminder).getUseFor() : null);
		preparedStatement.executeUpdate();

		dbHelper.closeConnection(connection);

	}

	public void updateReminder(Reminder reminder, int reminderId) throws SQLException {
		String sql = "UPDATE reminders WHERE reminderId = ? SET (date, details, recurring, intervals, waterAmountInMl, fertilizerType, fertalizerAmount, newPotSize, soilType, newLocation, reason, harvestPart, useFor VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		Connection connection = dbHelper.getConnection();
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		preparedStatement.setInt(1, reminderId);
		preparedStatement.setDate(2, new Date(reminder.getDate().getTime()));
		preparedStatement.setString(3, reminder.getDescription());
		preparedStatement.setBoolean(4, reminder.isRecurring());
		preparedStatement.setInt(5, reminder.getIntervals());
		preparedStatement.setInt(6, reminder instanceof WaterReminder ? ((WaterReminder) reminder).getAmountInMl() : 0);
		preparedStatement.setString(7,
				reminder instanceof FertilizeReminder ? ((FertilizeReminder) reminder).getFertilizerType() : null);
		preparedStatement.setInt(8,
				reminder instanceof FertilizeReminder ? ((FertilizeReminder) reminder).getAmount() : 0);
		preparedStatement.setString(9,
				reminder instanceof RepotReminder ? ((RepotReminder) reminder).getNewPotSize() : null);
		preparedStatement.setString(10,
				reminder instanceof RepotReminder ? ((RepotReminder) reminder).getSoilType() : null);
		preparedStatement.setString(11,
				reminder instanceof MoveReminder ? ((MoveReminder) reminder).getNewLocation() : null);
		preparedStatement.setString(12,
				reminder instanceof MoveReminder ? ((MoveReminder) reminder).getReason() : null);
		preparedStatement.setString(13,
				reminder instanceof HarvestReminder ? ((HarvestReminder) reminder).getHarvestPart() : null);
		preparedStatement.setString(14,
				reminder instanceof HarvestReminder ? ((HarvestReminder) reminder).getUseFor() : null);
		preparedStatement.executeUpdate();
		dbHelper.closeConnection(connection);
	}

	// Helper method to determine the reminderType string for the database
	private String getReminderTypeString(Reminder reminder) {
		if (reminder instanceof HarvestReminder) {
			return "Harvest Reminder";
		} else if (reminder instanceof FertilizeReminder) {
			return "Fertilize Reminder";
		} else if (reminder instanceof MoveReminder) {
			return "Move Reminder";
		} else if (reminder instanceof RepotReminder) {
			return "RepotReminder";
		} else if (reminder instanceof WaterReminder) {
			return "Water Reminder";
		} else {
			return reminder.getClass().getSimpleName();
		}
	}

	// Delete a reminder by its ID
	public void deleteReminder(int reminderId) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		String sql = "DELETE FROM Reminder WHERE id = ?";
		try {
			connection = dbHelper.getConnection();
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, reminderId);
			preparedStatement.executeUpdate();
		} finally {
			if (preparedStatement != null) {
				preparedStatement.close();
			}
			dbHelper.closeConnection(connection);
		}
	}

	public ObservableList<Reminder> populateList() throws SQLException {
		ObservableList<Reminder> reminders = FXCollections.observableArrayList();
		String sql = "SELECT * FROM reminder"; // Select all columns
		Connection connection = dbHelper.getConnection();
		PreparedStatement preparedStatement = null;
		ResultSet results = null;

		try {
			preparedStatement = connection.prepareStatement(sql);
			results = preparedStatement.executeQuery();

			while (results.next()) {
				int reminderId = results.getInt("reminderId");
				String plantName = results.getString("plantName");
				String reminderType = results.getString("reminderType");
				Date date = results.getDate("date");
				String details = results.getString("details");
				boolean recurring = results.getBoolean("recurring");
				int intervals = results.getInt("intervals");
				Integer waterAmountInMl = results.getInt("waterAmountInMl");
				if (results.wasNull())
					waterAmountInMl = null;
				String fertilizerType = results.getString("fertilizerType");
				Integer fertalizerAmount = results.getInt("fertalizerAmount");
				if (results.wasNull())
					fertalizerAmount = null;
				String newPotSize = results.getString("newPotSize");
				String soilType = results.getString("soilType");
				String newLocation = results.getString("newLocation");
				String reason = results.getString("reason");
				String harvestPart = results.getString("harvestPart");
				String useFor = results.getString("useFor");

				Reminder reminder = null;
				switch (reminderType) {
				case "Water Reminder":
					reminder = new WaterReminder(plantName, date, recurring, intervals,
							waterAmountInMl != null ? waterAmountInMl : 0);
					break;
				case "Fertilize Reminder":
					reminder = new FertilizeReminder(plantName, date, recurring, intervals, fertilizerType,
							fertalizerAmount != null ? fertalizerAmount : 0);
					break;
				case "RepotReminder":
					reminder = new RepotReminder(plantName, date, recurring, intervals, newPotSize, soilType);
					break;
				case "Move Reminder":
					reminder = new MoveReminder(plantName, date, recurring, intervals, newLocation, reason);
					break;
				case "Harvest Reminder":
					reminder = new HarvestReminder(plantName, date, recurring, intervals, harvestPart, useFor);
					break;
				}
				if (reminder != null) {
					reminders.add(reminder);
				}
			}
		} finally {
			dbHelper.closeConnection(connection);
		}
		return reminders;
	}

	public static Reminder getSelectedReminder() {
		return selectedReminder;
	}

	public static void setSelectedReminder(Reminder selectedReminder) {
		ReminderDAO.selectedReminder = selectedReminder;
	}
}
