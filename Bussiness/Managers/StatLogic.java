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

	public List<Stat> getAllStats(int idGame) {
		return statDAO.readByGame(idGame);
	}

	public List<Game> getFinishedGames(String username){return gameDAO.readFinishedGames(username);}

	public void saveStat(int idGame, int minute, double money, int clicks, double autogen, float maxprod, double expenses) {
		statDAO.saveMinuteStat(idGame, minute, money, clicks, autogen, maxprod, expenses);
	}

	public void createStat(int idGame){
		statDAO.create(idGame, 0, 0, 0, 0, 0, 0);
	}

	public Stat getLastMinuteStat(int idGame){
		return statDAO.getLastMinuteStat(idGame);
	}

	public List<User> getAllUsers() {
		return userDAO.readAllUsers();
	}
}