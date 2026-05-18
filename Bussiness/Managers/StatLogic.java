package Bussiness.Managers;

import Bussiness.Entities.Game;
import Bussiness.Entities.Stat;
import Bussiness.Entities.User;
import Bussiness.Exceptions.DAOException;
import Persistance.DAO.GameDAO;
import Persistance.DAO.StatDAO;
import Persistance.DAO.UserDAO;

import java.sql.SQLException;
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
	 * Recupera el historial completo de estadísticas registradas para una partida específica.
	 *
	 * @param idGame Identificador único de la partida.
	 * @return Lista de objetos {@link Stat} que representan la evolución temporal del juego.
	 */
	public List<Stat> getAllStats(int idGame) throws DAOException {
        try {
            return statDAO.readByGame(idGame);
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

	/**
	 * Obtiene todas las partidas que el usuario ha completado satisfactoriamente.
	 *
	 * @param username Nombre del usuario propietario de las partidas.
	 * @return Listado de objetos {@link Game} cuyo estado es finalizado.
	 */
	public List<Game> getFinishedGames(String username) throws DAOException {
        try {
            return gameDAO.readFinishedGames(username);
        } catch (SQLException e) {
            throw new DAOException(e);
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
	public void saveStat(int idGame, int minute, double money, int clicks, double autogen, float maxprod, double expenses) throws DAOException {
        try {
            statDAO.saveMinuteStat(idGame, minute, money, clicks, autogen, maxprod, expenses);
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

	/**
	 * Inicializa el registro estadístico para una nueva partida con valores a cero.
	 *
	 * @param idGame Identificador de la partida recién creada.
	 */
	public void createStat(int idGame) throws DAOException {
        try {
            statDAO.create(idGame, 0, 0, 0, 0, 0, 0);
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

	/**
	 * Recupera la métrica más reciente guardada para una partida.
	 *
	 * @param idGame Identificador de la partida.
	 * @return El objeto {@link Stat} más actual o null si no existen registros.
	 */
	public Stat getLastMinuteStat(int idGame) throws DAOException {
        try {
            return statDAO.getLastMinuteStat(idGame);
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

	/**
	 * Obtiene el listado global de usuarios registrados en el sistema.
	 *
	 * @return Lista con todos los objetos {@link User} de la base de datos.
	 */
	public List<User> getAllUsers() throws DAOException {
        try {
            return userDAO.readAllUsers();
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }
}