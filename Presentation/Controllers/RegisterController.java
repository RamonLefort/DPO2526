package Presentation.Controllers;

import Bussiness.Managers.UserLogic;
import Presentation.Views.RegisterWindow;
import java.awt.event.MouseEvent;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;

/**
 * Controlador encargado de gestionar el proceso de registro de nuevos usuarios en el sistema.
 * Implementa {@link ActionListener} para procesar la creación de cuentas y utiliza adaptadores
 * de ratón para facilitar la navegación de retorno hacia la pantalla de inicio de sesión.
 */
public class RegisterController implements ActionListener {

	private final RegisterWindow view;
	private final UserLogic userLogic;
	private final ViewController viewController;

	/**
	 * Constructor que inicializa el controlador con las dependencias necesarias y
	 * configura los escuchadores de eventos para los componentes de la vista de registro.
	 *
	 * @param view           Ventana que contiene el formulario de registro.
	 * @param userLogic      Lógica de negocio para la validación y creación de usuarios.
	 * @param viewController Gestor de navegación entre las diferentes vistas de la aplicación.
	 */
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
			default:
				System.err.println("Comando desconocido: " + e.getActionCommand());
		}
	}

	/**
	 * Gestiona el flujo de registro de un nuevo usuario.
	 *
	 * El método realiza las siguientes validaciones antes de proceder al registro:
	 * 1. Verifica el formato del correo electrónico según las reglas de negocio.
	 * 2. Comprueba la robustez de la contraseña.
	 * 3. Asegura la coincidencia entre la contraseña y su confirmación.
	 * 4. Valida la disponibilidad del nombre de usuario y del email en la base de datos.
	 * * Si todas las validaciones son exitosas, solicita a {@link UserLogic} la creación de la cuenta
	 * y redirige al usuario a la pantalla de Login. En caso contrario, muestra el error pertinente.
	 */
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

	/**
	 * Redirige el flujo de la aplicación hacia la vista de inicio de sesión.
	 */
	private void moveToLogin() {
		viewController.showView("LOGIN");
	}
}
