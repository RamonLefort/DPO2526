package Bussiness.Managers;

import Bussiness.Entities.Game;
import Bussiness.Entities.Generator;
import Bussiness.Entities.Upgrade;
import Persistance.DAO.GameDAO;
import Persistance.DAO.GeneratorDAO;
import Persistance.DAO.UpgradeDAO;

import java.util.List;

/**
 * Clase encargada de gestionar la lógica de negocio central del juego.
 * Coordina las interacciones entre las entidades de juego, generadores y mejoras,
 * asegurando que las reglas de negocio se apliquen antes de realizar operaciones
 * de persistencia a través de los DAOs.
 */
public class GameLogic {

	private GameDAO gameDAO;
	private GeneratorDAO generatorDAO;
	private UpgradeDAO upgradeDAO;

	/**
	 * Constructor que inicializa los servicios de persistencia necesarios.
	 *
	 * @param gameDAO Acceso a datos para partidas.
	 * @param generatorDAO Acceso a datos para generadores.
	 * @param upgradeDAO Acceso a datos para mejoras.
	 */
	public GameLogic(GameDAO gameDAO, GeneratorDAO generatorDAO, UpgradeDAO upgradeDAO) {
		this.gameDAO = gameDAO;
		this.generatorDAO = generatorDAO;
		this.upgradeDAO = upgradeDAO;
	}

	/**
	 * Crea una nueva partida validando que el nombre no esté repetido para el usuario.
	 *
	 * @param nameGame Nombre deseado para la partida.
	 * @param username Usuario que crea la partida.
	 * @return El ID de la partida creada, o -1 si ya existe una partida con ese nombre.
	 */
	public int createGame(String nameGame, String username) {
		List<Game> userGames = gameDAO.getGamesByUser(username);
		for (int i = 0; i < userGames.size(); i++) {
			if (userGames.get(i).getNameGame().equalsIgnoreCase(nameGame)) {
				return -1;
			}
		}
		return gameDAO.createGame(nameGame, username);
	}

	/**
	 * Guarda el progreso actual de la partida en la base de datos.
	 *
	 * @param username Nombre del usuario.
	 * @param idGame Identificador de la partida.
	 * @param money Dinero actual.
	 * @param hours Horas transcurridas.
	 * @param minutes Minutos transcurridos.
	 * @param seconds Segundos transcurridos.
	 * @param coffeexclick Cafés obtenidos por click manual.
	 * @param prodxsec Tasa de producción automática por segundo.
	 */
	public void saveGame(String username, int idGame, double money, int hours, int minutes, int seconds, int coffeexclick, float prodxsec) {
		gameDAO.updateGame(username, idGame, money, hours, minutes, seconds, coffeexclick, prodxsec);
	}

	/**
	 * Elimina una partida específica del sistema.
	 *
	 * @param idGame ID de la partida a borrar.
	 */
	public void deleteGame(int idGame) {
		gameDAO.deleteGame(idGame);
	}

	/**
	 * Marca una partida como finalizada.
	 *
	 * @param idGame ID de la partida a terminar.
	 */
	public void finishGame(int idGame){gameDAO.finishGame(idGame);}

	/**
	 * Carga una partida específica buscando entre las partidas del usuario.
	 *
	 * @param username Usuario propietario.
	 * @param idGame ID de la partida buscada.
	 * @return El objeto {@link Game} correspondiente.
	 * @throws IllegalArgumentException si no se encuentra la partida.
	 */
    public Game loadGame(String username, int idGame) {
        List<Game> games = gameDAO.getGamesByUser(username);
        for (int i = 0; i < games.size(); i++) {
            if (games.get(i).getIdGame() == idGame) {
                return games.get(i);
            }
        }
        throw new IllegalArgumentException("Game not found with id: " + idGame);
    }

	/**
	 * Obtiene todas las partidas asociadas a un usuario.
	 *
	 * @param username Nombre del usuario.
	 * @return Lista de partidas {@link Game}.
	 */
	public List<Game> getUserGames(String username) {
		return gameDAO.getGamesByUser(username);
	}

	/**
	 * Verifica globalmente si un nombre de partida ya existe.
	 *
	 * @param nameGame Nombre a verificar.
	 * @return true si el nombre ya está registrado.
	 */
	public boolean gameNameExists(String nameGame) {
		return gameDAO.existsByName(nameGame);
	}

	/**
	 * Obtiene los generadores configurados para una partida.
	 *
	 * @param idGame ID de la partida.
	 * @return Lista de generadores {@link Generator}.
	 */
	public List<Generator> getGenerators(int idGame){
		return generatorDAO.readByGame(idGame);
	}

	/**
	 * Inicializa los generadores base para una nueva partida.
	 *
	 * @param idGame ID de la partida recién creada.
	 * @return Lista de los generadores creados.
	 */
	public List<Generator> createGenerators(int idGame){
		return generatorDAO.createInitialGenerators(idGame);
	}

	/**
	 * Actualiza el estado de un generador tras una compra o cambio de nivel.
	 *
	 * @param idGame ID de la partida.
	 * @param generator Objeto generador con los datos actualizados.
	 */
	public void updateGenerators(int idGame, Generator generator){
		generatorDAO.update(idGame, generator);
	}

	/**
	 * Crea las mejoras iniciales vinculándolas correctamente con los IDs de los
	 * generadores recién creados para esa partida.
	 *
	 * @param idGame ID de la partida.
	 * @param generators Lista de generadores de los cuales se extraerán los IDs.
	 */
	public void createUpgrades(int idGame, List<Generator> generators){
		int idBarista = 0, idMachine = 0, idPlantation = 0;
		for(Generator g: generators){
			if(g.getName().equals("Barista")){
				idBarista = g.getIdGenerator();
			}else if(g.getName().equals("Espresso Machine")){
				idMachine = g.getIdGenerator();
			}else if(g.getName().equals("Coffee Plantation")){
				idPlantation = g.getIdGenerator();
			}
		}
		upgradeDAO.createInitialUpgrades(idGame, idBarista, idMachine, idPlantation);
	}

	/**
	 * Activa una mejora específica para un generador.
	 *
	 * @param idGame ID de la partida.
	 * @param idGenerator ID del generador cuya mejora se activa.
	 */
	public void updateUpgrades(int idGame, int idGenerator){
		upgradeDAO.update(idGame, idGenerator, true);
	}

	/**
	 * Obtiene el listado de mejoras de una partida.
	 *
	 * @param idGame ID de la partida.
	 * @return Lista de mejoras {@link Upgrade}.
	 */
	public List<Upgrade> getUpgrades(int idGame){
		return upgradeDAO.readByGame(idGame);
	}
}