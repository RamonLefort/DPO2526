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

	/**
	 * Crea un nuevo registro de estadística en la tabla 'stat'.
	 * Este método se usa tanto para el volcado minuto a minuto como para el resumen final.
	 */
	public void create(int idGame, int minute, double money, int clicks, double autoGen, float maxProd, double expenses) {
		String query = "INSERT INTO stat (id_games, minute_mark, money_at_minute, manual_clicks_total, " +
				"auto_generated_total, max_production_rate, upgrades_expenses) " +
				"VALUES (?, ?, ?, ?, ?, ?, ?)";

		try (PreparedStatement ps = mySQLDAO.getConnection().prepareStatement(query)) {
			ps.setInt(1, idGame);           // id_games (FK)
			ps.setInt(2, minute);           // minute_mark
			ps.setDouble(3, money);         // money_at_minute
			ps.setInt(4, clicks);           // manual_clicks_total
			ps.setDouble(5, autoGen);       // auto_generated_total
			ps.setFloat(6, maxProd);        // max_production_rate
			ps.setDouble(7, expenses);      // upgrades_expenses

			ps.executeUpdate();
		} catch (SQLException e) {
			System.err.println("Error al insertar estadísticas: " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Inserta un registro de progreso (cada minuto) o el resumen final.
	 */
	public void saveMinuteStat(int idGame, int minute, double money, int clicks, double autoGen, float maxProd, double expenses) {
		String query = "INSERT INTO stat (id_games, minute_mark, money_at_minute, manual_clicks_total, " +
				"auto_generated_total, max_production_rate, upgrades_expenses) " +
				"VALUES (?, ?, ?, ?, ?, ?, ?)";

		try (PreparedStatement ps = mySQLDAO.getConnection().prepareStatement(query)) {
			ps.setInt(1, idGame);
			ps.setInt(2, minute);
			ps.setDouble(3, money);
			ps.setInt(4, clicks);
			ps.setDouble(5, autoGen);
			ps.setFloat(6, maxProd);
			ps.setDouble(7, expenses);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Recupera el historial completo de estadísticas de una partida.
	 * @param idGame ID de la partida a consultar.
	 * @return Lista de objetos Stat ordenada por minuto.
	 */
	public List<Stat> readByGame(int idGame) {
		List<Stat> stats = new ArrayList<>();
		String query = "SELECT * FROM stat WHERE id_games = ? ORDER BY minute_mark ASC";

		try (PreparedStatement ps = mySQLDAO.getConnection().prepareStatement(query)) {
			ps.setInt(1, idGame);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					stats.add(new Stat(
							rs.getInt("id_stat"),
							rs.getInt("id_games"),
							rs.getInt("minute_mark"),
							rs.getDouble("money_at_minute"),
							rs.getInt("manual_clicks_total"),
							rs.getDouble("auto_generated_total"),
							rs.getFloat("max_production_rate"),
							rs.getDouble("upgrades_expenses")
					));
				}
			}
		} catch (SQLException e) {
			System.err.println("Error al leer estadísticas: " + e.getMessage());
			e.printStackTrace();
		}
		return stats;
	}

	/**
	 * Obtiene el último registro estadístico guardado para una partida específica.
	 * Útil para recuperar acumulados o verificar el último minuto registrado.
	 * @param idGame ID de la partida.
	 * @return El objeto Stat más reciente o null si no hay registros.
	 */
	public Stat getLastMinuteStat(int idGame) {
		Stat lastStat = null;
		// Buscamos el minuto máximo usando ORDER BY y LIMIT 1 para optimizar la consulta.
		String query = "SELECT * FROM stat WHERE id_games = ? ORDER BY minute_mark DESC LIMIT 1";

		try (PreparedStatement ps = mySQLDAO.getConnection().prepareStatement(query)) {
			ps.setInt(1, idGame);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					lastStat = new Stat(
							rs.getInt("id_stat"),
							rs.getInt("id_games"),
							rs.getInt("minute_mark"),
							rs.getDouble("money_at_minute"),
							rs.getInt("manual_clicks_total"),
							rs.getDouble("auto_generated_total"),
							rs.getFloat("max_production_rate"),
							rs.getDouble("upgrades_expenses")
					);
				}
			}
		} catch (SQLException e) {
			System.err.println("Error al obtener la última estadística: " + e.getMessage());
			e.printStackTrace();
		}
		return lastStat;
	}

}