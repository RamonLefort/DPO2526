package Presentation.Controllers;

import Bussiness.Managers.SettingLogic;
import Bussiness.Managers.UserLogic;
import Presentation.Views.SettingView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SettingController implements ActionListener {

	private final SettingView settingView;
	private final UserLogic userLogic;
	private final ViewController viewController;

	public SettingController(SettingView settingView, UserLogic userLogic, ViewController viewController) {
		this.settingView = settingView;
		this.userLogic = userLogic;
		this.viewController = viewController;

		this.settingView.getLogoutBtn().addActionListener(this);
		this.settingView.getDeleteAccountBtn().addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
			case SettingView.BTN_LOGOUT:
				handleLogout();
				break;
			case SettingView.BTN_DELETE_ACCOUNT:
				handleDeleteAccount();
				break;
			default:
				System.err.println("Unknown action command: " + e.getActionCommand());
		}
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
