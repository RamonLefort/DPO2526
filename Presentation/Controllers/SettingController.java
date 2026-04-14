package Presentation.Controllers;

import Bussiness.Managers.SettingLogic;
import Bussiness.Managers.UserLogic;
import Presentation.Views.SettingView;

public class SettingController {


	private SettingView settingView;
	private SettingLogic settingLogic;
	private UserLogic userLogic;

	public SettingController(SettingView settingView, SettingLogic settingLogic, UserLogic userLogic) {
		this.settingView = settingView;
		this.settingLogic = settingLogic;
		this.userLogic = userLogic;
	}

	public void handleSaveSettings() {

	}

	public void handleLogout() {

	}

	public void handleDeleteAccount() {

	}

	public void loadSettings(String username) {

	}
}