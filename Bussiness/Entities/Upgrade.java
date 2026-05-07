package Bussiness.Entities;

/**
 * Representa una mejora específica aplicable a un generador.
 * Una vez activada, altera las propiedades de producción del generador asociado.
 */
public class Upgrade {

	private int idUpgrade;
	private int idGenerator;
	private int idGame;
	private boolean active;
	private int price;

	/**
	 * Constructor para gestionar mejoras en el sistema.
	 *
	 * @param idUpgrade Identificador único de la mejora.
	 * @param idGenerator Generador al que afecta esta mejora.
	 * @param idGame Partida en la que está disponible la mejora.
	 * @param active Indica si la mejora ha sido comprada y está en efecto.
	 * @param price Coste necesario para activar la mejora.
	 */
	public Upgrade(int idUpgrade, int idGenerator, int idGame, boolean active, int price) {
		this.idUpgrade = idUpgrade;
		this.idGenerator = idGenerator;
		this.idGame = idGame;
		this.active = active;
		this.price = price;
	}

	/**
	 * Obtiene el ID del generador asociado a la mejora.
	 * @return ID del generador asociado a la mejora.
	 */
	public int getIdGenerator() {
		return idGenerator;
	}

	/**
	 * Obtiene el ID del juego asociado a la mejora.
	 * @return ID del juego asociado a la mejora.
	 */
	public int getIdGame() {
		return idGame;
	}

	/**
	 * Actualiza el ID del generador asociado a la mejora.
	 * @param idGame ID del generador asociado a la mejora.
	 */
	public void setIdGame(int idGame) {
		this.idGame = idGame;
	}

	/**
	 * Indica si la mejora ha sido comprada y está activa.
	 * @return true si está activa, false de lo contrario.
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * Obtiene el coste de adquisición de la mejora.
	 * @return Precio de la mejora.
	 */
	public int getPrice() {
		return price;
	}
}
