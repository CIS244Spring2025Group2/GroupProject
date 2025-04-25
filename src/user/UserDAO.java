package user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import database.DbHelper;
import database.ProjUtil;

public class UserDAO {

	private DbHelper dbHelper = new DbHelper();

	// Methods for adding, retrieving, updating, deleting users
	public void addUser(User user) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		String sql = "INSERT INTO users (username, password, ...) VALUES (?, ?, ...)";
		// ... (rest of the addUser implementation using dbHelper.getConnection())
	}

	public User getUser(String username) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		String sql = "SELECT * FROM users WHERE username = ?";
		// ... (rest of the getUser implementation using dbHelper.getConnection())
		return null; // Placeholder
	}

	// ... other user-related database operations
	public void createDefaultAdminUser() {
		String adminUsername = ProjUtil.getProperty("default.admin.email");
		String adminPassword = ProjUtil.getProperty("default.admin.password");

		if (adminUsername != null && adminPassword != null && !adminUsername.isEmpty() && !adminPassword.isEmpty()) {
			try (Connection conn = dbHelper.getConnection()) {
				if (!adminUserExists(conn, adminUsername)) {
					String hashedPassword = ProjUtil.getSHA(adminPassword);
					String insertAdminSQL = "INSERT INTO User (firstName, lastName, email, password) VALUES ('Admin', 'User', ?, ?)";
					try (PreparedStatement pstmt = conn.prepareStatement(insertAdminSQL)) {
						pstmt.setString(1, adminUsername);
						pstmt.setString(2, hashedPassword);
						pstmt.executeUpdate();
						System.out.println("Default admin user created: " + adminUsername);
					} catch (SQLException e) {
						System.err.println("Error creating default admin user: " + e.getMessage());
						e.printStackTrace();
					}
				} else {
					System.out.println("Default admin user already exists: " + adminUsername);
				}
			} catch (SQLException e) {
				System.err.println("Error connecting to database to create admin user: " + e.getMessage());
				e.printStackTrace();
			}
		} else {
			System.out.println("Admin username or password not configured in config.properties.");
		}
	}

	private boolean adminUserExists(Connection conn, String username) throws SQLException {
		String checkSQL = "SELECT COUNT(*) FROM User WHERE email = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(checkSQL)) {
			pstmt.setString(1, username);
			ResultSet rs = pstmt.executeQuery();
			return rs.next() && rs.getInt(1) > 0;
		}
	}
}