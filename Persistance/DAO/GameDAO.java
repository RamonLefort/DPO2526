package Persistance.DAO;

import Bussiness.Entities.Game;
import Persistance.Configuration.MySQLDAO;
import java.util.List;

public class GameDAO {

	private MySQLDAO mySQLDAO;

	public GameDAO(MySQLDAO mySQLDAO) {
		this.mySQLDAO = mySQLDAO;
	}

	public int createGame(String nameGame, String username) {
		return 0;
	}
	public List<Game> getGamesByUser(String username) {
		return null;
	}
	public void deleteGame(int idGame) {}
	public boolean existsByName(String nameGame) {

        return false;
    }
	public void updateGame(int idGame, float money, int minutes, int seconds) {}
	public List<Game> readFinishedGames(String username) {

        return null;
    }

}