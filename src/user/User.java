package user;

/**
 * User objects contain all the relevant user information email, first and last
 * name, security question and answer, password, and whether they're an admin
 */
public class User {

	private String email;
	private String firstName;
	private String lastName;
	private String securityQuestion;
	private String securityAnswer;
	private String password;
	private boolean admin;

	public User(String email, String firstName, String lastName, String securityQuestion, String securityAnswer,
			String password) {
		this(email, firstName, lastName, securityQuestion, securityAnswer, password, false);
	}

	public User(String email, String firstName, String lastName, String securityQuestion, String securityAnswer,
			String password, Boolean admin) {
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.securityQuestion = securityQuestion;
		this.securityAnswer = securityAnswer;
		this.password = password;
		this.admin = admin;
	}

	protected String getPassword() {
		return password;
	}

	protected void setPassword(String password) {
		this.password = password;
	}

	public boolean isAdmin() {
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

	public String getSecurityQuestion() {
		return securityQuestion;
	}

	public void setSecurityQuestion(String securityQuestion) {
		this.securityQuestion = securityQuestion;
	}

	protected String getSecurityAnswer() {
		return securityAnswer;
	}

	protected void setSecurityAnswer(String securityAnswer) {
		this.securityAnswer = securityAnswer;
	}

}
