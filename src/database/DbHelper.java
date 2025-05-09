package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import util.ProjUtil;

public class DbHelper {

	private static String dbtype = ProjUtil.getProperty("db.type").toLowerCase();

	public DbHelper() {
	}

	public static String getDbtype() {
		return dbtype;
	}

	public Connection getConnection() {
		Connection conn = null;
		try {
			String driver = ProjUtil.getProperty("db.driver");
			String url = ProjUtil.getProperty("db.url");
//			System.out.println("Attempting to load driver: " + driver);
			Class.forName(driver);
//			System.out.println("Driver loaded successfully.");
//			System.out.println("Attempting to connect to: " + url);
			conn = DriverManager.getConnection(url);
//			System.out.println("Connection established successfully.");
		} catch (ClassNotFoundException e) {
			System.err.println("Error: MySQL JDBC driver not found: " + e.getMessage());
			e.printStackTrace();
		} catch (SQLException e) {
			System.err.println("Error: Could not connect to the database: " + e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			System.err.println("An unexpected error occurred: " + e.getMessage());
			e.printStackTrace();
		}
		return conn;
	}

	public void closeConnection(Connection conn) {
		try {
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public void execute(String sql) throws SQLException {
		Connection conn = null;
		try {
			conn = this.getConnection();
			Statement stmt = conn.createStatement();

			stmt.execute(sql);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
	}

	public ResultSet executeQuery(Connection conn, String sql) {

		ResultSet rs = null;
		try {
			Statement stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		return rs;
	}

	public ArrayList<String> getColumnNames(ResultSet rs) {
		ArrayList<String> al = new ArrayList<String>();

		try {

			ResultSetMetaData rsMetaData;
			rsMetaData = rs.getMetaData();

			// Retrieving the list of column names
			int count = rsMetaData.getColumnCount();
			for (int i = 1; i <= count; i++) {
				al.add(rsMetaData.getColumnName(i));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return al;
	}

}
