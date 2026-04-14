package Persistance.DAO;

import Bussiness.Entities.Stat;
import Persistance.Configuration.MySQLDAO;
import java.util.List;

public class StatDAO {

	private MySQLDAO mySQLDAO;


	public StatDAO(MySQLDAO mySQLDAO) {
		this.mySQLDAO = mySQLDAO;
	}

	public void create(Stat stat) {

	}
	public List<Stat> readStatsByGame(int idGames) {

        return null;
    }
	public void delete(int idGames) {
	}
}