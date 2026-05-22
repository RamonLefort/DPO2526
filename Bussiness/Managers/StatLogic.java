package Bussiness.Managers;

import Bussiness.Entities.Game;
import Bussiness.Entities.Stat;
import Bussiness.Entities.User;
import Bussiness.Exceptions.BusinessException;
import Persistance.DAO.GameDAO;
import Persistance.DAO.StatDAO;
import Persistance.DAO.UserDAO;
import Persistance.Exceptions.PersistanceException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase de lógica de negocio encargada de la gestión y análisis de estadísticas.
 */
public class StatLogic {

	private StatDAO statDAO;
	private GameDAO gameDAO;
	private UserDAO userDAO;

	/**
	 * Constructor que guarda las dependencias de persistencia necesarias para la gestión de las estadísticas.
	 *
	 * @param statDAO Acceso a datos de estadísticas.
	 * @param gameDAO Acceso a datos de partidas.
	 * @param userDAO Acceso a datos de usuarios.
	 */
	public StatLogic(StatDAO statDAO, GameDAO gameDAO, UserDAO userDAO) {
		this.statDAO = statDAO;
		this.gameDAO = gameDAO;
		this.userDAO = userDAO;
	}

	/**
	 * Recupera todos los nombres de usuario registrados en el sistema.
	 * Útil para rellenar el JComboBox primario de filtrado.
	 *
	 * @return Lista de cadenas con los nombres de usuario.
	 * @throws BusinessException Si falla la comunicación con la base de datos.
	 */
	public List<String> getAllUsernames() throws BusinessException {
		try {
			// Nota: Asumimos que tu userDAO expone un método para listar usuarios o cadenas
			return userDAO.getAllUsernames();
		} catch (PersistanceException e) {
			throw new BusinessException(e);
		}
	}

	/**
	 * Obtiene el nombre del usuario propietario de una partida concreta.
	 * Permite auto-seleccionar al usuario activo al abrir la vista.
	 *
	 * @param idGame Identificador de la partida.
	 * @return El nombre de usuario del propietario.
	 * @throws BusinessException Si ocurre un error en la consulta física.
	 */
	public String getGameOwner(int idGame) throws BusinessException {
		try {
			// Buscamos la partida directamente en el DAO
			Game game = gameDAO.getGameById(idGame);
			return game.getUsername(); // Retorna el dueño asignado de la partida
		} catch (PersistanceException e) {
			throw new BusinessException(e);
		}
	}

	/**
	 * Recupera exclusivamente los nombres legibles de las partidas de un usuario.
	 * Diseñado para rellenar el JComboBox secundario de la vista.
	 *
	 * @param username Nombre del usuario a filtrar.
	 * @return Lista de Strings con los nombres de sus partidas.
	 * @throws BusinessException Si falla la persistencia.
	 */
	public List<String> getFinishedGameNamesByUser(String username) throws BusinessException {
		try {
			List<Game> games = gameDAO.getFinishedGamesByUser(username);
			List<String> names = new ArrayList<>();
			for (Game g : games) {
				names.add(g.getNameGame());
			}
			return names;
		} catch (PersistanceException e) {
			throw new BusinessException(e);
		}
	}

	/**
	 * Recupera los identificadores (IDs) numéricos de las partidas de un usuario.
	 * Sincroniza la posición del índice visual con el ID real de la base de datos.
	 *
	 * @param username Nombre del usuario.
	 * @return Lista de enteros con los IDs de las partidas.
	 * @throws BusinessException Si falla la consulta SQL.
	 */
	public List<Integer> getFinishedGameIdsByUser(String username) throws BusinessException {
		try {
			List<Game> games = gameDAO.getFinishedGamesByUser(username);
			List<Integer> ids = new ArrayList<>();
			for (Game g : games) {
				ids.add(g.getIdGame());
			}
			return ids;
		} catch (PersistanceException e) {
			throw new BusinessException(e);
		}
	}

	/**
	 * Resuelve el ID único de una partida cruzando su nombre y el de su propietario.
	 *
	 * @param gameName Nombre de la partida seleccionado en el combo.
	 * @param username Dueño de la partida seleccionado en el combo.
	 * @return El ID numérico de la partida.
	 * @throws BusinessException Si no se encuentra correspondencia o falla el canal.
	 */
	public int getGameIdByNameAndUser(String gameName, String username) throws BusinessException {
		try {
			List<Game> userGames = gameDAO.getGamesByUser(username);
			for (Game g : userGames) {
				if (g.getNameGame().equalsIgnoreCase(gameName)) {
					return g.getIdGame();
				}
			}
		} catch (PersistanceException e) {
			throw new BusinessException(e);
		}
		return 0;
	}

	/**
	 * Recupera el historial completo de estadísticas registradas para una partida específica.
	 *
	 * @param idGame Identificador único de la partida.
	 * @return Lista de objetos {@link Stat} que representan la evolución temporal del juego.
	 */
	public List<Stat> getAllStats(int idGame) throws BusinessException {
        try {
            return statDAO.readByGame(idGame);
        } catch (PersistanceException e) {
            throw new BusinessException(e);
        }
    }

	/**
	 * Obtiene todas las partidas que el usuario ha completado satisfactoriamente.
	 *
	 * @param username Nombre del usuario propietario de las partidas.
	 * @return Listado de objetos {@link Game} cuyo estado es finalizado.
	 */
	public List<Game> getFinishedGames(String username) throws BusinessException {
        try {
            return gameDAO.readFinishedGames(username);
        } catch (PersistanceException e) {
            throw new BusinessException(e);
        }
    }

	/**
	 * Registra una instantánea del estado económico y de producción del jugador en un minuto concreto.
	 *
	 * @param idGame Identificador de la partida.
	 * @param minute Marcador temporal de la muestra.
	 * @param money Dinero acumulado en ese instante.
	 * @param clicks Total de clicks manuales realizados.
	 * @param autogen Cantidad de recursos generados automáticamente.
	 * @param maxprod Tasa máxima de producción alcanzada.
	 * @param expenses Inversión total realizada en mejoras.
	 */
	public void saveStat(int idGame, int minute, double money, int clicks, double autogen, float maxprod, double expenses) throws BusinessException {
        try {
            statDAO.saveMinuteStat(idGame, minute, money, clicks, autogen, maxprod, expenses);
        } catch (PersistanceException e) {
            throw new BusinessException(e);
        }
    }

	/**
	 * Inicializa el registro estadístico para una nueva partida con valores a cero.
	 *
	 * @param idGame Identificador de la partida recién creada.
	 */
	public void createStat(int idGame) throws BusinessException {
        try {
            statDAO.create(idGame, 0, 0, 0, 0, 0, 0);
        } catch (PersistanceException e) {
            throw new BusinessException(e);
        }
    }

	/**
	 * Recupera la métrica más reciente guardada para una partida.
	 *
	 * @param idGame Identificador de la partida.
	 * @return El objeto {@link Stat} más actual o null si no existen registros.
	 */
	public Stat getLastMinuteStat(int idGame) throws BusinessException {
        try {
            return statDAO.getLastMinuteStat(idGame);
        } catch (PersistanceException e) {
            throw new BusinessException(e);
        }
    }

	/**
	 * Obtiene el listado global de usuarios registrados en el sistema.
	 *
	 * @return Lista con todos los objetos {@link User} de la base de datos.
	 */
	public List<User> getAllUsers() throws BusinessException {
        try {
            return userDAO.readAllUsers();
        } catch (PersistanceException e) {
            throw new BusinessException(e);
        }
    }
}