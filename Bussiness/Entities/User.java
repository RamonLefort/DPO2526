package Bussiness.Entities;

/**
 * Entidad que representa a un usuario dentro del sistema.
 * Almacena la información de identidad necesaria para el inicio de sesión
 * y la vinculación con sus partidas guardadas.
 */
public class User {

	private String username;
	private String email;
	private String password;

	/**
	 * Constructor completo para instanciar un usuario.
	 *
	 * @param username Nombre único de identificación en el sistema.
	 * @param email Dirección de correo electrónico asociada a la cuenta.
	 * @param password Contraseña (se recomienda que esté hasheada antes de la instanciación).
	 */
	public User(String username, String email, String password) {
		this.username = username;
		this.email    = email;
		this.password = password;
	}

	/**
	 * Obtiene el nombre de usuario único en el sistema.
	 * @return El nombre de usuario.
	 */
	public String getUsername() { return username; }

	/**
	 * Actualiza el nombre de usuario.
	 * @param username Nuevo nombre de usuario único.
	 */
	public void setUsername(String username) { this.username = username; }

	/**
	 * Obtiene la dirección de correo electrónico del usuario.
	 * @return El email registrado.
	 */
	public String getEmail() { return email; }

	/**
	 * Obtiene la contraseña (normalmente en formato hash).
	 * @return La contraseña almacenada.
	 */
	public String getPassword() { return password; }
}