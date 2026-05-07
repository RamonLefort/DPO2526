package Bussiness.Entities;

/**
 * Entidad que almacena los parámetros técnicos necesarios para establecer la conexión
 * con el motor de base de datos MySQL.
 */
public class Configuration {

	private String databaseHost;
	private String databaseName;
	private String databaseUsername;
	private String databasePassword;
	private int databasePort;

	/**
	 * Constructor completo para inicializar los parámetros de conexión.
	 *
	 * @param databaseHost Dirección IP o dominio del servidor de base de datos.
	 * @param databaseName Nombre de la base de datos a la que se desea conectar.
	 * @param databaseUsername Nombre de usuario con permisos de acceso al motor SQL.
	 * @param databasePassword Contraseña asociada al usuario proporcionado.
	 * @param databasePort Puerto de escucha del servicio de base de datos (típicamente 3306).
	 */
	public Configuration(String databaseHost, String databaseName, String databaseUsername, String databasePassword, int databasePort) {
		this.databaseHost = databaseHost;
		this.databaseName = databaseName;
		this.databaseUsername = databaseUsername;
		this.databasePassword = databasePassword;
		this.databasePort = databasePort;
	}

	/**
	 * Obtiene la dirección del host del servidor de base de datos.
	 * @return El nombre del host o dirección IP.
	 */
	public String getDatabaseHost() {
		return databaseHost;
	}

	/**
	 * Obtiene el nombre de la base de datos configurada para el sistema.
	 * @return El nombre de la base de datos.
	 */
	public String getDatabaseName() {
		return databaseName;
	}

	/**
	 * Obtiene el nombre de usuario registrado para la conexión.
	 * @return El identificador de usuario de la base de datos.
	 */
	public String getDatabaseUsername() {
		return databaseUsername;
	}

	/**
	 * Obtiene la contraseña de acceso al servidor de base de datos.
	 * @return La clave secreta de conexión.
	 */
	public String getDatabasePassword() {
		return databasePassword;
	}

	/**
	 * Obtiene el número de puerto configurado para la comunicación SQL.
	 * @return El puerto de red del servicio de base de datos.
	 */
	public int getDatabasePort() {
		return databasePort;
	}
}