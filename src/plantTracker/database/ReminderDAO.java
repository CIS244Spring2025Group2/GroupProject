package plantTracker.database;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import database.DbHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import plantTracker.model.FertilizeReminder;
import plantTracker.model.HarvestReminder;
import plantTracker.model.MoveReminder;
import plantTracker.model.Reminder;
import plantTracker.model.RepotReminder;
import plantTracker.model.WaterReminder;

public class ReminderDAO {

	private DbHelper dbHelper = new DbHelper();
	private static Reminder selectedReminder;

	public int addReminder(Reminder reminder) throws SQLException {

		Connection connection = dbHelper.getConnection();
		String sql = "INSERT INTO reminder (plantName, reminderType, date, details, recurring, intervals, waterAmountInMl, fertilizerType, fertalizerAmount, newPotSize, soilType, newLocation, reason, harvestPart, useFor, complete) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS); // Request
																															// generated
																															// keys

		preparedStatement.setString(1, reminder.getPlantName());
		preparedStatement.setString(2, getReminderTypeString(reminder));
		preparedStatement.setDate(3, java.sql.Date.valueOf(reminder.getDate()));
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
		preparedStatement.setBoolean(16, false);
		preparedStatement.executeUpdate();

		ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
		int generatedId = 0;
		if (generatedKeys.next()) {
			generatedId = generatedKeys.getInt(1);
		}

		dbHelper.closeConnection(connection);
		return generatedId;
	}

	public void markReminderComplete(int reminderId) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		String sql = "UPDATE reminder SET complete = TRUE WHERE reminderId = ?";
		try {
			connection = dbHelper.getConnection();
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, reminderId);
			int rowsAffected = preparedStatement.executeUpdate();
			if (rowsAffected > 0) {
				System.out.println("Reminder with ID " + reminderId + " marked as complete.");
			} else {
				System.out.println("Reminder with ID " + reminderId + " not found.");
			}
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
	}

	public void updateReminder(Reminder reminder, int reminderId) throws SQLException {
		String sql = "UPDATE reminders WHERE reminderId = ? SET (date, details, recurring, intervals, waterAmountInMl, fertilizerType, fertalizerAmount, newPotSize, soilType, newLocation, reason, harvestPart, useFor, complete VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		Connection connection = dbHelper.getConnection();
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		preparedStatement.setInt(1, reminderId);
		preparedStatement.setDate(2, java.sql.Date.valueOf(reminder.getDate()));
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
		preparedStatement.setBoolean(15, false);
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
		String sql = "SELECT * FROM reminder WHERE complete = FALSE"; // Select all columns
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
				java.sql.Date sqlDate = results.getDate("date");
				LocalDate date = sqlDate.toLocalDate();
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

	public List<Reminder> getUpcomingAndRecentIncompleteReminders() throws SQLException {
		List<Reminder> reminders = new ArrayList<>();
		String sqlUpcoming = "SELECT * FROM reminder WHERE date >= ? AND complete = FALSE ORDER BY date ASC LIMIT 10";
		String sqlRecent = "SELECT * FROM reminder WHERE date < ? AND complete = FALSE ORDER BY date DESC LIMIT 5";

		Connection connection = null;
		PreparedStatement upcomingStmt = null;
		ResultSet upcomingResults = null;
		PreparedStatement recentStmt = null;
		ResultSet recentResults = null;

		try {
			connection = dbHelper.getConnection();
			java.sql.Date currentDate = java.sql.Date.valueOf(LocalDate.now(ZoneId.systemDefault()));

			// Fetch upcoming reminders
			upcomingStmt = connection.prepareStatement(sqlUpcoming);
			upcomingStmt.setDate(1, currentDate);
			upcomingResults = upcomingStmt.executeQuery();
			while (upcomingResults.next()) {
				Reminder reminder = createReminderFromResultSet(upcomingResults);
				if (reminder != null) {
					reminders.add(reminder);
				}
			}

			// Fetch recent past reminders
			recentStmt = connection.prepareStatement(sqlRecent);
			recentStmt.setDate(1, currentDate);
			recentResults = recentStmt.executeQuery();
			while (recentResults.next()) {
				Reminder reminder = createReminderFromResultSet(recentResults);
				if (reminder != null) {
					reminders.add(reminder);
				}
			}

		} finally {
			// Close resources
			if (upcomingResults != null)
				try {
					upcomingResults.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			if (upcomingStmt != null)
				try {
					upcomingStmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			if (recentResults != null)
				try {
					recentResults.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			if (recentStmt != null)
				try {
					recentStmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			dbHelper.closeConnection(connection);
		}
		return reminders;
	}

	private Reminder createReminderFromResultSet(ResultSet results) throws SQLException {
		int reminderId = results.getInt("reminderId");
		String plantName = results.getString("plantName");
		String reminderType = results.getString("reminderType");
		java.sql.Date sqlDate = results.getDate("date");
		LocalDate date = sqlDate.toLocalDate();
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
		boolean complete = results.getBoolean("complete");

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
			reminder.setReminderId(reminderId);
			reminder.setComplete(complete);
		}
		return reminder;
	}

	public void generateRecurringReminders() throws SQLException {
		Connection connection = dbHelper.getConnection();
		String sql = "SELECT * FROM reminder WHERE recurring = TRUE AND complete = FALSE ORDER BY date ASC";
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		ResultSet results = preparedStatement.executeQuery();

		LocalDate now = LocalDate.now(ZoneId.systemDefault());

		while (results.next()) {
			int reminderId = results.getInt("reminderId");
			String plantName = results.getString("plantName");
			String reminderType = results.getString("reminderType");
			LocalDate date = results.getDate("date").toLocalDate();
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
			boolean complete = results.getBoolean("complete");

			LocalDate nextOccurrence = date.plusDays(intervals);

			// Check if the next occurrence is in the future and hasn't been created yet
			if (nextOccurrence.isAfter(now) || nextOccurrence.isEqual(now)) {
				Reminder newReminder = null;
				switch (reminderType) {
				case "Water Reminder":
					newReminder = new WaterReminder(plantName, nextOccurrence, recurring, intervals,
							waterAmountInMl != null ? waterAmountInMl : 0);
					break;
				case "Fertilize Reminder":
					newReminder = new FertilizeReminder(plantName, nextOccurrence, recurring, intervals, fertilizerType,
							fertalizerAmount != null ? fertalizerAmount : 0);
					break;
				case "RepotReminder":
					newReminder = new RepotReminder(plantName, nextOccurrence, recurring, intervals, newPotSize,
							soilType);
					break;
				case "Move Reminder":
					newReminder = new MoveReminder(plantName, nextOccurrence, recurring, intervals, newLocation,
							reason);
					break;
				case "Harvest Reminder":
					newReminder = new HarvestReminder(plantName, nextOccurrence, recurring, intervals, harvestPart,
							useFor);
					break;
				}
				if (newReminder != null) {
					addReminder(newReminder);
				}
			}
		}
		dbHelper.closeConnection(connection);
	}

	public void updateReminderDate(int reminderId, LocalDate nextDate) throws SQLException {
		Connection connection = dbHelper.getConnection();
		String sql = "UPDATE reminder SET date = ? WHERE reminderId = ?";
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		preparedStatement.setDate(1, Date.valueOf(nextDate));
		preparedStatement.setInt(2, reminderId);
		preparedStatement.executeUpdate();
		dbHelper.closeConnection(connection);
	}

	public static Reminder getSelectedReminder() {
		return selectedReminder;
	}

	public static void setSelectedReminder(Reminder selectedReminder) {
		ReminderDAO.selectedReminder = selectedReminder;
	}
}
