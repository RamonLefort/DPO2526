package Persistance.DAO;

import Bussiness.Entities.Stat;
import Persistance.Configuration.MySQLDAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class StatDAO {

	private MySQLDAO mySQLDAO;


	public StatDAO(MySQLDAO mySQLDAO) {
		this.mySQLDAO = mySQLDAO;
	}

	public void create(Stat stat) {
		String query = "INSERT INTO stat (money, minutes, id_games, name_game) VALUES (?, ?, ?, ?)";
		try (PreparedStatement ps = mySQLDAO.getConnection().prepareStatement(query)) {
			ps.setDouble(1, stat.getMoney());
			ps.setInt(2, stat.getMinutes());
			ps.setInt(3, stat.getIdGame());
			ps.setString(4, stat.getNameGame());
			System.out.println("Saving stat: " + stat.getMoney() + " " + stat.getMinutes() + " " + stat.getIdGame() + " " + stat.getNameGame());
			ps.executeUpdate();
			System.out.println("Stat saved OK");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public List<Stat> readAllStats() {
		List<Stat> stats = new ArrayList<>();
		String query = "SELECT * FROM stat";
		try (PreparedStatement ps = mySQLDAO.getConnection().prepareStatement(query)) {
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				stats.add(new Stat(
						rs.getInt("id_stat"),
						rs.getDouble("money"),
						rs.getInt("minutes"),
						rs.getInt("id_games"),
						rs.getString("name_game")
				));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return stats;
	}

	public void delete(int idGames) {
	}


}