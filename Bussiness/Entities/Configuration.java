package Bussiness.Entities;

public class Configuration {

	private String databaseHost;
	private String databaseName;
	private String databaseUsername;
	private String databasePassword;
	private int databasePort;

	public Configuration(String databaseHost, String databaseName, String databaseUsername, String databasePassword, int databasePort) {
		this.databaseHost = databaseHost;
		this.databaseName = databaseName;
		this.databaseUsername = databaseUsername;
		this.databasePassword = databasePassword;
		this.databasePort = databasePort;
	}

	public String getDatabaseHost() {
		return databaseHost;
	}

	public String getDatabaseName() {
		return databaseName;
	}

	public String getDatabaseUsername() {
		return databaseUsername;
	}

	public String getDatabasePassword() {
		return databasePassword;
	}

	public int getDatabasePort() {
		return databasePort;
	}
}