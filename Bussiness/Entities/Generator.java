package Bussiness.Entities;

/**
 * Entidad que representa un generador de recursos en el juego.
 * Los generadores son los activos que el usuario compra para obtener
 * una producción automática de café a lo largo del tiempo.
 */
public class Generator {

	private int idGenerator;
	private String name;
	private int idGame;
	private int quantity;
	private int price;
	private int period;
	private int earning;

	/**
	 * Constructor básico para inicializar un generador vinculado a una partida.
	 *
	 * @param name Nombre del tipo de generador.
	 * @param idGame ID de la partida a la que pertenece.
	 */
	public Generator(String name, int idGame) {
		this.name = name;
		this.idGame = idGame;
	}

	/**
	 * Obtiene el nombre del tipo de generador (ej. "Barista").
	 * @return Nombre del generador.
	 */
	public String getName() { return name; }

	/**
	 * Obtiene la cantidad de unidades que posee el jugador de este generador.
	 * @return Cantidad de generadores comprados.
	 */
	public int getQuantity() { return quantity; }

	/**
	 * Actualiza la cantidad de generadores poseídos.
	 * @param quantity Nuevo número de unidades.
	 */
	public void setQuantity(int quantity) { this.quantity = quantity; }

	/**
	 * Obtiene el precio actual de compra para la siguiente unidad.
	 * @return Precio en café/moneda.
	 */
	public int getPrice() { return price; }

	/**
	 * Actualiza el precio del generador (usado tras cada compra).
	 * @param price Nuevo precio calculado.
	 */
	public void setPrice(int price) { this.price = price; }

	/**
	 * Obtiene el ID de este generador.
	 * @return ID del generador.
	 */
	public int getIdGenerator() {
		return idGenerator;
	}

	/**
	 * Obtiene el periodo de este generador.
	 * @return Periodo del generador.
	 */
	public int getPeriod(){return period;}

	/**
	 * Obtiene el ID del juego al que está asociado el generador.
	 * @return ID del juego al que está asociado el generador.
	 */
	public int getIdGame() {
		return idGame;
	}

	/**
	 * Obtiene la ganancia del generador.
	 * @return La ganancia del generador.
	 */
	public int getEarning() {
		return earning;
	}

	/**
	 * Actualiza el ID del generador.
	 * @param idGenerator Nuevo ID del generador.
	 */
	public void setIdGenerator(int idGenerator) {
		this.idGenerator = idGenerator;
	}

	/**
	 * Actualiza el periodo del generador
	 * @param period Nuevo periodo del generador.
	 */
	public void setPeriod(int period) {
		this.period = period;
	}

	/**
	 * Actualiza la ganancia del generador.
	 * @param earning Nueva ganancia del generador.
	 */
	public void setEarning(int earning) {
		this.earning = earning;
	}
}
