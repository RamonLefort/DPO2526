package Presentation.Controllers;

import Bussiness.Managers.UserLogic;
import Presentation.Views.RegisterWindow;
import java.awt.event.MouseEvent;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;



public class RegisterController implements ActionListener {

	private final RegisterWindow view;
	private final UserLogic userLogic;
	private final ViewController viewController;

	public RegisterController(RegisterWindow view, UserLogic userLogic, ViewController viewController) {
		this.view = view;
		this.userLogic = userLogic;
		this.viewController = viewController;

		this.view.getRegisterButton().addActionListener(this);

		this.view.getFooterLabel().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				moveToLogin();
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
			case RegisterWindow.BTN_REGISTER:
				handleRegister();
				break;
			default:
				System.err.println("Unknown action command: " + e.getActionCommand());
		}
	}

	private void handleRegister() {
		String username = view.getUserField().getText().trim();
		String email    = view.getMailField().getText().trim();
		String password = new String(view.getPasswordField().getPassword()).trim();
		String confirm  = new String(view.getConfirmField().getPassword()).trim();

		if (!userLogic.validateEmail(email)) {
			view.showError("El email no tiene un formato válido (@gmail.com).");
			return;
		}

		if (!userLogic.validatePassword(password)) {
			view.showError("La contraseña debe tener letras, números y una mayúscula.");
			return;
		}

		if (!password.equals(confirm)) {
			view.showError("Las contraseñas no coinciden.");
			return;
		}

		if (userLogic.usernameExists(username)) {
			view.showError("El nombre de usuario ya está en uso.");
			return;
		}

		if (userLogic.emailExists(email)) {
			view.showError("El email ya está registrado.");
			return;
		}

		if (userLogic.register(username, email, password, confirm)) {
			viewController.showView("LOGIN");
		} else {
			view.showError("Error al registrar. Inténtalo de nuevo.");
		}
	}


	private void moveToLogin() {
		viewController.showView("LOGIN");
	}
}
