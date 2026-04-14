package Bussiness.Managers;

import Bussiness.Entities.Setting;
import Persistance.DAO.SettingDAO;

public class SettingLogic {
	private SettingDAO settingDAO;

	public SettingLogic(SettingDAO settingDAO) {
		this.settingDAO = settingDAO;
	}

	public Setting getSettings(String username) {
		return settingDAO.readByUsername(username);
	}

	public void updateSettings(Setting setting) {
		settingDAO.update(setting);
	}

}