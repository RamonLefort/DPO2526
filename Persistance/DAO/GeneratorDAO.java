package Persistance.DAO;

import Bussiness.Entities.Generator;
import Persistance.Configuration.MySQLDAO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GeneratorDAO {
	private final MySQLDAO mySQLDAO;

	public GeneratorDAO(MySQLDAO mySQLDAO) {
		this.mySQLDAO = mySQLDAO;
	}

	/**
	 * Lee todos los generadores asociados a una partida específica.
	 * Hardcoding de valores iniciales si no existen en la DB.
	 */
	public List<Generator> readByGame(int idGame) {
		List<Generator> generators = new ArrayList<>();
		String query = "SELECT * FROM generador WHERE id_game = ?";

		try (PreparedStatement ps = mySQLDAO.getConnection().prepareStatement(query)) {
			ps.setInt(1, idGame);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Generator gen = new Generator(
						rs.getString("name"),
						rs.getInt("id_game")
				);
				gen.setIdGenerator(rs.getInt("id_generator"));
				gen.setQuantity(rs.getInt("quantity"));
				gen.setPrice(rs.getInt("price"));
				gen.setEarning(rs.getInt("earning"));
				gen.setPeriod(rs.getInt("period"));
				generators.add(gen);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return generators;
	}

	public void update(int idGame, Generator generator) {
		String query = "UPDATE generador SET quantity = ?, price = ? WHERE id_generator = ? AND id_game = ?";
		try (PreparedStatement ps = mySQLDAO.getConnection().prepareStatement(query)) {
			ps.setInt(1, generator.getQuantity());
			ps.setInt(2, generator.calcNextPrice());
			ps.setInt(3, generator.getIdGenerator());
			ps.setInt(4, idGame);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Crea el set inicial de generadores para una nueva partida.
	 * Se definen valores base de coste y producción para equilibrar el 'early game'.
	 *
	 * @param idGame El identificador de la partida recién creada.
	 * @return
	 */
	public List<Generator> createInitialGenerators(int idGame) {
		String query = "INSERT INTO generador (name, id_game, quantity, price, period, earning) VALUES (?, ?, ?, ?, ?, ?)";

		try (PreparedStatement ps = mySQLDAO.getConnection().prepareStatement(query)) {
			// Generador 1: Barista
			ps.setString(1, "Barista");
			ps.setInt(2, idGame);
			ps.setInt(3, 0);      // Cantidad inicial
			ps.setInt(4, 15);     // Precio inicial: 15 cafés
			ps.setInt(5, 5000);   // Periodo: 5 segundos
			ps.setInt(6, 1);      // Produce 1 café por periodo
			ps.addBatch();

			// Generador 2: Máquina de Espresso
			ps.setString(1, "Espresso Machine");
			ps.setInt(2, idGame);
			ps.setInt(3, 0);
			ps.setInt(4, 150);    // Precio inicial: 150 cafés
			ps.setInt(5, 3000);   // Periodo: 3 segundos
			ps.setInt(6, 2);      // Produce 2 cafés por periodo
			ps.addBatch();

			// Generador 3: Plantación de Café
			ps.setString(1, "Coffee Plantation");
			ps.setInt(2, idGame);
			ps.setInt(3, 0);
			ps.setInt(4, 2000);   // Precio inicial: 2K cafés
			ps.setInt(5, 1000);   // Periodo: 1 segundo
			ps.setInt(6, 1);     // Produce 1 cafés por periodo
			ps.addBatch();

			ps.executeBatch(); // Ejecución eficiente de las tres inserciones
		} catch (SQLException e) {
			System.err.println("Error al inicializar generadores: " + e.getMessage());
			e.printStackTrace();
		}
		return readByGame(idGame);
	}
}