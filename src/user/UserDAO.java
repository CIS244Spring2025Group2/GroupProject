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
	public void addUser(User user) throws SQLException, UserAlreadyExistsException {
		Connection connection = null;
		PreparedStatement checkStatement = null;
		PreparedStatement insertStatement = null;

		try {
			connection = dbHelper.getConnection();

			// 1. Check if the username (email in this example) already exists
			String checkSql = "SELECT COUNT(*) FROM User WHERE email = ?";
			checkStatement = connection.prepareStatement(checkSql);
			checkStatement.setString(1, user.getEmail());
			ResultSet resultSet = checkStatement.executeQuery();

			if (resultSet.next() && resultSet.getInt(1) > 0) {
				throw new UserAlreadyExistsException("User with email '" + user.getEmail() + "' already exists.");
			}

			// 2. If the username doesn't exist, proceed with insertion
			String hashedPassword = ProjUtil.getSHA(user.getPassword());
			String insertSql = "INSERT INTO User (firstName, lastName, email, password) VALUES (?, ?, ?, ?)";
			insertStatement = connection.prepareStatement(insertSql);
			insertStatement.setString(1, user.getFirstName());
			insertStatement.setString(2, user.getLastName());
			insertStatement.setString(3, user.getEmail());
			insertStatement.setString(4, hashedPassword); // Assuming password is already hashed

			insertStatement.executeUpdate();

		} finally {
			if (checkStatement != null) {
				try {
					checkStatement.close();
				} catch (SQLException e) {
					e.printStackTrace(); // Log or handle error
				}
			}
			if (insertStatement != null) {
				try {
					insertStatement.close();
				} catch (SQLException e) {
					e.printStackTrace(); // Log or handle error
				}
			}
			dbHelper.closeConnection(connection);
		}
	}

	// Custom exception to indicate that a user already exists
	public static class UserAlreadyExistsException extends Exception {
		public UserAlreadyExistsException(String message) {
			super(message);
		}
	}

	public User getUser(String username) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		String sql = "SELECT * FROM users WHERE username = ?";
		// ... (rest of the getUser implementation using dbHelper.getConnection())
		return null; // Placeholder
	}

	public User deleteUser(String username) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		String sql = "DELETE * FROM users WHERE username = ?";
		// ... (rest of the getUser implementation using dbHelper.getConnection())
		return null; // Placeholder
	}

	public User updateUser(String username) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		String sql = "DELETE * FROM users WHERE username = ?";
		// ... (rest of the getUser implementation using dbHelper.getConnection())
		return null; // Placeholder
	}

	public void validatePassword(String username, String password) {

	}

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