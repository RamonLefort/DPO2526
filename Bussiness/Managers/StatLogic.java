package Bussiness.Managers;

import Bussiness.Entities.Game;
import Bussiness.Entities.Stat;
import Bussiness.Entities.User;
import Persistance.DAO.GameDAO;
import Persistance.DAO.StatDAO;
import Persistance.DAO.UserDAO;

import java.util.List;

public class StatLogic {


	private StatDAO statDAO;
	private GameDAO gameDAO;
	private UserDAO userDAO;

	public StatLogic(StatDAO statDAO, GameDAO gameDAO, UserDAO userDAO) {

		this.statDAO = statDAO;
		this.gameDAO = gameDAO;
		this.userDAO = userDAO;
	}

	public List<Stat> getStatsByGame(int idGames) {

		return statDAO.readStatsByGame(idGames);
	}

	public List<User> getAllUsers() {

		return userDAO.readAllUsers();
	}

	public List<Game> getFinishedGames(String username) {

		return gameDAO.readFinishedGames(username);
	}
}