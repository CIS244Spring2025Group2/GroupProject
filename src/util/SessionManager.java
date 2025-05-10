package util;

import user.User;

/**
 * Session Manager keeps track of the current logged in user with a static
 * method
 */
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
