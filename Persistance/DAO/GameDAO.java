package Persistance.DAO;

import Bussiness.Entities.Game;
import Persistance.Configuration.MySQLDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) para la entidad Game.
 * Actúa como puente entre la capa de Business y la base de datos relacional,
 * gestionando todas las operaciones de persistencia relativas a las partidas.
 */
public class GameDAO {

	private MySQLDAO mySQLDAO;

	/**
	 * Constructor que guarda la dependencia de conexión a la base de datos.
	 *
	 * @param mySQLDAO Instancia de la clase que maneja la conexión física con MySQL.
	 */
	public GameDAO(MySQLDAO mySQLDAO) {
		this.mySQLDAO = mySQLDAO;
	}

	/**
	 * Registra una nueva partida en el sistema inicializando sus valores a los parámetros
	 * por defecto del inicio del juego.
	 *
	 * @param nameGame Nombre identificativo asignado a la partida.
	 * @param username Nombre del usuario al que pertenece la partida.
	 * @return El ID numérico autogenerado por la base de datos para la nueva partida, o 0 si falla.
	 */
	public int createGame(String nameGame, String username) throws SQLException {
		String query = "INSERT INTO game (name_game, username, money, minutes, seconds, coffee_per_click) VALUES (?, ?, 0, 0, 0, 1)";
		int gameId = -1;
		try (PreparedStatement ps = mySQLDAO.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
			ps.setString(1, nameGame);
			ps.setString(2, username);
			ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();
			if (rs.next()) {
				gameId = rs.getInt(1);
			}
			return gameId;
		} catch (SQLException e) {
			throw new SQLException(e);
		}
	}

	/**
	 * Recupera el listado completo de partidas de un jugador.
	 *
	 * @param username El nombre de usuario dueño de las partidas.
	 * @return Una lista de objetos {@link Game} ordenados por la base de datos.
	 * Devuelve una lista vacía si el usuario no tiene partidas registradas.
	 */
	public List<Game> getGamesByUser(String username) throws SQLException {
		List<Game> games = new ArrayList<>();
		String query = "SELECT * FROM game WHERE username = ?";
		try (PreparedStatement ps = mySQLDAO.getConnection().prepareStatement(query)) {
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				games.add(new Game(
						rs.getInt("id_game"),
						rs.getString("name_game"),
						rs.getDouble("money"),
						rs.getInt("hours"),
						rs.getInt("minutes"),
						rs.getInt("seconds"),
						rs.getInt("coffee_per_click"),
						rs.getFloat("production_per_second"),
						rs.getString("username"),
						rs.getBoolean("finished")
				));
			}
		} catch (SQLException e) {
			throw new SQLException(e);
		}
		return games;
	}

	/**
	 * Elimina permanentemente una partida.
	 *
	 * @param idGame El identificador único de la partida a destruir.
	 */
	public void deleteGame(int idGame) throws SQLException {
		mySQLDAO.deleteObject("game", "id_game", String.valueOf(idGame));
	}

	/**
	 * Actualiza el estado de una partida para marcarla como terminada.
	 *
	 * @param idGame El identificador único de la partida que se da por concluida.
	 */
	public void finishGame(int idGame) throws SQLException {
		String query = "UPDATE game SET finished = ? WHERE id_game = ?";

		try (PreparedStatement ps = mySQLDAO.getConnection().prepareStatement(query)) {
			ps.setDouble(1, 1);
			ps.setInt(2, idGame);
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new SQLException(e);
		}
	}

	/**
	 * Comprueba la disponibilidad de un nombre de partida.
	 *
	 * @param nameGame Nombre a verificar.
	 * @return {@code true} si ya existe una partida con ese nombre, {@code false} en caso contrario.
	 */
	public boolean existsByName(String nameGame) throws SQLException {
		ResultSet rs = mySQLDAO.readSpecific("game", "name_game", nameGame);
		try {
			return rs != null && rs.next();
		} catch (SQLException e) {
			throw new SQLException(e);
		}
	}

	/**
	 * Sobrescribe los valores actuales de la partida para persistir el progreso del jugador.
	 *
	 * @param username              Nombre del usuario.
	 * @param idGame                Identificador único de la partida objetivo.
	 * @param money                 Cantidad de moneda virtual acumulada.
	 * @param hours                 Horas transcurridas en la partida.
	 * @param minutes               Minutos transcurridos en la partida.
	 * @param seconds               Segundos transcurridos en la partida.
	 * @param coffeexclicks         Nivel actual de producción por cada click manual.
	 * @param production_per_second Tasa actual de generación automática.
	 */
	public void updateGame(String username, int idGame, double money, int hours, int minutes, int seconds, int coffeexclicks, float production_per_second) throws SQLException {
		String query = "UPDATE game SET money = ?, minutes = ?, seconds = ?, hours = ?, coffee_per_click = ?, production_per_second = ? WHERE id_game = ?";

		try (PreparedStatement ps = mySQLDAO.getConnection().prepareStatement(query)) {
			ps.setDouble(1, money);
			ps.setInt(2, minutes);
			ps.setInt(3, seconds);
			ps.setInt(4, hours);
			ps.setInt(5, coffeexclicks);
			ps.setFloat(6, production_per_second);
			ps.setInt(7, idGame);
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new SQLException(e);
		}
	}

	/**
	 * Recupera una partida completa de la base de datos buscando por su clave primaria (idGame).
	 *
	 * @param idGame Identificador único de la partida.
	 * @return Un objeto Game mapeado, o null si no se encuentra correspondencia.
	 * @throws SQLException Si ocurre un fallo en el socket de conexión.
	 */
	public Game getGameById(int idGame) throws SQLException {
		String query = "SELECT id_game, name_game, username, money, hours, minutes, seconds, coffeexclick, prodxsec, finished " +
				"FROM Game WHERE id_game = ?";

		Connection connection = mySQLDAO.getConnection();

		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setInt(1, idGame);

			try (ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					Game game = new Game(resultSet.getInt("id_game"), resultSet.getString("name_game"), resultSet.getDouble("money"), resultSet.getInt("hours") , resultSet.getInt("minutes"), resultSet.getInt("seconds"), resultSet.getInt("coffeexclick"), resultSet.getFloat("prodxsec"), resultSet.getString("username"), resultSet.getBoolean("finished"));
					return game;
				}
			}
		}
		return null;
	}

	/**
	 * Devuelve una lista ordenada con las claves primarias (IDs) de las partidas de un usuario.
	 * Es crucial para que el controlador pueda sincronizar el índice del JComboBox con el ID real.
	 */
	public List<Integer> getGameIdsByUser(String username) throws SQLException {
		List<Integer> ids = new ArrayList<>();
		String query = "SELECT id_game FROM Game WHERE username = ? ORDER BY id_game ASC";

		Connection connection = mySQLDAO.getConnection();

		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, username);

			try (ResultSet resultSet = statement.executeQuery()) {
				while (resultSet.next()) {
					ids.add(resultSet.getInt("id_game"));
				}
			}
		}
		return ids;
	}

	/**
	 * Filtra y recupera exclusivamente el listado de partidas que ya han sido terminadas
	 * por un usuario específico.
	 *
	 * @param username El nombre de usuario dueño de las partidas.
	 * @return Una lista de objetos {@link Game} cuyo campo {@code finished} es verdadero.
	 */
	public List<Game> readFinishedGames(String username) throws SQLException {
		List<Game> games = new ArrayList<>();
		String query = "SELECT * FROM game WHERE username = ? AND finished = true";
		try (PreparedStatement ps = mySQLDAO.getConnection().prepareStatement(query)) {
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				games.add(new Game(
						rs.getInt("id_game"),
						rs.getString("name_game"),
						rs.getDouble("money"),
						rs.getInt("hours"),
						rs.getInt("minutes"),
						rs.getInt("seconds"),
						rs.getInt("coffee_per_click"),
						rs.getFloat("production_per_second"),
						rs.getString("username"),
						rs.getBoolean("finished")
				));
			}
		} catch (SQLException e) {
			throw new SQLException(e);
		}
		return games;
	}
}