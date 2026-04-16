package Presentation.Controllers;

import Bussiness.Entities.User;
import Bussiness.Managers.UserLogic;
import Presentation.Views.LoginWindow;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class LoginController implements ActionListener {
	private final LoginWindow view;
	private final UserLogic userLogic;
	private final ViewController viewController;

	public LoginController(LoginWindow view, UserLogic userLogic, ViewController viewController) {
		this.view = view;
		this.userLogic = userLogic;
		this.viewController = viewController;


		this.view.setActionListener(e -> handleLogin());


		this.view.getFooterLabel().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				viewController.showView("REGISTER");
			}
		});
	}


	@Override
	public void actionPerformed(ActionEvent e) {

		switch (e.getActionCommand()) {
			case LoginWindow.BTN_LOGIN:
				handleLogin();
				break;
			default:
				System.err.println("Unknown action command: " + e.getActionCommand());
		}
	}

	private void handleLogin() {
		String username = view.getUsernameField().getText().trim();
		String password = new String(view.getPasswordField().getPassword());

		User user = userLogic.login(username, password);

		if (user != null) {
			viewController.showView("SETTINGS");
		} else {
			view.showError("Usuario o contraseña incorrectos.");
		}
	}
}