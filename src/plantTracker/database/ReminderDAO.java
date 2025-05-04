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
		String sql = "INSERT INTO reminder (plantName, reminderType, firstDueDate, currentDueDate, nextDueDate, details, recurring, intervals, waterAmountInMl, fertilizerType, fertalizerAmount, newPotSize, soilType, newLocation, reason, harvestPart, useFor, complete) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

		preparedStatement.setString(1, reminder.getPlantName());
		preparedStatement.setString(2, getReminderTypeString(reminder));
		preparedStatement.setDate(3, java.sql.Date.valueOf(reminder.getFirstDueDate()));
		preparedStatement.setDate(4, java.sql.Date.valueOf(reminder.getCurrentDueDate()));
		preparedStatement.setDate(5,
				reminder.getNextDueDate() != null ? java.sql.Date.valueOf(reminder.getNextDueDate()) : null);
		preparedStatement.setString(6, reminder.getDescription());
		preparedStatement.setBoolean(7, reminder.isRecurring());
		preparedStatement.setInt(8, reminder.getIntervals());
		preparedStatement.setInt(9, reminder instanceof WaterReminder ? ((WaterReminder) reminder).getAmountInMl() : 0);
		preparedStatement.setString(10,
				reminder instanceof FertilizeReminder ? ((FertilizeReminder) reminder).getFertilizerType() : null);
		preparedStatement.setInt(11,
				reminder instanceof FertilizeReminder ? ((FertilizeReminder) reminder).getAmount() : 0);
		preparedStatement.setString(12,
				reminder instanceof RepotReminder ? ((RepotReminder) reminder).getNewPotSize() : null);
		preparedStatement.setString(13,
				reminder instanceof RepotReminder ? ((RepotReminder) reminder).getSoilType() : null);
		preparedStatement.setString(14,
				reminder instanceof MoveReminder ? ((MoveReminder) reminder).getNewLocation() : null);
		preparedStatement.setString(15,
				reminder instanceof MoveReminder ? ((MoveReminder) reminder).getReason() : null);
		preparedStatement.setString(16,
				reminder instanceof HarvestReminder ? ((HarvestReminder) reminder).getHarvestPart() : null);
		preparedStatement.setString(17,
				reminder instanceof HarvestReminder ? ((HarvestReminder) reminder).getUseFor() : null);
		preparedStatement.setBoolean(18, false);
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
		String sql = "UPDATE reminders WHERE reminderId = ? SET (currentDueDate, nextDueDate, details, recurring, intervals, waterAmountInMl, fertilizerType, fertalizerAmount, newPotSize, soilType, newLocation, reason, harvestPart, useFor, complete VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		Connection connection = dbHelper.getConnection();
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		preparedStatement.setInt(1, reminderId);
		preparedStatement.setDate(2, java.sql.Date.valueOf(reminder.getCurrentDueDate()));
		preparedStatement.setDate(3,
				reminder.getNextDueDate() != null ? java.sql.Date.valueOf(reminder.getNextDueDate()) : null);
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
		PreparedStatement deleteSingleStmt = null;
		PreparedStatement deleteAllRecurringStmt = null;

		try {
			connection = dbHelper.getConnection();

			// Get the plantName and reminderType of the reminder being deleted
			String selectSql = "SELECT plantName, reminderType, recurring FROM reminder WHERE reminderId = ?";
			PreparedStatement selectStmt = connection.prepareStatement(selectSql);
			selectStmt.setInt(1, reminderId);
			ResultSet resultSet = selectStmt.executeQuery();

			boolean isRecurring = false;
			String plantNameToDelete = null;
			String reminderTypeToDelete = null;
			if (resultSet.next()) {
				isRecurring = resultSet.getBoolean("recurring");
				plantNameToDelete = resultSet.getString("plantName");
				reminderTypeToDelete = resultSet.getString("reminderType");
			}

			if (isRecurring && plantNameToDelete != null && reminderTypeToDelete != null) {
				// Delete all reminders with the same plantName and reminderType (you might need
				// a more specific link)
				String deleteAllSql = "DELETE FROM reminder WHERE plantName = ? AND reminderType = ?";
				deleteAllRecurringStmt = connection.prepareStatement(deleteAllSql);
				deleteAllRecurringStmt.setString(1, plantNameToDelete);
				deleteAllRecurringStmt.setString(2, reminderTypeToDelete);
				int recurringRowsAffected = deleteAllRecurringStmt.executeUpdate();
				System.out.println("Deleted " + recurringRowsAffected + " recurring reminders for plant '"
						+ plantNameToDelete + "' and type '" + reminderTypeToDelete + "'.");
			} else {
				// Delete only the single reminder
				String deleteSingleSql = "DELETE FROM Reminder WHERE reminderId = ?";
				deleteSingleStmt = connection.prepareStatement(deleteSingleSql);
				deleteSingleStmt.setInt(1, reminderId);
				int singleRowsAffected = deleteSingleStmt.executeUpdate();
				System.out.println("Deleted reminder with ID " + reminderId + ".");
			}

		} finally {
			if (deleteSingleStmt != null)
				try {
					deleteSingleStmt.close();
				} catch (SQLException e) {
				}
			if (deleteAllRecurringStmt != null)
				try {
					deleteAllRecurringStmt.close();
				} catch (SQLException e) {
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
				java.sql.Date sqlcurrentDueDate = results.getDate("currentDueDate");
				LocalDate currentDueDate = sqlcurrentDueDate.toLocalDate();
				java.sql.Date sqlnextDueDate = results.getDate("nextDueDate");
				LocalDate nextDueDate = null;
				if (sqlnextDueDate != null) {
					nextDueDate = sqlnextDueDate.toLocalDate();
				}
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
					reminder = new WaterReminder(plantName, currentDueDate, recurring, intervals,
							waterAmountInMl != null ? waterAmountInMl : 0);
					break;
				case "Fertilize Reminder":
					reminder = new FertilizeReminder(plantName, currentDueDate, recurring, intervals, fertilizerType,
							fertalizerAmount != null ? fertalizerAmount : 0);
					break;
				case "RepotReminder":
					reminder = new RepotReminder(plantName, currentDueDate, recurring, intervals, newPotSize, soilType);
					break;
				case "Move Reminder":
					reminder = new MoveReminder(plantName, currentDueDate, recurring, intervals, newLocation, reason);
					break;
				case "Harvest Reminder":
					reminder = new HarvestReminder(plantName, currentDueDate, recurring, intervals, harvestPart,
							useFor);
					break;
				}
				if (reminder != null) {
					if (recurring) {
						reminder.setNextDueDate(nextDueDate);
					}
					reminder.setReminderId(reminderId);
					reminders.add(reminder);
				}
			}
		} finally {
			dbHelper.closeConnection(connection);
		}
		return reminders;
	}

	private Reminder createReminderFromResultSet(ResultSet results) throws SQLException {
		int reminderId = results.getInt("reminderId");
		String plantName = results.getString("plantName");
		String reminderType = results.getString("reminderType");
		java.sql.Date sqlcurrentDueDate = results.getDate("currentDueDate");
		LocalDate currentDueDate = sqlcurrentDueDate.toLocalDate();
		java.sql.Date sqlnextDueDate = results.getDate("nextDueDate");
		LocalDate nextDueDate = null;
		if (sqlnextDueDate != null) {
			nextDueDate = sqlnextDueDate.toLocalDate();
		}
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
			reminder = new WaterReminder(plantName, currentDueDate, recurring, intervals,
					waterAmountInMl != null ? waterAmountInMl : 0);
			break;
		case "Fertilize Reminder":
			reminder = new FertilizeReminder(plantName, currentDueDate, recurring, intervals, fertilizerType,
					fertalizerAmount != null ? fertalizerAmount : 0);
			break;
		case "RepotReminder":
			reminder = new RepotReminder(plantName, currentDueDate, recurring, intervals, newPotSize, soilType);
			break;
		case "Move Reminder":
			reminder = new MoveReminder(plantName, currentDueDate, recurring, intervals, newLocation, reason);
			break;
		case "Harvest Reminder":
			reminder = new HarvestReminder(plantName, currentDueDate, recurring, intervals, harvestPart, useFor);
			break;
		}
		if (reminder != null) {
			reminder.setReminderId(reminderId);
			reminder.setComplete(complete);
			if (recurring) {
				reminder.setNextDueDate(nextDueDate);
			}
		}
		return reminder;
	}

	public void advanceRecurringReminders() throws SQLException {
		Connection connection = dbHelper.getConnection();
		LocalDate now = LocalDate.now(ZoneId.systemDefault());
		String sql = "UPDATE reminder SET currentDueDate = nextDueDate, nextDueDate = DATE_ADD(nextDueDate, INTERVAL intervals DAY), complete = FALSE WHERE recurring = TRUE AND complete = TRUE AND nextDueDate <= ?";
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		preparedStatement.setDate(1, Date.valueOf(now));
		preparedStatement.executeUpdate();
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

	public List<Reminder> getPlantReminders(String plantName) throws SQLException {
		ObservableList<Reminder> reminders = FXCollections.observableArrayList();
		String sql = "SELECT * FROM reminder WHERE plantName = ? ORDER BY nextDueDate ASC"; // Order by next due date

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet results = null;

		try {
			connection = dbHelper.getConnection();
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, plantName);
			results = preparedStatement.executeQuery();

			while (results.next()) {
				Reminder reminder = createReminderFromResultSet(results);
				if (reminder != null) {
					reminders.add(reminder);
				}
			}
		} finally {
			// Close resources
			if (results != null)
				try {
					results.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			if (preparedStatement != null)
				try {
					preparedStatement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			dbHelper.closeConnection(connection);
		}
		return reminders;
	}

	public List<Reminder> getIncompleteReminders() throws SQLException {
		ObservableList<Reminder> reminders = FXCollections.observableArrayList();
		String sql = "SELECT * FROM reminder WHERE complete = FALSE ORDER BY nextDueDate ASC"; // Order by next due date

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet results = null;

		try {
			connection = dbHelper.getConnection();
			preparedStatement = connection.prepareStatement(sql);
			results = preparedStatement.executeQuery();

			while (results.next()) {
				Reminder reminder = createReminderFromResultSet(results);
				if (reminder != null) {
					reminders.add(reminder);
				}
			}
		} finally {
			// Close resources
			if (results != null)
				try {
					results.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			if (preparedStatement != null)
				try {
					preparedStatement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			dbHelper.closeConnection(connection);
		}
		return reminders;
	}

	public List<Reminder> getUpcomingIncompleteReminders(LocalDate startDate, LocalDate endDate) throws SQLException {
		List<Reminder> reminders = new ArrayList<>();
		String sqlUpcoming = "SELECT * FROM reminder WHERE nextDueDate >= ? AND nextDueDate <= ? AND complete = FALSE ORDER BY nextDueDate ASC";

		Connection connection = null;
		PreparedStatement upcomingStmt = null;
		ResultSet upcomingResults = null;

		try {
			connection = dbHelper.getConnection();
			upcomingStmt = connection.prepareStatement(sqlUpcoming);
			upcomingStmt.setDate(1, Date.valueOf(startDate));
			upcomingStmt.setDate(2, Date.valueOf(endDate));
			upcomingResults = upcomingStmt.executeQuery();
			while (upcomingResults.next()) {
				Reminder reminder = createReminderFromResultSet(upcomingResults);
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
			dbHelper.closeConnection(connection);
		}
		return reminders;
	}

	public List<Reminder> getRecentIncompleteReminders(LocalDate endDate, int limit) throws SQLException {
		List<Reminder> reminders = new ArrayList<>();
		String sqlRecent = "SELECT * FROM reminder WHERE date < ? AND complete = FALSE ORDER BY date DESC LIMIT ?";

		Connection connection = null;
		PreparedStatement recentStmt = null;
		ResultSet recentResults = null;

		try {
			connection = dbHelper.getConnection();
			recentStmt = connection.prepareStatement(sqlRecent);
			recentStmt.setDate(1, Date.valueOf(endDate));
			recentStmt.setInt(2, limit);
			recentResults = recentStmt.executeQuery();
			while (recentResults.next()) {
				Reminder reminder = createReminderFromResultSet(recentResults);
				if (reminder != null) {
					reminders.add(reminder);
				}
			}
		} finally {
			// Close resources
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

	public List<Reminder> getRecentCompleteReminders(LocalDate endDate, int limit) throws SQLException {
		List<Reminder> reminders = new ArrayList<>();
		String sqlRecent = "SELECT * FROM reminder WHERE date < ? AND complete = TRUE ORDER BY date DESC LIMIT ?";

		Connection connection = null;
		PreparedStatement recentStmt = null;
		ResultSet recentResults = null;

		try {
			connection = dbHelper.getConnection();
			recentStmt = connection.prepareStatement(sqlRecent);
			recentStmt.setDate(1, Date.valueOf(endDate));
			recentStmt.setInt(2, limit);
			recentResults = recentStmt.executeQuery();
			while (recentResults.next()) {
				Reminder reminder = createReminderFromResultSet(recentResults);
				if (reminder != null) {
					reminders.add(reminder);
				}
			}
		} finally {
			// Close resources
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

	public static Reminder getSelectedReminder() {
		return selectedReminder;
	}

	public static void setSelectedReminder(Reminder selectedReminder) {
		ReminderDAO.selectedReminder = selectedReminder;
	}

	public static void clearSelectedReminder() {
		ReminderDAO.selectedReminder = null;
	}
}
