package Presentation.Controllers;
import Bussiness.Entities.User;
import Bussiness.Managers.UserLogic;
import Presentation.Views.LoginWindow;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class LoginController {
	private final LoginWindow view;
	private final UserLogic userLogic;
	private final ViewController viewController;

	public LoginController(LoginWindow view, UserLogic userLogic, ViewController viewController) {
		this.view = view;
		this.userLogic = userLogic;
		this.viewController = viewController;

		initListeners();
	}

	private void initListeners() {
		view.getFooterLabel().addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				viewController.showView("REGISTER");
			}
			@Override
			public void mousePressed(MouseEvent e) {}
			@Override
			public void mouseReleased(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
		});

		view.getLoginButton().addActionListener(e -> HandleLogin());
	}

	private void HandleLogin() {
		String usernameOrEmail = view.getUsernameField().getText().trim();
		String password        = new String(view.getPasswordField().getPassword());

		User user = userLogic.login(usernameOrEmail, password);

		if (user != null) {
			viewController.showView("GAME");
		} else {
			view.showError("Usuario o contraseña incorrectos.");
		}
	}
}