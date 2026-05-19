package Persistance.Configuration;

import Bussiness.Entities.Configuration;

import java.io.IOException;
import java.sql.*;

/**
 * Clase Singleton encargada de gestionar la conexión con la base de datos MySQL.
 * Utiliza el patrón Singleton para garantizar que solo exista una instancia de
 * conexión a la base de datos en toda la aplicación, reduciendo el consumo de recursos.
 */
public class MySQLDAO {
	private static MySQLDAO instance;
	private Connection connection;

	private String url;
	private String username;
	private String password;

	/**
	 * Constructor de la conexión SQL
	 *
	 * @param config Configuración de la conexión
	 */
	private MySQLDAO(Configuration config) throws SQLException {
		if (config != null) {
			this.url = "jdbc:mysql://" + config.getDatabaseHost() + ":" +
					config.getDatabasePort() + "/" + config.getDatabaseName();
			this.username = config.getDatabaseUsername();
			this.password = config.getDatabasePassword();

			createDatabaseIfNotExists(config);
		}
	}

	private void createDatabaseIfNotExists(Configuration config) throws SQLException {

		String serverUrl = "jdbc:mysql://" + config.getDatabaseHost() + ":" + config.getDatabasePort();

		try (
				Connection tempConnection =
						DriverManager.getConnection(serverUrl, username, password);

				Statement statement = tempConnection.createStatement()
		) {

			String query = "CREATE DATABASE IF NOT EXISTS `" + config.getDatabaseName() + "` " +
					"DEFAULT CHARACTER SET utf8mb4 " +
					"COLLATE utf8mb4_general_ci";

			statement.executeUpdate(query);

			System.out.println("Base de datos verificada/creada correctamente.");
		}
	}

	/**
	 * Crea todas las tablas necesarias si no existen.
	 * Debe ejecutarse después de connect().
	 */
	public void createTablesIfNotExists() throws SQLException {

		try (Statement statement = connection.createStatement()) {

			statement.executeUpdate("""
            CREATE TABLE IF NOT EXISTS `user` (
                `username` VARCHAR(50) NOT NULL,
                `email` VARCHAR(100) NOT NULL,
                `password` VARCHAR(255) NOT NULL,
                PRIMARY KEY (`username`)
            ) ENGINE=InnoDB
            DEFAULT CHARSET=utf8mb4
            COLLATE=utf8mb4_general_ci
        """);

			statement.executeUpdate("""
            CREATE TABLE IF NOT EXISTS `game` (
                `id_game` INT NOT NULL AUTO_INCREMENT,
                `name_game` VARCHAR(100) DEFAULT NULL,
                `money` DOUBLE(100,2) DEFAULT 0.00,
                `hours` INT NOT NULL DEFAULT 0,
                `minutes` INT DEFAULT 0,
                `seconds` INT DEFAULT 0,
                `coffee_per_click` INT DEFAULT 1,
                `production_per_second` FLOAT NOT NULL DEFAULT 0,
                `username` VARCHAR(50) DEFAULT NULL,
                `finished` TINYINT(1) NOT NULL DEFAULT 0,

                PRIMARY KEY (`id_game`),

                KEY `username` (`username`),

                CONSTRAINT `game_ibfk_1`
                    FOREIGN KEY (`username`)
                    REFERENCES `user` (`username`)
                    ON DELETE CASCADE

            ) ENGINE=InnoDB
            DEFAULT CHARSET=utf8mb4
            COLLATE=utf8mb4_general_ci
        """);

			statement.executeUpdate("""
            CREATE TABLE IF NOT EXISTS `generador` (
                `id_generator` INT NOT NULL AUTO_INCREMENT,
                `name` VARCHAR(100) DEFAULT NULL,
                `id_game` INT DEFAULT NULL,
                `quantity` INT DEFAULT 0,
                `price` INT DEFAULT NULL,
                `period` DOUBLE(100,1) DEFAULT NULL,
                `earning` DOUBLE(100,1) DEFAULT NULL,

                PRIMARY KEY (`id_generator`),

                KEY `id_game` (`id_game`),

                CONSTRAINT `generador_ibfk_1`
                    FOREIGN KEY (`id_game`)
                    REFERENCES `game` (`id_game`)
                    ON DELETE CASCADE

            ) ENGINE=InnoDB
            DEFAULT CHARSET=utf8mb4
            COLLATE=utf8mb4_general_ci
        """);

			statement.executeUpdate("""
            CREATE TABLE IF NOT EXISTS `setting` (
                `id_setting` INT NOT NULL AUTO_INCREMENT,
                `volume` INT DEFAULT NULL,
                `background` VARCHAR(255) DEFAULT NULL,
                `skin` VARCHAR(255) DEFAULT NULL,
                `username` VARCHAR(50) DEFAULT NULL,

                PRIMARY KEY (`id_setting`),

                KEY `username` (`username`),

                CONSTRAINT `setting_ibfk_1`
                    FOREIGN KEY (`username`)
                    REFERENCES `user` (`username`)
                    ON DELETE CASCADE

            ) ENGINE=InnoDB
            DEFAULT CHARSET=utf8mb4
            COLLATE=utf8mb4_general_ci
        """);

			statement.executeUpdate("""
            CREATE TABLE IF NOT EXISTS `stat` (
                `id_stat` INT NOT NULL AUTO_INCREMENT,
                `id_games` INT NOT NULL,
                `minute_mark` INT NOT NULL,
                `money_at_minute` DOUBLE(100,2) NOT NULL,
                `manual_clicks_total` INT DEFAULT 0,
                `auto_generated_total` DOUBLE(100,2) DEFAULT 0.00,
                `max_production_rate` FLOAT DEFAULT 0,
                `upgrades_expenses` DOUBLE(100,2) DEFAULT 0.00,

                PRIMARY KEY (`id_stat`),

                KEY `id_games` (`id_games`),

                CONSTRAINT `stat_ibfk_1`
                    FOREIGN KEY (`id_games`)
                    REFERENCES `game` (`id_game`)
                    ON DELETE CASCADE

            ) ENGINE=InnoDB
            DEFAULT CHARSET=utf8mb4
            COLLATE=utf8mb4_general_ci
        """);

			statement.executeUpdate("""
            CREATE TABLE IF NOT EXISTS `upgrade` (
                `id_upgrade` INT NOT NULL AUTO_INCREMENT,
                `id_generator` INT DEFAULT NULL,
                `id_game` INT DEFAULT NULL,
                `active` TINYINT(1) NOT NULL DEFAULT 0,
                `price` INT DEFAULT NULL,

                PRIMARY KEY (`id_upgrade`),

                KEY `id_generator` (`id_generator`),
                KEY `id_game` (`id_game`),

                CONSTRAINT `upgrade_ibfk_1`
                    FOREIGN KEY (`id_generator`)
                    REFERENCES `generador` (`id_generator`)
                    ON DELETE CASCADE,

                CONSTRAINT `upgrade_ibfk_2`
                    FOREIGN KEY (`id_game`)
                    REFERENCES `game` (`id_game`)
                    ON DELETE CASCADE

            ) ENGINE=InnoDB
            DEFAULT CHARSET=utf8mb4
            COLLATE=utf8mb4_general_ci
        """);

			System.out.println("Tablas verificadas/creadas correctamente.");

		} catch (SQLException e) {

			throw new SQLException(
					"Error al crear las tablas: " + e.getMessage(),
					e
			);
		}
	}

	/**
	 * Obtiene la única instancia de la clase. Si no existe, la crea utilizando
	 * los parámetros de configuración leídos desde el archivo JSON.
	 *
	 * @param jsonDAO Objeto de acceso a datos para leer la configuración inicial.
	 * @return La instancia única de MySQLDAO.
	 */
	public static MySQLDAO getInstance(JsonConfigurationDAO jsonDAO) throws SQLException, IOException {
		if (instance == null) {
            Configuration config = null;
            try {
                config = jsonDAO.readJson();
            } catch (IOException e) {
                throw new IOException(e);
            }
            try {
                instance = new MySQLDAO(config);
            } catch (SQLException e) {
                throw new SQLException(e);
            }
        }
		return instance;
	}

	/**
	 * Establece la conexión física con el motor de base de datos.
	 * Es seguro llamarlo múltiples veces, ya que verifica si la conexión está cerrada previamente.
	 */
	public void connect() throws SQLException {
		try {
			if (connection == null || connection.isClosed()) {
				connection = DriverManager.getConnection(url, username, password);
				createTablesIfNotExists();

				System.out.println("Conexión exitosa a XAMPP");

			}
		} catch (SQLException e) {
			throw new SQLException(e);
		}
	}

	/**
	 * Ejecuta una consulta SELECT genérica filtrando por un atributo específico.
	 *
	 * @param nameTable Nombre de la tabla a consultar.
	 * @param column    Nombre de la columna para la cláusula WHERE.
	 * @param attribute Valor a buscar en la columna especificada.
	 * @return Un ResultSet con los datos obtenidos, o null si ocurre una SQLException.
	 */
	public ResultSet readSpecific(String nameTable, String column, String attribute) throws SQLException {
		try {
			String query = "SELECT * FROM " + nameTable + " WHERE " + column + " = ?";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, attribute);
			return statement.executeQuery();
		} catch (SQLException e) {
			throw new SQLException(e);
		}
	}

	/**
	 * Actualiza un campo específico de una tabla en base a una condición.
	 *
	 * @param table     Nombre de la tabla.
	 * @param field     Columna que se desea actualizar.
	 * @param value     Nuevo valor a establecer.
	 * @param refColumn Columna de referencia para la cláusula WHERE.
	 * @param refValue  Valor de referencia para la cláusula WHERE.
	 */
	public void updateField(String table, String field, String value, String refColumn, String refValue) throws SQLException {
		String query = "UPDATE " + table + " SET " + field + " = ? WHERE " + refColumn + " = ?";
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, value);
			statement.setString(2, refValue);
			statement.executeUpdate();
		} catch (SQLException e) {
			throw new SQLException(e);
		}
	}

	/**
	 * Elimina registros de una tabla específica basándose en la coincidencia de un atributo.
	 *
	 * @param nameTable El nombre de la tabla de la cual se eliminarán los datos.
	 * @param column    La columna que se evaluará para la eliminación.
	 * @param attribute El valor de la columna que determinará qué filas serán borradas.
	 */
	public void deleteObject(String nameTable, String column, String attribute) throws SQLException {
		String query = "DELETE FROM " + nameTable + " WHERE " + column + " = ?";
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, attribute);
			statement.executeUpdate();
		} catch (SQLException e) {
			throw new SQLException(e);
		}
	}

	/**
	 * Recupera todos los registros de una tabla.
	 *
	 * @param nameTable El nombre de la tabla a consultar completamente.
	 * @return Un {@link ResultSet} con todas las filas de la tabla, o null si la consulta falla.
	 */
	public ResultSet readAllTable(String nameTable) throws SQLException {
		try {
			Statement statement = connection.createStatement();
			return statement.executeQuery("SELECT * FROM " + nameTable);
		} catch (SQLException e) {
			throw new SQLException(e);
		}
	}

	/**
	 * Cierra de manera segura la conexión activa con la base de datos.
	 * Libera los recursos de red y previene fugas de memoria.
	 */
	public void disconnect() throws SQLException {
		try {
			if (connection != null && !connection.isClosed()) {
				connection.close();
				System.out.println("Conexión cerrada con éxito.");
			}
		} catch (SQLException e) {
			throw new SQLException(e);
		}
	}

	/**
	 * Obtiene el objeto de conexión JDBC subyacente.
	 * Permite a otras clases DAO reutilizar la misma conexión para ejecutar sus consultas.
	 *
	 * @return El objeto {@link Connection} actual hacia la base de datos.
	 */
	public Connection getConnection() {
		return connection;
	}
}