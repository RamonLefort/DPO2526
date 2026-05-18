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
	private MySQLDAO(Configuration config) {
		if (config != null) {
			this.url = "jdbc:mysql://" + config.getDatabaseHost() + ":" +
					config.getDatabasePort() + "/" + config.getDatabaseName();
			this.username = config.getDatabaseUsername();
			this.password = config.getDatabasePassword();
		}
	}

	/**
	 * Obtiene la única instancia de la clase. Si no existe, la crea utilizando
	 * los parámetros de configuración leídos desde el archivo JSON.
	 *
	 * @param jsonDAO Objeto de acceso a datos para leer la configuración inicial.
	 * @return La instancia única de MySQLDAO.
	 */
	public static MySQLDAO getInstance(JsonConfigurationDAO jsonDAO) throws IOException {
		if (instance == null) {
			Configuration config = jsonDAO.readJson();
			instance = new MySQLDAO(config);
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