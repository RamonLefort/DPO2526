package Presentation.Controllers;

import Bussiness.Exceptions.BusinessException;
import Bussiness.Managers.UserLogic;
import Presentation.Views.PresentationException;
import Presentation.Views.RegisterWindow;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Controlador encargado de gestionar el proceso de registro de nuevos usuarios en el sistema.
 * Implementa {@link ActionListener} para procesar la creación de cuentas y utiliza adaptadores
 * de ratón para facilitar la navegación de retorno hacia la pantalla de inicio de sesión.
 */
public class RegisterController implements ActionListener {

	private final RegisterWindow view;
	private final UserLogic userLogic;
	private final ViewController viewController;
	private final GameMenuController gameMenuController;

	/**
	 * Inicializa el controlador de registro estableciendo sus dependencias y vinculando
	 * los escuchadores de eventos necesarios para la interacción con la vista.
	 *
	 * @param view               La interfaz de registro que este controlador gestiona.
	 * @param userLogic          Instancia de la capa de lógica de negocio para operaciones de usuario.
	 * @param viewController     Gestor de navegación entre vistas de la aplicación.
	 * @param gameMenuController Controlador del menú principal para transiciones post-registro.
	 */
	public RegisterController(RegisterWindow view, UserLogic userLogic, ViewController viewController, GameMenuController gameMenuController) {
		this.view = view;
		this.userLogic = userLogic;
		this.viewController = viewController;
		this.gameMenuController = gameMenuController;

		this.view.setActionListener(this);

		this.view.getFooterLabel().addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent e) {
				viewController.showView("LOGIN");
			}
		});
	}

	/**
	 * Captura y distribuye las acciones realizadas en la interfaz de usuario.
	 *
	 * @param e El evento de acción capturado desde la vista.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
			case RegisterWindow.BTN_REGISTER:
				handleRegister();
				break;
			case "GO_LOGIN":
				moveToLogin();
				break;
		}
	}

	/**
	 * Procesa la solicitud de registro de un nuevo usuario.
	 * Valida los campos del formulario y, si todo es correcto, registra al usuario
	 * y redirige al menú de juego.
	 */
	private void handleRegister() {
		String username = view.getUserField().getText().trim();
		String email    = view.getMailField().getText().trim();
		String password = new String(view.getPasswordField().getPassword()).trim();
		String confirm  = new String(view.getConfirmField().getPassword()).trim();

		if (!userLogic.validateEmail(email)) {
			PresentationException presentationException = new PresentationException();
			presentationException.showErrorDialog("El email no tiene un formato válido (@gmail.com).", "Formato Inválido");
			return;
		}

		if (!userLogic.validatePassword(password)) {
			PresentationException presentationException = new PresentationException();
			presentationException.showErrorDialog("La contraseña debe tener 8 caracteres con, al menos, una letra, un número y una mayúscula.", "Contraseña Débil");
			return;
		}

		if (!password.equals(confirm)) {
			PresentationException presentationException = new PresentationException();
			presentationException.showErrorDialog("Las contraseñas introducidas no coinciden.", "Error de Coincidencia");
			return;
		}

		try {
			if (userLogic.usernameExists(username)) {
				PresentationException presentationException = new PresentationException();
				presentationException.showErrorDialog("El nombre de usuario ya está en uso por otra cuenta.", "Usuario Duplicado");
				return;
			}

			if (userLogic.emailExists(email)) {
				PresentationException presentationException = new PresentationException();
				presentationException.showErrorDialog("El email ya está registrado en el sistema.", "Email Duplicado");
				return;
			}

			if (!userLogic.register(username, email, password, confirm)) {
				PresentationException presentationException = new PresentationException();
				presentationException.showErrorDialog("No se ha podido procesar el alta del usuario. Verifica los campos.", "Error de Negocio");
				return;
			}

			try {
				userLogic.login(username, password);
				gameMenuController.loadGames();
				viewController.showView("GAME MENU");
			} catch (BusinessException ex) {
				PresentationException presentationException = new PresentationException();
				presentationException.showErrorDialog("No se ha podido establecer comunicación con el servidor de base de datos", "Error de Conexión");
			}

		} catch (BusinessException daoEx) {
			PresentationException presentationException = new PresentationException();
			presentationException.showErrorDialog("No se ha podido establecer comunicación con el servidor de base de datos", "Error de Conexión");
		}
	}

	/**
	 * Redirige el flujo de la aplicación hacia la vista de inicio de sesión.
	 */
	private void moveToLogin() {
		viewController.showView("LOGIN");
	}
}

