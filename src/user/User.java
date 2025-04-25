package user;

public class User {

	private String email;
	private String firstName;
	private String lastName;
	private String password;
	private boolean admin;

	public User(String email, String firstName, String lastName, String password) {
		this.setEmail(email);
		this.setFirstName(firstName);
		this.setLastName(lastName);
		this.password = password;
		this.admin = false;
	}

	protected String getPassword() {
		return password;
	}

	protected void setPassword(String password) {
		this.password = password;
	}

	protected boolean isAdmin() {
		return admin;
	}

	protected void setAdmin(boolean admin) {
		this.admin = admin;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

}
