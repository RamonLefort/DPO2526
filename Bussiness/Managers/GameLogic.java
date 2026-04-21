package Bussiness.Managers;

import Bussiness.Entities.Game;
import Bussiness.Entities.Stat;
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
		List<Game> userGames = gameDAO.getGamesByUser(username);
		for (int i = 0; i < userGames.size(); i++) {
			if (userGames.get(i).getNameGame().equalsIgnoreCase(nameGame)) {
				return -2;
			}
		}
		return gameDAO.createGame(nameGame, username);
	}

	public void saveGame(String username, int idGame, double money, int minutes, int seconds) {
		List<Game> games = gameDAO.getGamesByUser(username);
		for (int i = 0; i < games.size(); i++) {
			Game game = games.get(i);
			if (game.getIdGame() == idGame) {
				gameDAO.updateGame(idGame, money, minutes, seconds, game.getCoffeePerClick());
				return;
			}
		}
		throw new IllegalArgumentException("Game not found with id: " + idGame);
	}

	public void deleteGame(int idGame) {
		gameDAO.deleteGame(idGame);
	}

    public Game loadGame(String username, int idGame) {
        List<Game> games = gameDAO.getGamesByUser(username);
        for (int i = 0; i < games.size(); i++) {
            if (games.get(i).getIdGame() == idGame) {
                return games.get(i);
            }
        }
        throw new IllegalArgumentException("Game not found with id: " + idGame);
    }

	public void saveStat(int idGame, double money, int minutes, String nameGame) {
		statDAO.create(new Stat(money, minutes, idGame, nameGame));
	}

	public List<Game> getUserGames(String username) {
		return gameDAO.getGamesByUser(username);
	}

	public boolean gameNameExists(String nameGame) {
		return gameDAO.existsByName(nameGame);
	}
}