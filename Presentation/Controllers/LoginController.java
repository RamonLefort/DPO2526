package Presentation.Controllers;

import Bussiness.Entities.User;
import Bussiness.Exceptions.BusinessException;
import Bussiness.Managers.UserLogic;
import Presentation.Views.LoginWindow;
import Presentation.Views.PresentationException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Controlador encargado de gestionar el proceso de inicio de sesión (Login).
 * Implementa {@link ActionListener} para procesar el intento de acceso y utiliza
 * adaptadores de ratón para gestionar la navegación hacia el registro de nuevos usuarios.
 */
public class LoginController implements ActionListener {
	private final LoginWindow view;
	private final UserLogic userLogic;
	private final ViewController viewController;
	private final GameMenuController gameMenuController;

	/**
	 * Constructor que inicializa el controlador con las dependencias necesarias y
	 * configura los escuchadores de eventos de la vista.
	 *
	 * @param view           Ventana de inicio de sesión.
	 * @param userLogic      Lógica de negocio para la validación de credenciales.
	 * @param viewController Gestor de navegación entre ventanas.
	 * @param gameMenuController Controlador de la página de menú de los juegos
	 */
	public LoginController(LoginWindow view, UserLogic userLogic, ViewController viewController, GameMenuController gameMenuController) {
		this.view = view;
		this.userLogic = userLogic;
		this.viewController = viewController;
		this.gameMenuController = gameMenuController;

		this.view.setActionListener(this);

		this.view.getFooterLabel().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				viewController.showView("REGISTER");
			}
		});
	}

	/**
	 * Gestiona los eventos de acción disparados por la interfaz de usuario.
	 *
	 * @param e Evento de acción capturado.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
			case LoginWindow.BTN_LOGIN:
				handleLogin();
                break;
			default:
				System.err.println("Comando desconocido: " + e.getActionCommand());
		}
	}

	/**
	 * Procesa el intento de inicio de sesión del usuario.
	 * Extrae las credenciales de la vista, solicita la verificación a la capa de lógica
	 * y, en caso de éxito, redirige al usuario al menú principal del juego.
	 * Si la autenticación falla, muestra un mensaje de error en la vista.
	 */
	private void handleLogin() {
		String username = view.getUsernameField().getText().trim();
		String password = new String(view.getPasswordField().getPassword());
        User user = null;
        try {
            user = userLogic.login(username, password);
        } catch (BusinessException e) {
			PresentationException presentationException = new PresentationException();
			presentationException.showErrorDialog("No se ha podido establecer comunicación con el servidor de base de datos", "Error de Conexión");
        }

        if (user != null) {
			gameMenuController.loadGames();
			viewController.showView("GAME MENU");
		}else{
			view.showError("Usuario o contraseña incorrecto");
		}
	}
}
