package user;

public class SessionManager {
	private static User currentUser;

	public static User getCurrentUser() {
		return currentUser;
	}

	public static void setCurrentUser(User user) {
		currentUser = user;
	}

	public static void logoutUser() {
		currentUser = null;
	}
}
