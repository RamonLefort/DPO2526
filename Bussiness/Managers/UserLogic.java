package Bussiness.Managers;

import Bussiness.Entities.User;
import Persistance.DAO.UserDAO;
import Persistance.DAO.SettingDAO;
import org.mindrot.jbcrypt.BCrypt;
public class UserLogic {

	private UserDAO userDAO;
	private SettingDAO settingDAO;
	private User currentUser;

	public UserLogic(UserDAO userDAO, SettingDAO settingDAO) {
		this.userDAO = userDAO;
		this.settingDAO = settingDAO;
	}

	public boolean register(String username, String email, String password, String confirm) {
		if (!password.equals(confirm) || !validateEmail(email) || !validatePassword(password)) {
			return false;
		}
		if (userDAO.usernameExists(username)) return false;
		if (userDAO.emailExists(email)) return false;

		String hashed = BCrypt.hashpw(password, BCrypt.gensalt());
		return userDAO.create(new User(username, email, hashed));
	}

	public User login(String usernameOrEmail, String pass) {

		User user = userDAO.loginCheck(usernameOrEmail);
		if (user != null && BCrypt.checkpw(pass, user.getPassword())) {
			this.currentUser = user;
			return user;
		}
		return null;
	}

	public boolean usernameExists(String username) {
		return userDAO.usernameExists(username);
	}

	public boolean emailExists(String email) {
		return userDAO.emailExists(email);
	}



	public void logout() {
		this.currentUser = null;
	}

	public void deleteAccount(String username) {
		settingDAO.delete(username);
		userDAO.delete(username);
		if (currentUser != null && currentUser.getUsername().equals(username)) {
			logout();
		}
	}

	public boolean validateEmail(String email) {
		return email != null
				&& email.endsWith("@gmail.com")
				&& email.indexOf("@") > 0;
	}

	public boolean validatePassword(String password) {
		if (password == null || password.length() < 8){
			return false;
		}

		boolean hasLetter    = false;
		boolean hasDigit     = false;
		boolean hasMLetter = false;

		for (int i = 0; i < password.length(); i++) {
			char c = password.charAt(i);
			if (c >= 'a' && c <= 'z') {
				hasLetter    = true;
			}
			if (c >= 'A' && c <= 'Z') {
				hasLetter  = true; hasMLetter = true; }
			if (c >= '0' && c <= '9'){
				hasDigit     = true;
			}
		}
		if (hasLetter == false) {
			return false;
		}
		if (hasMLetter == false) {
			return false;
		}
		if (hasDigit == false) {
			return false;
		}
		return true;
	}

	public User getCurrentUser() {
		return currentUser;
	}
}