package Bussiness.Entities;

/**
 * Representa una instancia de juego o partida guardada.
 * Contiene el estado económico, el progreso temporal y la configuración
 * de producción actual del jugador.
 */
public class Game {

	private int idGame;
	private String nameGame;
	private double money;
	private int hours;
	private int minutes;
	private int seconds;
	private int coffeePerClick;
	private String username;
	private float production_per_sec;
	private boolean finished;

	/**
	 * Constructor para reconstruir una partida desde la base de datos.
	 *
	 * @param idGame Identificador único de la partida.
	 * @param nameGame Nombre descriptivo de la partida.
	 * @param money Cantidad actual de moneda acumulada.
	 * @param hours Horas totales de juego transcurridas.
	 * @param minutes Minutos transcurridos en el contador actual.
	 * @param seconds Segundos transcurridos en el contador actual.
	 * @param coffeePerClick Cantidad de café generado por cada click manual.
	 * @param production_per_sec Tasa de producción automática por segundo.
	 * @param username Propietario de la partida.
	 * @param finished Estado de finalización de la partida.
	 */
	public Game(int idGame, String nameGame, double money, int hours, int minutes, int seconds, int coffeePerClick, float production_per_sec, String username, boolean finished) {
		this.idGame = idGame;
		this.nameGame = nameGame;
		this.money = money;
		this.hours = hours;
		this.minutes = minutes;
		this.seconds = seconds;
		this.coffeePerClick = coffeePerClick;
		this.username = username;
		this.production_per_sec = production_per_sec;
		this.finished = finished;
	}

	/**
	 * Obtiene el identificador único de la partida en la base de datos.
	 * @return ID de la partida.
	 */
	public int getIdGame() { return idGame; }

	/**
	 * Obtiene la cantidad de moneda acumulada actualmente.
	 * @return Saldo de la partida.
	 */
	public double getMoney() { return money; }

	/**
	 * Obtiene la tasa de producción automática por segundo.
	 * @return Producción pasiva actual.
	 */
	public float getProduction_per_sec() { return production_per_sec; }

	/**
	 * Obtiene el nombre de la partida en la base de datos.
	 * @return El nombre de la partida.
	 */
	public String getNameGame()       { return nameGame; }

	/**
	 * Obtiene las horas de la partida en la base de datos.
	 * @return Las horas de la partida.
	 */
	public int getHours()           { return hours; }

	/**
	 * Obtiene los minutos de la partida en la base de datos.
	 * @return Los minutos de la partida.
	 */
	public int getMinutes()           { return minutes; }

	/**
	 * Obtiene los segundos de la partida en la base de datos.
	 * @return Los segundos de la partida.
	 */
	public int getSeconds()           { return seconds; }

	/**
	 * Obtiene los coffeePerClick de la partida en la base de datos.
	 * @return Los coffeePerClick de la partida.
	 */
	public int getCoffeePerClick()    { return coffeePerClick; }

	/**
	 * Obtiene nombre de usuario de la partida en la base de datos.
	 * @return El nombre de usuario de la partida.
	 */
	public String getUsername()       { return username; }

	/**
	 * Añade una cantidad de dinero a la partida.
	 * @param amount Dinero añadido al total.
	 */
	public void addMoney(double amount)         { this.money += amount; }

	/**
	 * Actualiza el saldo de dinero de la partida.
	 * @param money Nuevo saldo total.
	 */
	public void setMoney(double money) { this.money = money; }

	/**
	 * Actualiza la hora de la partida.
	 * @param hours Nueva hora.
	 */
	public void setHours(int hours)         { this.hours = hours; }

	/**
	 * Actualiza los minutos de la partida.
	 * @param minutes Nuevo minuto.
	 */
	public void setMinutes(int minutes)         { this.minutes = minutes; }

	/**
	 * Actualiza los segundos de la partida.
	 * @param seconds Nuevos segundos.
	 */
	public void setSeconds(int seconds)         { this.seconds = seconds; }

	/**
	 * Actualiza la producción por segundo de la partida.
	 * @param production_per_sec Nueva producción por segundo.
	 */
	public void setProduction_per_sec(float production_per_sec) {
		this.production_per_sec = production_per_sec;
	}
}