package Persistance.DAO;

import Bussiness.Entities.User;
import Persistance.Configuration.MySQLDAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) para la entidad User.
 * Se encarga de centralizar todas las operaciones de persistencia relacionadas con las cuentas
 * de usuario, incluyendo el registro, la validación de credenciales y la comprobación de
 * duplicados en el sistema.
 */
public class UserDAO {

	private MySQLDAO mySQLDAO;

	/**
	 * Constructor que guarda la instancia de conexión necesaria para las operaciones SQL.
	 *
	 * @param mySQLDAO Objeto que gestiona la conexión física con la base de datos MySQL.
	 */
	public UserDAO(MySQLDAO mySQLDAO) {
		this.mySQLDAO = mySQLDAO;
	}

	/**
	 * Inserta un nuevo usuario en la base de datos.
	 *
	 * @param user Objeto {@link User} que contiene la información del nuevo registro.
	 * @return {@code true} si la inserción fue exitosa; {@code false} si ocurrió un error
	 * de SQL (como violación de restricción de unicidad).
	 */
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

	/**
	 * Recupera un usuario específico a través de su nombre de usuario.
	 *
	 * @param username El nombre de usuario a buscar.
	 * @return Una instancia de {@link User} con los datos de la base de datos,
	 * o {@code null} si no se encuentra ninguna coincidencia.
	 */
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

	/**
	 * Obtiene el listado completo de todos los usuarios registrados en el sistema.
	 *
	 * @return Una lista de objetos {@link User}.
	 */
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

	/**
	 * Elimina permanentemente un usuario de la base de datos.
	 *
	 * @param username El identificador del usuario a eliminar.
	 */
	public void delete(String username) {
		mySQLDAO.deleteObject("user", "username", username);
	}

	/**
	 * Verifica si un nombre de usuario ya está en uso.
	 *
	 * @param username Nombre de usuario a comprobar.
	 * @return {@code true} si el nombre ya existe; {@code false} en caso contrario.
	 */
	public boolean usernameExists(String username) {
		return read(username) != null;
	}

	/**
	 * Verifica si una dirección de correo electrónico ya está registrada.
	 *
	 * @param email Correo electrónico a comprobar.
	 * @return {@code true} si el email ya existe en el sistema; {@code false} si está disponible.
	 */
	public boolean emailExists(String email) {
		ResultSet rs = mySQLDAO.readSpecific("user", "email", email);
		try {
			return rs != null && rs.next();
		} catch (SQLException e) {
			return false;
		}
	}

	/**
	 * Realiza la comprobación de identidad permitiendo el acceso tanto por nombre de usuario
	 * como por correo electrónico.
	 *
	 * @param usernameOrEmail Cadena que representa el nombre de usuario o el email introducido.
	 * @return El objeto {@link User} si las credenciales coinciden con algún registro,
	 * o {@code null} si no hay coincidencias.
	 */
	public User loginCheck(String usernameOrEmail) {
		String query = "SELECT * FROM user WHERE username = ? OR email = ?";
		try (PreparedStatement ps = mySQLDAO.getConnection().prepareStatement(query)) {
			ps.setString(1, usernameOrEmail);
			ps.setString(2, usernameOrEmail);
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
