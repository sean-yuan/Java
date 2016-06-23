package seanyuan_CSCI201L_Assignment4;

import tab.ServerFrame;

public class AccountManager {
	public static UserAccount signup(String username, String hashedPassword) {
		return UserAccount.createAccount(username, hashedPassword);
	}
	
	public static UserAccount loginAccount(String username, String unhashedPassword) {
		return UserAccount.authenticate(username, unhashedPassword);
	}
	
	public static boolean validatePassword(String unhashedPassword) {
		boolean hasUppercase = !unhashedPassword.equals(unhashedPassword.toLowerCase());
		boolean hasNumber = unhashedPassword.matches(".*\\d+.*");
		return hasUppercase && hasNumber;
	}
}
