package Persistance.DAO;

import Bussiness.Entities.Generator;
import Persistance.Configuration.MySQLDAO;
import Persistance.Exceptions.PersistanceException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object para la entidad Generator.
 * Se encarga de gestionar la persistencia de los generadores
 * que el jugador compra para generar recursos de forma pasiva en el juego.
 */
public class GeneratorDAO {
	private final MySQLDAO mySQLDAO;

	/**
	 * Constructor que guarda la dependencia de conexión.
	 *
	 * @param mySQLDAO Objeto que provee la conexión activa a la base de datos MySQL.
	 */
	public GeneratorDAO(MySQLDAO mySQLDAO) {
		this.mySQLDAO = mySQLDAO;
	}

	/**
	 * Extrae de la base de datos todos los generadores asociados a una partida específica.
	 *
	 * @param idGame El identificador único de la partida.
	 * @return Una lista de objetos {@link Generator} poblados con sus estadísticas actuales.
	 * @throws PersistanceException Si ocurre un error de comunicación o ejecución durante la inserción en la base de datos.
	 */
	public List<Generator> readByGame(int idGame) throws PersistanceException {
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
			throw new PersistanceException(e);
		}
		return generators;
	}

	/**
	 * Actualiza el estado de un generador en la base de datos tras una compra por parte del jugador.
	 *
	 * @param idGame    ID de la partida a la que pertenece el generador.
	 * @param generator Objeto {@link Generator} con los nuevos valores de cantidad y precio.
	 * @throws PersistanceException Si ocurre un error de comunicación o ejecución durante la inserción en la base de datos.
	 */
	public void update(int idGame, Generator generator) throws PersistanceException {
		String query = "UPDATE generador SET quantity = ?, price = ? WHERE id_generator = ? AND id_game = ?";
		try (PreparedStatement ps = mySQLDAO.getConnection().prepareStatement(query)) {
			ps.setInt(1, generator.getQuantity());
			ps.setInt(2, generator.getPrice());
			ps.setInt(3, generator.getIdGenerator());
			ps.setInt(4, idGame);
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new PersistanceException(e);
		}
	}

	/**
	 * Crea el modelo base de los generadores al inicio de una nueva partida.
	 *
	 * @param idGame El identificador de la partida recién creada.
	 * @return La lista de generadores recién insertados.
	 * @throws PersistanceException Si ocurre un error de comunicación o ejecución durante la inserción en la base de datos.
	 */
	public List<Generator> createInitialGenerators(int idGame) throws PersistanceException {
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
			throw new PersistanceException(e);
		}
		return readByGame(idGame);
	}
}