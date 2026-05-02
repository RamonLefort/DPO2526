package Persistance.DAO;

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

	public void update(Upgrade upgrade) {
		String query = "UPDATE upgrade SET active = ?, price = ? WHERE id_upgrade = ?";
		try (PreparedStatement ps = mySQLDAO.getConnection().prepareStatement(query)) {
			ps.setBoolean(1, upgrade.isActive());
			ps.setDouble(2, upgrade.getPrice());
			ps.setInt(3, upgrade.getIdUpgrade());
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