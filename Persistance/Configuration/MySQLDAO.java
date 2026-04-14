package Persistance.Configuration;

import Bussiness.Entities.Configuration;
import java.sql.*;

public class MySQLDAO {
	private static MySQLDAO instance;
	private Connection connection;


	private String url;
	private String username;
	private String password;


	private MySQLDAO(Configuration config) {
		if (config != null) {
			this.url = "jdbc:mysql://" + config.getDatabaseHost() + ":" +
					config.getDatabasePort() + "/" + config.getDatabaseName();
			this.username = config.getDatabaseUsername();
			this.password = config.getDatabasePassword();
		}
	}


	public static MySQLDAO getInstance(JsonConfigurationDAO jsonDAO) {
		if (instance == null) {
			Configuration config = jsonDAO.readJson();
			instance = new MySQLDAO(config);
		}
		return instance;
	}

	public void connect() {
		try {
			if (connection == null || connection.isClosed()) {
				connection = DriverManager.getConnection(url, username, password);
				System.out.println("Conexión exitosa a XAMPP");
			}
		} catch (SQLException e) {
			System.err.println("Error al conectar: " + e.getMessage());
		}
	}

	public ResultSet readSpecific(String nameTable, String column, String attribute) {
		try {
			String query = "SELECT * FROM " + nameTable + " WHERE " + column + " = ?";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, attribute);
			return statement.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void updateField(String table, String field, String value, String refColumn, String refValue) {
		String query = "UPDATE " + table + " SET " + field + " = ? WHERE " + refColumn + " = ?";
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, value);
			statement.setString(2, refValue);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void deleteObject(String nameTable, String column, String attribute) {
		String query = "DELETE FROM " + nameTable + " WHERE " + column + " = ?";
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, attribute);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public ResultSet readAllTable(String nameTable) {
		try {

			Statement statement = connection.createStatement();
			return statement.executeQuery("SELECT * FROM " + nameTable);
		} catch (SQLException e) {
			System.err.println("Error al leer la tabla " + nameTable + ": " + e.getMessage());
			return null;
		}
	}

	public void disconnect() {
		try {
			if (connection != null && !connection.isClosed()) {
				connection.close();
				System.out.println("Conexión cerrada con éxito.");
			}
		} catch (SQLException e) {
			System.err.println("Error al desconectar: " + e.getMessage());
		}
	}

	public Connection getConnection() {
		return connection;
	}
}