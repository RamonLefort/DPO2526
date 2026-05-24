package Bussiness.Managers;

import Bussiness.Entities.Game;
import Bussiness.Entities.Generator;
import Bussiness.Entities.Upgrade;
import Bussiness.Exceptions.BusinessException;
import Persistance.DAO.GameDAO;
import Persistance.DAO.GeneratorDAO;
import Persistance.DAO.UpgradeDAO;
import Persistance.Exceptions.PersistanceException;

import java.sql.SQLException;
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
	 * @throws BusinessException Si el proceso de persistencia falla durante la validación o la creación de la partida.
	 */
	public int createGame(String nameGame, String username) throws BusinessException {
        List<Game> userGames = null;
        try {
            userGames = gameDAO.getGamesByUser(username);
        } catch (PersistanceException e) {
            throw new BusinessException(e);
        }
        for (int i = 0; i < userGames.size(); i++) {
			if (userGames.get(i).getNameGame().equalsIgnoreCase(nameGame)) {
				return -1;
			}
		}
        try {
            return gameDAO.createGame(nameGame, username);
        } catch (PersistanceException e) {
            throw new BusinessException(e);
        }
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
     * @throws BusinessException Si el proceso de persistencia falla durante la validación o la creación de la partida.
	 */
	public void saveGame(String username, int idGame, double money, int hours, int minutes, int seconds, int coffeexclick, float prodxsec) throws BusinessException {
        try {
            gameDAO.updateGame(username, idGame, money, hours, minutes, seconds, coffeexclick, prodxsec);
        } catch (PersistanceException e) {
            throw new BusinessException(e);
        }
    }

	/**
	 * Elimina una partida específica del sistema.
	 *
	 * @param idGame ID de la partida a borrar.
	 * @throws BusinessException Si el proceso de persistencia falla durante la validación o la creación de la partida.
	 */
	public void deleteGame(int idGame) throws BusinessException {
		gameDAO.deleteGame(idGame);
	}

	/**
	 * Marca una partida como finalizada.
	 *
	 * @param idGame ID de la partida a terminar.
	 * @throws BusinessException Si el proceso de persistencia falla durante la validación o la creación de la partida.
	 */
	public void finishGame(int idGame) throws BusinessException {
        try {
            gameDAO.finishGame(idGame);
        } catch (PersistanceException e) {
            throw new BusinessException(e);
        }
    }

	/**
	 * Carga una partida específica buscando entre las partidas del usuario.
	 *
	 * @param username Usuario propietario.
	 * @param idGame ID de la partida buscada.
	 * @return El objeto {@link Game} correspondiente.
	 * @throws BusinessException Si el proceso de persistencia falla durante la validación o la creación de la partida.
	 */
    public Game loadGame(String username, int idGame) throws BusinessException {
        List<Game> games = null;
        try {
            games = gameDAO.getGamesByUser(username);
        } catch (PersistanceException e) {
            throw new BusinessException(e);
        }
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
	 * @throws BusinessException Si el proceso de persistencia falla durante la validación o la creación de la partida.
	 */
	public List<Game> getUserGames(String username) throws BusinessException {
        try {
            return gameDAO.getGamesByUser(username);
        } catch (PersistanceException e) {
            throw new BusinessException(e);
        }
    }

	/**
	 * Verifica globalmente si un nombre de partida ya existe.
	 *
	 * @param nameGame Nombre a verificar.
	 * @return true si el nombre ya está registrado.
	 * @throws BusinessException Si el proceso de persistencia falla durante la validación o la creación de la partida.
	 */
	public boolean gameNameExists(String nameGame) throws BusinessException {
        try {
            return gameDAO.existsByName(nameGame);
        } catch (PersistanceException e) {
            throw new BusinessException(e);
        }
    }

	/**
	 * Obtiene los generadores configurados para una partida.
	 *
	 * @param idGame ID de la partida.
	 * @return Lista de generadores {@link Generator}.
	 * @throws BusinessException Si el proceso de persistencia falla durante la validación o la creación de la partida.
	 */
	public List<Generator> getGenerators(int idGame) throws BusinessException {
        try {
            return generatorDAO.readByGame(idGame);
        } catch (PersistanceException e) {
            throw new BusinessException(e);
        }
    }

	/**
	 * Inicializa los generadores base para una nueva partida.
	 *
	 * @param idGame ID de la partida recién creada.
	 * @return Lista de los generadores creados.
	 * @throws BusinessException Si el proceso de persistencia falla durante la validación o la creación de la partida.
	 */
	public List<Generator> createGenerators(int idGame) throws BusinessException {
        try {
            return generatorDAO.createInitialGenerators(idGame);
        } catch (PersistanceException e) {
            throw new BusinessException(e);
        }
    }

	/**
	 * Actualiza el estado de un generador tras una compra o cambio de nivel.
	 *
	 * @param idGame ID de la partida.
	 * @param generator Objeto generador con los datos actualizados.
	 * @throws BusinessException Si el proceso de persistencia falla durante la validación o la creación de la partida.
	 */
	public void updateGenerators(int idGame, Generator generator) throws BusinessException {
        try {
            generatorDAO.update(idGame, generator);
        } catch (PersistanceException e) {
            throw new BusinessException(e);
        }
    }

	/**
	 * Crea las mejoras iniciales vinculándolas correctamente con los IDs de los
	 * generadores recién creados para esa partida.
	 *
	 * @param idGame ID de la partida.
	 * @param generators Lista de generadores de los cuales se extraerán los IDs.
	 * @throws BusinessException Si el proceso de persistencia falla durante la validación o la creación de la partida.
	 */
	public void createUpgrades(int idGame, List<Generator> generators) throws BusinessException {
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
        try {
            upgradeDAO.createInitialUpgrades(idGame, idBarista, idMachine, idPlantation);
        } catch (PersistanceException e) {
            throw new BusinessException(e);
        }
    }

	/**
	 * Activa una mejora específica para un generador.
	 *
	 * @param idGame ID de la partida.
	 * @param idGenerator ID del generador cuya mejora se activa.
	 * @throws BusinessException Si el proceso de persistencia falla durante la validación o la creación de la partida.
	 */
	public void updateUpgrades(int idGame, int idGenerator) throws BusinessException {
        try {
            upgradeDAO.update(idGame, idGenerator, true);
        } catch (PersistanceException e) {
            throw new BusinessException(e);
        }
    }

	/**
	 * Obtiene el listado de mejoras de una partida.
	 *
	 * @param idGame ID de la partida.
	 * @return Lista de mejoras {@link Upgrade}.
	 * @throws BusinessException Si el proceso de persistencia falla durante la validación o la creación de la partida.
	 */
	public List<Upgrade> getUpgrades(int idGame) throws BusinessException {
        try {
            return upgradeDAO.readByGame(idGame);
        } catch (PersistanceException e) {
            throw new BusinessException(e);
        }
    }
}