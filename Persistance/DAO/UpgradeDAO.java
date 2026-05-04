package Persistance.DAO;

import Bussiness.Entities.Generator;
import Bussiness.Entities.Upgrade;
import Persistance.Configuration.MySQLDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UpgradeDAO {
	private final MySQLDAO mySQLDAO;

	public UpgradeDAO(MySQLDAO mySQLDAO) {
		this.mySQLDAO = mySQLDAO;
	}

	public void create(Upgrade upgrade) {
		String query = "INSERT INTO upgrade (id_generator, id_game, active, price) VALUES (?, ?, ?, ?)";
		try (PreparedStatement ps = mySQLDAO.getConnection().prepareStatement(query)) {
			ps.setInt(1, upgrade.getIdGenerator());
			ps.setInt(2, upgrade.getIdGame());
			ps.setBoolean(3, upgrade.isActive());
			ps.setDouble(4, upgrade.getPrice());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void createInitialUpgrades(int idGame, int idBarista, int idMachine, int idPlantation) {
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
			System.err.println("Error al inicializar generadores: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public List<Upgrade> readByGame(int idGame) {
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
			e.printStackTrace();
		}
		return upgrades;
	}

	public List<Upgrade> readByGenerator(int idGenerator) {
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
			e.printStackTrace();
		}
		return upgrades;
	}

	public void update(int idGame, int idGenerator, boolean active) {
		String query = "UPDATE upgrade SET active = ? WHERE id_game = ? AND id_generator = ?";
		try (PreparedStatement ps = mySQLDAO.getConnection().prepareStatement(query)) {
			ps.setBoolean(1, active);
			ps.setDouble(2, idGame);
			ps.setInt(3, idGenerator);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void delete(int idUpgrade) {
		String query = "DELETE FROM upgrade WHERE id_upgrade = ?";
		try (PreparedStatement ps = mySQLDAO.getConnection().prepareStatement(query)) {
			ps.setInt(1, idUpgrade);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}