package Persistance.DAO;

import Bussiness.Entities.Upgrade;
import Persistance.Configuration.MySQLDAO;
import Persistance.Exceptions.PersistanceException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object para la entidad Upgrade.
 * Gestiona la persistencia de las mejoras que el jugador puede adquirir para aumentar
 * la eficiencia de sus generadores. Se encarga de controlar el estado de activación
 * y el coste de cada mejora dentro de una partida específica.
 */
public class UpgradeDAO {
	private final MySQLDAO mySQLDAO;

	/**
	 * Constructor que guardaa la dependencia de conexión a la base de datos.
	 *
	 * @param mySQLDAO Instancia encargada de la comunicación con el servidor MySQL.
	 */
	public UpgradeDAO(MySQLDAO mySQLDAO) {
		this.mySQLDAO = mySQLDAO;
	}

	/**
	 * Inserta una nueva mejora en la base de datos.
	 *
	 * @param upgrade Objeto {@link Upgrade} que contiene la información de la mejora a persistir.
	 */
	public void create(Upgrade upgrade) throws PersistanceException {
		String query = "INSERT INTO upgrade (id_generator, id_game, active, price) VALUES (?, ?, ?, ?)";
		try (PreparedStatement ps = mySQLDAO.getConnection().prepareStatement(query)) {
			ps.setInt(1, upgrade.getIdGenerator());
			ps.setInt(2, upgrade.getIdGame());
			ps.setBoolean(3, upgrade.isActive());
			ps.setDouble(4, upgrade.getPrice());
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new PersistanceException(e);
		}
	}

	/**
	 * Inicializa el conjunto de mejoras base para una nueva partida.
	 *
	 * @param idGame       Identificador de la partida actual.
	 * @param idBarista    ID del generador 'Barista' en esta partida.
	 * @param idMachine    ID del generador 'Espresso Machine' en esta partida.
	 * @param idPlantation ID del generador 'Coffee Plantation' en esta partida.
	 */
	public void createInitialUpgrades(int idGame, int idBarista, int idMachine, int idPlantation) throws PersistanceException {
		String query = "INSERT INTO upgrade (id_generator, id_game, active, price) VALUES (?, ?, ?, ?)";

		try (PreparedStatement ps = mySQLDAO.getConnection().prepareStatement(query)) {
			// Upgrade 1: Barista
			ps.setInt(1, idBarista); //Para el Barista
			ps.setInt(2, idGame);
			ps.setInt(3, 0);
			ps.setInt(4, 15000);
			ps.addBatch();

			// Upgrade 2: Máquina de Espresso
			ps.setInt(1, idMachine); //Para la maquina
			ps.setInt(2, idGame);
			ps.setInt(3, 0);
			ps.setInt(4, 150000);
			ps.addBatch();

			// Upgrade 3: Plantación de Café
			ps.setInt(1, idPlantation); //Para la plantación
			ps.setInt(2, idGame);
			ps.setInt(3, 0);
			ps.setInt(4, 200000);
			ps.addBatch();

			ps.executeBatch();
		} catch (SQLException e) {
			throw new PersistanceException(e);
		}
	}

	/**
	 * Recupera todas las mejoras disponibles y su estado para una partida concreta.
	 *
	 * @param idGame ID de la partida a consultar.
	 * @return Lista de objetos {@link Upgrade} vinculados a la partida.
	 */
	public List<Upgrade> readByGame(int idGame) throws PersistanceException {
		List<Upgrade> upgrades = new ArrayList<>();
		String query = "SELECT * FROM upgrade WHERE id_game = ?";
		try (PreparedStatement ps = mySQLDAO.getConnection().prepareStatement(query)) {
			ps.setInt(1, idGame);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					upgrades.add(new Upgrade(
							rs.getInt("id_upgrade"),
							rs.getInt("id_generator"),
							rs.getInt("id_game"),
							rs.getBoolean("active"),
							rs.getInt("price")
					));
				}
			}
		} catch (SQLException e) {
			throw new PersistanceException(e);
		}
		return upgrades;
	}

	/**
	 * Obtiene las mejoras asociadas a un generador específico, independientemente de la partida.
	 *
	 * @param idGenerator ID único del generador.
	 * @return Lista de mejoras aplicables a dicho generador.
	 */
	public List<Upgrade> readByGenerator(int idGenerator) throws PersistanceException {
		List<Upgrade> upgrades = new ArrayList<>();
		String query = "SELECT * FROM upgrade WHERE id_generator = ?";
		try (PreparedStatement ps = mySQLDAO.getConnection().prepareStatement(query)) {
			ps.setInt(1, idGenerator);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					upgrades.add(new Upgrade(
							rs.getInt("id_upgrade"),
							rs.getInt("id_generator"),
							rs.getInt("id_game"),
							rs.getBoolean("active"),
							rs.getInt("price")
					));
				}
			}
		} catch (SQLException e) {
			throw new PersistanceException(e);
		}
		return upgrades;
	}

	/**
	 * Actualiza el estado de activación de una mejora específica dentro de una partida.
	 *
	 * @param idGame      ID de la partida.
	 * @param idGenerator ID del generador al que pertenece la mejora.
	 * @param active      Nuevo estado de activación (true/false).
	 */
	public void update(int idGame, int idGenerator, boolean active) throws PersistanceException {
		String query = "UPDATE upgrade SET active = ? WHERE id_game = ? AND id_generator = ?";
		try (PreparedStatement ps = mySQLDAO.getConnection().prepareStatement(query)) {
			ps.setBoolean(1, active);
			ps.setDouble(2, idGame);
			ps.setInt(3, idGenerator);
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new PersistanceException(e);
		}
	}

	/**
	 * Elimina físicamente una mejora de la base de datos mediante su identificador.
	 *
	 * @param idUpgrade Identificador único de la mejora a eliminar.
	 */
	public void delete(int idUpgrade) throws PersistanceException {
		String query = "DELETE FROM upgrade WHERE id_upgrade = ?";
		try (PreparedStatement ps = mySQLDAO.getConnection().prepareStatement(query)) {
			ps.setInt(1, idUpgrade);
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new PersistanceException(e);
		}
	}
}