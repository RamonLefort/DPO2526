package Bussiness.Managers;

import Bussiness.Entities.Game;
import Persistance.DAO.GameDAO;
import Persistance.DAO.GeneratorDAO;
import Persistance.DAO.StatDAO;
import java.util.List;

public class GameLogic {


	private GameDAO gameDAO;
	private GeneratorDAO generatorDAO;
	private StatDAO statDAO;


	public GameLogic(GameDAO gameDAO, GeneratorDAO generatorDAO, StatDAO statDAO) {
		this.gameDAO = gameDAO;
		this.generatorDAO = generatorDAO;
		this.statDAO = statDAO;
	}

	public int createGame(String nameGame, String username) {
		return gameDAO.createGame(nameGame, username);
	}

	public void saveGame(int idGame) {
	}

	public void deleteGame(int idGame) {
		gameDAO.deleteGame(idGame);
	}

	public void finishGame(int idGame) {
	}

	public int loadGame(int idGame) {
		return idGame;
	}

	public List<Game> getUserGames(String username) {
		return gameDAO.getGamesByUser(username);
	}

	public boolean gameNameExists(String nameGame) {
		return gameDAO.existsByName(nameGame);
	}
}