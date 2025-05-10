package user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import database.DbHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import util.ProjUtil;

/**
 * UserDAO (Database Access Object) contains all the methods for creating,
 * updating, and accessing user objects in the database
 * 
 * public void addUser(User user)
 * 
 * public User getUser(String email)
 * 
 * public void deleteUser(String email)
 * 
 * public void updatePassword(String email, String newPassword)
 * 
 * public void updateUserInfo(String email, String firstName, String lastName,
 * String securityQuestion, String securityAnswer)
 * 
 * public void createDefaultAdminUser()
 * 
 * private boolean adminUserExists(Connection conn, String username)
 * 
 * public ObservableList<User> getAllUsersWithAdminStatus()
 * 
 * public void updateUserAdminStatus(String email, boolean isAdmin)
 * 
 * public static class UserAlreadyExistsException extends Exception
 */

public class UserDAO {

	private DbHelper dbHelper = new DbHelper();

	// Method for adding user
	public void addUser(User user) throws SQLException, UserAlreadyExistsException {
		Connection connection = null;
		PreparedStatement checkStatement = null;
		PreparedStatement insertStatement = null;

		try {
			connection = dbHelper.getConnection();

			// 1. Check if the email already exists
			String checkSql = "SELECT COUNT(*) FROM User WHERE email = ?";
			checkStatement = connection.prepareStatement(checkSql);
			checkStatement.setString(1, user.getEmail());
			ResultSet resultSet = checkStatement.executeQuery();

			if (resultSet.next() && resultSet.getInt(1) > 0) {
				throw new UserAlreadyExistsException("User with email '" + user.getEmail() + "' already exists.");
			}

			// 2. If the email doesn't exist, proceed with insertion
			String hashedPassword = ProjUtil.getSHA(user.getPassword());
			String hashedSecurityAnswser = ProjUtil.getSHA(user.getSecurityAnswer());
			String insertSql = "INSERT INTO User (firstName, lastName, email, securityQuestion, securityAnswer, password, admin) VALUES (?, ?, ?, ?, ?, ?, 0)";
			insertStatement = connection.prepareStatement(insertSql);
			insertStatement.setString(1, user.getFirstName());
			insertStatement.setString(2, user.getLastName());
			insertStatement.setString(3, user.getEmail());
			insertStatement.setString(4, user.getSecurityQuestion());
			insertStatement.setString(5, hashedSecurityAnswser);
			insertStatement.setString(6, hashedPassword);

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

	// gets a user by email
	public User getUser(String email) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		User user = null;

		try {
			connection = dbHelper.getConnection();
			String sql = "SELECT id, firstName, lastName, email, securityQuestion, securityAnswer, password, admin FROM User WHERE email = ?";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, email);
			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				user = new User(resultSet.getString("email"), resultSet.getString("firstName"),
						resultSet.getString("lastName"), resultSet.getString("securityQuestion"),
						resultSet.getString("securityAnswer"), resultSet.getString("password"),
						resultSet.getBoolean("admin"));
			}
		} finally {
			// Close resources
			if (resultSet != null)
				try {
					resultSet.close();
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
		return user;
	}

	// delete user by email
	public void deleteUser(String email) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			connection = dbHelper.getConnection();
			String sql = "DELETE FROM User WHERE email = ?";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, email);
			int rowsAffected = preparedStatement.executeUpdate();
			if (rowsAffected > 0) {
				System.out.println("User with email '" + email + "' deleted.");
			} else {
				System.out.println("User with email '" + email + "' not found.");
			}
		} finally {
			if (preparedStatement != null)
				try {
					preparedStatement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			dbHelper.closeConnection(connection);
		}
	}

	// update user password
	public void updatePassword(String email, String newPassword) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			connection = dbHelper.getConnection();
			String hashedPassword = ProjUtil.getSHA(newPassword);
			String sql = "UPDATE User SET password = ? WHERE email = ?";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, hashedPassword);
			preparedStatement.setString(2, email);
			int rowsAffected = preparedStatement.executeUpdate();
			if (rowsAffected > 0) {
				System.out.println("Password updated successfully for user: " + email);
			} else {
				System.out.println("User with email '" + email + "' not found.");
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

	// updates user firstName, lastName, securityQuestion, and securityAnswer
	public void updateUserInfo(String email, String firstName, String lastName, String securityQuestion,
			String securityAnswer) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			connection = dbHelper.getConnection();
			String sql = "UPDATE User SET firstName = ?, lastName = ?, securityQuestion = ?, securityAnswer = ? WHERE email = ?";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, firstName);
			preparedStatement.setString(2, lastName);
			preparedStatement.setString(3, securityQuestion);
			preparedStatement.setString(4, securityAnswer);
			preparedStatement.setString(5, email);
			int rowsAffected = preparedStatement.executeUpdate();
			if (rowsAffected > 0) {
				System.out.println("User info updated successfully for user: " + email);
			} else {
				System.out.println("User with email '" + email + "' not found.");
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

	// creates the Default Admin user specified in config.properties
	public void createDefaultAdminUser() {
		String adminUsername = ProjUtil.getProperty("default.admin.email");
		String adminPassword = ProjUtil.getProperty("default.admin.password");
		String adminSecurityQuesiton = ProjUtil.getProperty("default.admin.securityQuestion");
		String adminSecurityAnswer = ProjUtil.getProperty("default.admin.securityAnswer");

		if (adminUsername != null && adminPassword != null && !adminUsername.isEmpty() && !adminPassword.isEmpty()) {
			try (Connection conn = dbHelper.getConnection()) {
				if (!adminUserExists(conn, adminUsername)) {
					String hashedPassword = ProjUtil.getSHA(adminPassword);
					String hashedSecurityAnswer = ProjUtil.getSHA(adminSecurityAnswer);
					String insertAdminSQL = "INSERT INTO User (firstName, lastName, email, securityQuestion, securityAnswer, password, admin) VALUES ('Admin', 'User', ?, ?, ?, ?, 1)";
					try (PreparedStatement pstmt = conn.prepareStatement(insertAdminSQL)) {
						pstmt.setString(1, adminUsername);
						pstmt.setString(2, adminSecurityQuesiton);
						pstmt.setString(3, hashedSecurityAnswer);
						pstmt.setString(4, hashedPassword);
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

	// checks if the default admin user already exists
	private boolean adminUserExists(Connection conn, String username) throws SQLException {
		String checkSQL = "SELECT COUNT(*) FROM User WHERE email = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(checkSQL)) {
			pstmt.setString(1, username);
			ResultSet rs = pstmt.executeQuery();
			return rs.next() && rs.getInt(1) > 0;
		}
	}

	// creates a list of all admin users
	public ObservableList<User> getAllUsersWithAdminStatus() throws SQLException {
		ObservableList<User> users = FXCollections.observableArrayList();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		String sql = "SELECT email, admin FROM User";

		try {
			connection = dbHelper.getConnection();
			preparedStatement = connection.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				String email = resultSet.getString("email");
				boolean isAdmin = resultSet.getBoolean("admin");
				User user = new User(email, null, null, null, null, null, isAdmin); // Create a User object with email
																					// and admin status
				users.add(user);
			}
		} finally {
			// Close resources
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (preparedStatement != null) {
				try {
					preparedStatement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			dbHelper.closeConnection(connection);
		}
		return users;
	}

	// adds or removes admin user status
	public void updateUserAdminStatus(String email, boolean isAdmin) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			connection = dbHelper.getConnection();
			String sql = "UPDATE User SET admin = ? WHERE email = ?";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setBoolean(1, isAdmin);
			preparedStatement.setString(2, email);
			int rowsAffected = preparedStatement.executeUpdate();
			if (rowsAffected > 0) {
				System.out.println("Admin status updated for user '" + email + "' to " + isAdmin);
			} else {
				System.out.println("User with email '" + email + "' not found.");
			}
		} finally {
			if (preparedStatement != null)
				try {
					preparedStatement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			dbHelper.closeConnection(connection);
		}
	}

	// exception for if a user already exists
	public static class UserAlreadyExistsException extends Exception {
		public UserAlreadyExistsException(String message) {
			super(message);
		}
	}
}