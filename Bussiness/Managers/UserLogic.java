package Bussiness.Managers;

import Bussiness.Entities.User;
import Persistance.DAO.UserDAO;

public class UserLogic {

	private UserDAO userDAO;
	private User currentUser;


	public UserLogic(UserDAO userDAO) {
		this.userDAO = userDAO;
	}
	public boolean register(String username, String email, String password, String confirm) {
		if (!password.equals(confirm) || !validateEmail(email) || !validatePassword(password)) {
			return false;
		}

		if (userDAO.usernameExists(username)) {
			return false;
		}

		if (userDAO.emailExists(email)) {
			return false;
		}

		return userDAO.create(new User(username, email, password));
	}

	public boolean usernameExists(String username) {
		return userDAO.usernameExists(username);
	}

	public boolean emailExists(String email) {
		return userDAO.emailExists(email);
	}

	public User login(String usernameOrEmail, String pass) {
		User user = userDAO.loginCheck(usernameOrEmail, pass);
		if (user != null) {
			this.currentUser = user;
		}
		return user;
	}

	public void logout() {
		this.currentUser = null;
	}

	public void deleteAccount(String username) {
		userDAO.delete(username);
		if (currentUser != null && currentUser.getUsername().equals(username)) {
			logout();
		}
	}

	public boolean validateEmail(String email) {
		return email != null && email.endsWith("@gmail.com");
	}

	public boolean validatePassword(String password) {
		if (password == null || password.length() < 8){
			return false;
		}

		boolean hasLetter    = false;
		boolean hasDigit     = false;
		boolean hasUpperCase = false;

		for (int i = 0; i < password.length(); i++) {
			char c = password.charAt(i);
			if (c >= 'a' && c <= 'z') {
				hasLetter    = true;
			}
			if (c >= 'A' && c <= 'Z') {
				hasLetter  = true; hasUpperCase = true; }
			if (c >= '0' && c <= '9'){
				hasDigit     = true;
			}
		}

		return hasLetter && hasDigit && hasUpperCase;
	}

	public User getCurrentUser() {
		return currentUser;
	}
}