package Persistance.DAO;

import Bussiness.Entities.User;
import Persistance.Configuration.MySQLDAO;
import Persistance.Exceptions.PersistanceException;

import java.sql.*;
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
	public boolean create(User user) throws PersistanceException {
		String query = "INSERT INTO user (username, email, password) VALUES (?, ?, ?)";
		try (PreparedStatement ps = mySQLDAO.getConnection().prepareStatement(query)) {
			ps.setString(1, user.getUsername());
			ps.setString(2, user.getEmail());
			ps.setString(3, user.getPassword());
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			throw new PersistanceException(e);
		}
	}

	/**
	 * Recupera un usuario específico a través de su nombre de usuario.
	 *
	 * @param username El nombre de usuario a buscar.
	 * @return Una instancia de {@link User} con los datos de la base de datos,
	 * o {@code null} si no se encuentra ninguna coincidencia.
	 */
	public User read(String username) throws PersistanceException {
		try {
			ResultSet rs = mySQLDAO.readSpecific("user", "username", username);
			if (rs != null && rs.next()) {
				return new User(
						rs.getString("username"),
						rs.getString("email"),
						rs.getString("password")
				);
			}
		} catch (SQLException e) {
			throw new PersistanceException(e);
		}
		return null;
	}

	/**
	 * Obtiene el listado completo de todos los usuarios registrados en el sistema.
	 *
	 * @return Una lista de objetos {@link User}.
	 */
	public List<User> readAllUsers() throws PersistanceException {
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
			throw new PersistanceException(e);
		}
		return users;
	}

	/**
	 * Extrae todos los nombres de usuario (username) únicos registrados en el sistema.
	 * Al propagar SQLException, permitimos que la lógica capture fallos físicos de red.
	 *
	 * @return Lista de Strings con los nombres de usuario.
	 * @throws SQLException Si el servidor de base de datos está caído o la consulta falla.
	 */
	public List<String> getAllUsernames() throws PersistanceException {
		List<String> usernames = new ArrayList<>();
		String query = "SELECT username FROM User ORDER BY username ASC";

		// Abrimos el canal de conexión a través de tu clase gestora de base de datos
		Connection connection = mySQLDAO.getConnection();

		try (PreparedStatement statement = connection.prepareStatement(query);
			 ResultSet resultSet = statement.executeQuery()) {

			while (resultSet.next()) {
				usernames.add(resultSet.getString("username"));
			}
		} catch (SQLException e) {
			throw new PersistanceException(e);
		}
		return usernames;
	}

	/**
	 * Elimina permanentemente un usuario de la base de datos.
	 *
	 * @param username El identificador del usuario a eliminar.
	 */
	public void delete(String username) throws PersistanceException {
        try {
            mySQLDAO.deleteObject("user", "username", username);
        } catch (SQLException e) {
            throw new PersistanceException(e);
        }
    }

	/**
	 * Verifica si un nombre de usuario ya está en uso.
	 *
	 * @param username Nombre de usuario a comprobar.
	 * @return {@code true} si el nombre ya existe; {@code false} en caso contrario.
	 */
	public boolean usernameExists(String username) throws PersistanceException {
        try {
            return read(username) != null;
        } catch (PersistanceException e) {
			throw new PersistanceException(e);
        }
    }

	/**
	 * Verifica si una dirección de correo electrónico ya está registrada.
	 *
	 * @param email Correo electrónico a comprobar.
	 * @return {@code true} si el email ya existe en el sistema; {@code false} si está disponible.
	 */
	public boolean emailExists(String email) throws PersistanceException {
		try {
			ResultSet rs = mySQLDAO.readSpecific("user", "email", email);
			return rs != null && rs.next();
		} catch (SQLException e) {
			throw new PersistanceException(e);
		}
	}

	/**
	 * Realiza la comprobación de identidad permitiendo el acceso tanto por nombre de usuario
	 * como por correo electrónico.
	 *
	 * @param usernameOrEmail Cadena que representa el nombre de usuario o el email introducido.
	 * @return El objeto {@link User} si las credenciales coinciden con algún registro,
	 */
	public User loginCheck(String usernameOrEmail) throws PersistanceException {
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
			throw new PersistanceException(e);
		}
		return null;
	}
}
