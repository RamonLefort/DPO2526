package Presentation.Controllers;

import Bussiness.Managers.GameLogic;
import Bussiness.Managers.StatLogic;
import Presentation.Views.GameCreator;
import Bussiness.Managers.UserLogic;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Bussiness.Entities.User;

/**
 * Controlador encargado de gestionar la vista de creación de nuevas partidas.
 * Implementa {@link ActionListener} para procesar las interacciones del usuario en el formulario
 * de creación, validando los datos de entrada y orquestando la inicialización completa
 * de una nueva sesión de juego en la base de datos.
 */
public class GameCreatorController implements ActionListener {

    private final GameCreator view;
    private final GameLogic gameLogic;
    private final UserLogic userLogic;
    private final StatLogic statLogic;
    private final ViewController viewController;
    private String username;

    /**
     * Constructor que inicializa el controlador con las dependencias necesarias para
     * crear una partida y gestionar la navegación.
     *
     * @param view           Vista que contiene el formulario de creación.
     * @param gameLogic      Lógica encargada de la creación de la partida y sus entidades.
     * @param userLogic      Lógica de gestión de usuarios para identificar al creador.
     * @param statLogic      Lógica para inicializar el registro de estadísticas.
     * @param viewController Gestor de navegación entre ventanas.
     * @param username       Nombre del usuario que está realizando la operación.
     */
    public GameCreatorController(GameCreator view, GameLogic gameLogic, UserLogic userLogic, StatLogic statLogic, ViewController viewController, String username) {
        this.view = view;
        this.gameLogic = gameLogic;
        this.userLogic = userLogic;
        this.statLogic = statLogic;
        this.viewController = viewController;
        this.username = username;
        this.view.setActionListener(this);
    }

    /**
     * Actualiza el nombre del usuario en sesión.
     *
     * @param username Nuevo nombre de usuario.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gestiona los eventos de acción producidos en la vista de creación.
     * Dirige el flujo hacia el retorno al menú, el cierre de sesión o el proceso
     * de creación de la partida.
     *
     * @param e El evento de acción capturado.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case GameCreator.BTN_BACK:
                handleBack();
                break;
            case GameCreator.BTN_LOGOUT:
                handleLogout();
                break;
            case GameCreator.BTN_CREATE:
                handleCreateGame();
                break;
            default:
                System.err.println("Comando desconocido: " + e.getActionCommand());
        }
    }

    /**
     * Cancela la operación actual y regresa al usuario al menú principal de partidas.
     */
    private void handleBack() {
        viewController.showView("GAME MENU");
    }

    /**
     * Finaliza la sesión del usuario actual y redirige a la ventana de inicio de sesión.
     */
    private void handleLogout() {
        userLogic.logout();
        viewController.showView("LOGIN");
    }

    /**
     * Procesa la solicitud de creación de una nueva partida.
     *
     * Realiza las siguientes validaciones y acciones en orden:
     * 1. Verifica que el nombre de la partida no esté vacío.
     * 2. Solicita a {@link GameLogic} la creación de la partida en la persistencia.
     * 3. Si el ID devuelto es válido (distinto de 0 o -1), procede a la inicialización en cascada:
     * - Crea los generadores base para la partida.
     * - Genera las mejoras iniciales asociadas a dichos generadores.
     * - Inicializa el registro de estadísticas (telemetría) para la partida.
     * 4. Redirige al usuario a la vista activa del juego.
     */
    private void handleCreateGame() {
        String gameName = view.getGameName();

        if (gameName.isEmpty()) {
            view.showError("Por favor, introduce un nombre para la partida.");
            return;
        }
        User currentUser = userLogic.getCurrentUser();
        String currentUsername = currentUser.getUsername();

        int idGame = gameLogic.createGame(gameName, currentUsername);
        if (idGame == 0) {
            view.showError("No se pudo crear la partida. Inténtalo de nuevo.");
            return;
        }
        if (idGame == -1) {
            view.showError("Ya tienes una partida con ese nombre.");
            return;
        }
        if (idGame != 0) {
            gameLogic.createGenerators(idGame);
            gameLogic.createUpgrades(idGame, gameLogic.getGenerators(idGame));
            statLogic.createStat(idGame);
            viewController.showGameView(idGame, currentUsername);
        }
    }
}
