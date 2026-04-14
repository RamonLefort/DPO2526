package Persistance.DAO;

import Bussiness.Entities.User;
import Persistance.Configuration.MySQLDAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

	private MySQLDAO mySQLDAO;


	public UserDAO(MySQLDAO mySQLDAO) {
		this.mySQLDAO = mySQLDAO;
	}

	public boolean create(User user) {
		String query = "INSERT INTO user (username, email, password) VALUES (?, ?, ?)";
		try (PreparedStatement ps = mySQLDAO.getConnection().prepareStatement(query)) {
			ps.setString(1, user.getUsername());
			ps.setString(2, user.getEmail());
			ps.setString(3, user.getPassword());
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public User read(String username) {
		ResultSet rs = mySQLDAO.readSpecific("user", "username", username);
		try {
			if (rs != null && rs.next()) {
				return new User(
						rs.getString("username"),
						rs.getString("email"),
						rs.getString("password")
				);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}


	public List<User> readAllUsers() {
		List<User> users = new ArrayList<>();
		String query = "SELECT * FROM user";
		try (Statement st = mySQLDAO.getConnection().createStatement();
		     ResultSet rs = st.executeQuery(query)) {
			while (rs.next()) {
				users.add(new User(
						rs.getString("username"),
						rs.getString("email"),
						rs.getString("password")
				));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return users;
	}

	public void delete(String username) {
		mySQLDAO.deleteObject("user", "username", username);
	}

	public boolean usernameExists(String username) {
		return read(username) != null;
	}

	public boolean emailExists(String email) {
		ResultSet rs = mySQLDAO.readSpecific("user", "email", email);
		try {
			return rs != null && rs.next();
		} catch (SQLException e) {
			return false;
		}
	}


	public User loginCheck(String usernameOrEmail, String password) {
		String query = "SELECT * FROM user WHERE (username = ? OR email = ?) AND password = ?";
		try (PreparedStatement ps = mySQLDAO.getConnection().prepareStatement(query)) {
			ps.setString(1, usernameOrEmail);
			ps.setString(2, usernameOrEmail);
			ps.setString(3, password);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return new User(
						rs.getString("username"),
						rs.getString("email"),
						rs.getString("password")
				);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}