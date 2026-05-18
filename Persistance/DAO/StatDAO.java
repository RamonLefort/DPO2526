package Persistance.DAO;

import Bussiness.Entities.Stat;
import Persistance.Configuration.MySQLDAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object para la entidad Stat.
 * Encargado de la persistencia de las métricas de progreso de las partidas.
 * Permite registrar y consultar el histórico de generación de recursos
 */
public class StatDAO {

	private MySQLDAO mySQLDAO;

	/**
	 * Constructor que guarda la dependencia de la conexión a la base de datos.
	 *
	 * @param mySQLDAO Objeto que gestiona la conexión con MySQL.
	 */
	public StatDAO(MySQLDAO mySQLDAO) {
		this.mySQLDAO = mySQLDAO;
	}

	/**
	 * Crea un nuevo registro de estadística en la tabla 'stat'.
	 *
	 * @param idGame   Identificador de la partida.
	 * @param minute   Minuto exacto de la partida en el que se toma la muestra.
	 * @param money    Dinero total acumulado hasta ese minuto.
	 * @param clicks   Cantidad total de clicks manuales realizados.
	 * @param autoGen  Cantidad total de recursos generados automáticamente.
	 * @param maxProd  Tasa de producción máxima alcanzada en ese punto.
	 * @param expenses Dinero total invertido en mejoras y generadores.
	 */
	public void create(int idGame, int minute, double money, int clicks, double autoGen, float maxProd, double expenses) throws SQLException {
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
			throw new SQLException(e);
		}
	}

	/**
	 * Inserta un registro de progreso (cada minuto) o el resumen final.
	 *
	 * @param idGame   Identificador de la partida.
	 * @param minute   Minuto de la muestra.
	 * @param money    Dinero en el minuto actual.
	 * @param clicks   Clicks manuales totales.
	 * @param autoGen  Generación automática total.
	 * @param maxProd  Producción máxima.
	 * @param expenses Gastos totales en mejoras.
	 */
	public void saveMinuteStat(int idGame, int minute, double money, int clicks, double autoGen, float maxProd, double expenses) throws SQLException {
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
			throw new SQLException(e);
		}
	}

	/**
	 * Recupera el historial completo de estadísticas de una partida.
	 *
	 * @param idGame ID de la partida a consultar.
	 * @return Lista de objetos Stat ordenada por minuto.
	 */
	public List<Stat> readByGame(int idGame) throws SQLException {
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
			throw new SQLException(e);
		}
		return stats;
	}

	/**
	 * Obtiene el último registro estadístico guardado para una partida específica.
	 *
	 * @param idGame ID de la partida.
	 * @return El objeto Stat más reciente o null si no hay registros.
	 */
	public Stat getLastMinuteStat(int idGame) throws SQLException {
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
			throw new SQLException(e);
		}
		return lastStat;
	}

}