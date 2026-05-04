package Persistance.DAO;

import Bussiness.Entities.Game;
import Persistance.Configuration.MySQLDAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class GameDAO {

	private MySQLDAO mySQLDAO;

	public GameDAO(MySQLDAO mySQLDAO) {
		this.mySQLDAO = mySQLDAO;
	}

	public int createGame(String nameGame, String username) {
		String query = "INSERT INTO game (name_game, username, money, minutes, seconds, coffee_per_click) VALUES (?, ?, 0, 0, 0, 1)";
		try (PreparedStatement ps = mySQLDAO.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
			ps.setString(1, nameGame);
			ps.setString(2, username);
			ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();
			if (rs.next()) {
				return rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public List<Game> getGamesByUser(String username) {
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
			e.printStackTrace();
		}
		return games;
	}

	public void deleteGame(int idGame) {
		mySQLDAO.deleteObject("game", "id_game", String.valueOf(idGame));
	}

	public void finishGame(int idGame){
		String query = "UPDATE game SET finished = ? WHERE id_game = ?";

		try (PreparedStatement ps = mySQLDAO.getConnection().prepareStatement(query)) {
			ps.setDouble(1, 1);
			ps.setInt(2, idGame);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean existsByName(String nameGame) {
		ResultSet rs = mySQLDAO.readSpecific("game", "name_game", nameGame);
		try {
			return rs != null && rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public void updateGame(String username, int idGame, double money, int hours, int minutes, int seconds, int coffeexclicks, float production_per_second) {
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
			e.printStackTrace();
		}
	}

	public List<Game> readFinishedGames(String username) {
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
			e.printStackTrace();
		}
		return games;
	}
}