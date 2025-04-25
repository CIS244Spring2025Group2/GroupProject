package plantTracker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import database.DbHelper;

public class ReminderDAO {
	public void addReminder(Reminder reminder) throws SQLException {
		String sql = "INSERT INTO reminders (plantName, reminderType, date, details, recurring, intervals)VALUES (?, ?, ?, ?)";
		try (Connection conn = DbHelper.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, reminder.getPlantName());
			stmt.setString(2, reminder.getType());
			stmt.setString(3, reminder.getDate());
			stmt.setString(4, reminder.getDescription());
			stmt.executeUpdate();
		}
	}
}
