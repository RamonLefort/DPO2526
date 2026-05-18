package Presentation.Controllers;

import Bussiness.Exceptions.DAOException;
import Bussiness.Managers.UserLogic;
import Presentation.Exceptions.CustomUIException;
import Presentation.Views.RegisterWindow;

import javax.swing.*;
import java.awt.event.MouseEvent;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.sql.SQLException;

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

	private void handleRegister() {
		try {
			String username = view.getUserField().getText().trim();
			String email    = view.getMailField().getText().trim();
			String password = new String(view.getPasswordField().getPassword()).trim();
			String confirm  = new String(view.getConfirmField().getPassword()).trim();

			if (!userLogic.validateEmail(email)) {
				throw new CustomUIException("El email no tiene un formato válido (@gmail.com).", "Formato Inválido", JOptionPane.WARNING_MESSAGE);
			}

			if (!userLogic.validatePassword(password)) {
				throw new CustomUIException("La contraseña debe tener letras, números y al menos una mayúscula.", "Contraseña Débil", JOptionPane.WARNING_MESSAGE);
			}

			if (!password.equals(confirm)) {
				throw new CustomUIException("Las contraseñas introducidas no coinciden.", "Error de Coincidencia", JOptionPane.WARNING_MESSAGE);
			}

			try {
				if (userLogic.usernameExists(username)) {
					throw new CustomUIException("El nombre de usuario ya está en uso por otra cuenta.", "Usuario Duplicado", JOptionPane.ERROR_MESSAGE);
				}

				if (userLogic.emailExists(email)) {
					throw new CustomUIException("El email ya está registrado en el sistema.", "Email Duplicado", JOptionPane.ERROR_MESSAGE);
				}

				if (!userLogic.register(username, email, password, confirm)) {
					throw new CustomUIException("No se ha podido procesar el alta del usuario. Verifica los campos.", "Error de Negocio", JOptionPane.ERROR_MESSAGE);
				}

				viewController.showView("LOGIN");

			} catch (DAOException daoEx) {
				throw new CustomUIException("No se ha podido establecer comunicación con el servidor de base de datos", "Error de Conexión", JOptionPane.ERROR_MESSAGE);
			}

		} catch (CustomUIException uiEx) {
			uiEx.showDialog(view);
		}
    }

	/**
	 * Redirige el flujo de la aplicación hacia la vista de inicio de sesión.
	 */
	private void moveToLogin() {
		viewController.showView("LOGIN");
	}
}
