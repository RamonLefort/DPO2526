package Persistance.DAO;

import Bussiness.Entities.Setting;
import Persistance.Configuration.MySQLDAO;

public class SettingDAO {

	private MySQLDAO mySQLDAO;


	public SettingDAO(MySQLDAO mySQLDAO) {
		this.mySQLDAO = mySQLDAO;
	}

	public void create(Setting setting) {
	}
	public Setting readByUsername(String username) {
	return null;
	}
	public void update(Setting setting) {

	}
	public void delete(String username) {
	}
}