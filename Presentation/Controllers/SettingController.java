package Presentation.Controllers;

import Bussiness.Managers.SettingLogic;
import Bussiness.Managers.UserLogic;
import Presentation.Views.SettingView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SettingController {

	private SettingView settingView;
	private SettingLogic settingLogic;
	private UserLogic userLogic;
	private ViewController viewController;

	public SettingController(SettingView settingView, UserLogic userLogic, ViewController viewController) {
		this.settingView = settingView;
		this.userLogic = userLogic;
		this.viewController = viewController;

		this.settingView.getLogoutBtn().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleLogout();
			}
		});

		this.settingView.getDeleteAccountBtn().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleDeleteAccount();
			}
		});
	}

	public void handleSaveSettings() {

	}

	public void handleLogout() {
		userLogic.logout();
		viewController.showView("LOGIN");
	}

	public void handleDeleteAccount() {
		String username = userLogic.getCurrentUser().getUsername();
		userLogic.deleteAccount(username);
		viewController.showView("LOGIN");
	}

	public void loadSettings(String username) {

	}
}