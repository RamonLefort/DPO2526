package Presentation.Controllers;

import Bussiness.Managers.UserLogic;
import Presentation.Views.RegisterWindow;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class RegisterController {

	private final RegisterWindow view;
	private final UserLogic userLogic;
	private final ViewController viewController;

	public RegisterController(RegisterWindow view, UserLogic userLogic, ViewController viewController) {
		this.view = view;
		this.userLogic = userLogic;
		this.viewController = viewController;
		initListeners();
	}

	private void initListeners() {
		view.getRegisterButton().addActionListener(e -> HandleRegister());

		view.getFooterLabel().addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				MovetoLogin();
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
	}

	private void HandleRegister() {
		String username = view.getUserField().getText().trim();
		String email    = view.getMailField().getText().trim();
		String password = new String(view.getPasswordField().getPassword()).trim();
		String confirm  = new String(view.getConfirmField().getPassword()).trim();

		if (userLogic.validateEmail(email) == false) {
			view.showError("El email no tiene un formato válido (@gmail.com).");
			return;
		}

		if (userLogic.validatePassword(password) == false) {
			view.showError("La contraseña debe tener letras, números y una mayúscula.");
			return;
		}

		if (password.equals(confirm) == false) {
			view.showError("Las contraseñas no coinciden.");
			return;
		}

		if (userLogic.usernameExists(username) == true) {
			view.showError("El nombre de usuario ya está en uso.");
			return;
		}

		if (userLogic.emailExists(email) == true) {
			view.showError("El email ya está registrado.");
			return;
		}

		boolean success = userLogic.register(username, email, password, confirm);

		if (success == true) {
			viewController.showView("LOGIN");
		} else {
			view.showError("Error al registrar. Inténtalo de nuevo.");
		}
	}

	private void MovetoLogin() {
		viewController.showView("LOGIN");
	}
}