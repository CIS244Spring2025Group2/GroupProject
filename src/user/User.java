package user;

public class User {

	private String username;
	private String password;
	private boolean admin;

	public User(String username, String password) {
		this.username = username;
		this.password = password;
		this.admin = false;
	}

	protected String getPassword() {
		return password;
	}

	protected void setPassword(String password) {
		this.password = password;
	}

	protected String getUsername() {
		return username;
	}

	protected void setUsername(String username) {
		this.username = username;
	}

	protected boolean isAdmin() {
		return admin;
	}

	protected void setAdmin(boolean admin) {
		this.admin = admin;
	}

}
