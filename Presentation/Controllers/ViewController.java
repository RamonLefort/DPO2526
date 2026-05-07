package Presentation.Controllers;

import Bussiness.Managers.GameLogic;
import Bussiness.Managers.StatLogic;
import Bussiness.Managers.UserLogic;
import Presentation.Views.*;
import Bussiness.Managers.GameplayLogic;

import javax.swing.*;
import java.awt.*;

/**
 * Controlador central de navegación y gestión de vistas de la aplicación.
 * Actúa como un orquestador que centraliza el intercambio de paneles en la ventana principal,
 * inicializa todos los controladores del sistema y gestiona el flujo de estados
 * mediante el uso de CardLayout.
 */
public class ViewController {

    private final JFrame frame;
    private final CardLayout cardLayout;
    private final JPanel rootPanel;
    private final UserLogic userLogic;
    private final GameLogic gameLogic;
    private final GameplayLogic gameplayLogic;
    private GameCreator gameCreator;
    private GameCreatorController gameCreatorController;
    private GameController gameController;
    private StatsController statsController;
    private final StatLogic statLogic;
    private GameMenuController gameMenuController;

    /**
     * Constructor que inicializa el marco principal (JFrame) y configura el sistema de navegación.
     * Carga el icono de la aplicación y prepara el panel raíz para albergar las distintas vistas.
     *
     * @param userLogic     Lógica de gestión de usuarios.
     * @param gameLogic     Lógica de gestión de partidas.
     * @param gameplayLogic Lógica de ejecución del juego en tiempo real.
     * @param statLogic     Lógica de gestión de estadísticas y telemetría.
     */
    public ViewController(UserLogic userLogic, GameLogic gameLogic, GameplayLogic gameplayLogic, StatLogic statLogic) {
        this.userLogic = userLogic;
        this.gameLogic = gameLogic;
        this.gameplayLogic = gameplayLogic;
        this.frame = new JFrame("Coffee Clicker");
        this.cardLayout = new CardLayout();
        this.rootPanel = new JPanel(cardLayout);
        this.statLogic = statLogic;

        Image icon = new ImageIcon("assets/icono.png").getImage();
        frame.setIconImage(icon);

        setupFrame();
        setupViews();
    }

    /**
     * Instancia todas las vistas de la aplicación y sus respectivos controladores.
     * Registra cada vista en el CardLayout asignándole un identificador textual único (String ID).
     */
    private void setupViews() {
        LoginWindow loginView = new LoginWindow();
        RegisterWindow registerView = new RegisterWindow();
        GameView gameView = new GameView();
        SettingView settingView = new SettingView();
        GameMenuView gameMenuView = new GameMenuView();
        StatsView statsView = new StatsView();
        gameCreator = new GameCreator();

        gameCreatorController = new GameCreatorController(gameCreator, gameLogic, userLogic, statLogic,this, "");
        gameController = new GameController(gameView, gameplayLogic, this, 0, "", gameLogic, statLogic);

        new LoginController(loginView, userLogic, this);
        new RegisterController(registerView, userLogic, this);
        new SettingController(settingView, userLogic, this);
        gameMenuController = new GameMenuController(gameMenuView, gameLogic, statLogic, userLogic, this);
        statsController = new StatsController(statsView, statLogic, this);


        rootPanel.add(statsView, "STATS");
        rootPanel.add(loginView, "LOGIN");
        rootPanel.add(registerView, "REGISTER");
        rootPanel.add(gameView, "GAME");
        rootPanel.add(settingView, "SETTINGS");
        rootPanel.add(gameMenuView, "GAME MENU");
        rootPanel.add(gameCreator, "GAME CREATOR");
    }

    /**
     * Cambia la visibilidad hacia una vista específica identificada por su nombre.
     * Si la vista es el menú de juego, activa la recarga automática de las partidas del usuario.
     *
     * @param viewName Nombre identificador de la vista a mostrar.
     */
    public void showView(String viewName) {
        if (viewName.equals("GAME MENU")) {
            gameMenuController.loadGames();
        }
        cardLayout.show(rootPanel, viewName);
    }

    /**
     * Prepara y muestra la interfaz activa de juego para una partida concreta.
     * Delega en el {@link GameController} la carga de los datos de la partida antes del cambio visual.
     *
     * @param idGame   Identificador de la partida a cargar.
     * @param username Nombre del usuario en sesión.
     */
    public void showGameView(int idGame, String username) {
        gameController.loadGame(idGame, username);
        cardLayout.show(rootPanel, "GAME");
    }

    /**
     * Configura las propiedades físicas de la ventana principal (JFrame), como el tamaño,
     * la posición centrada y la operación de cierre por defecto.
     */
    private void setupFrame() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1100, 700);
        frame.setLocationRelativeTo(null);
        frame.setContentPane(rootPanel);
    }

    /**
     * Cambia la interfaz hacia la vista de estadísticas de una partida.
     * Coordina la carga de datos históricos a través del {@link StatsController}.
     *
     * @param idGame Identificador de la partida a analizar.
     */
    public void showStats(int idGame) {
        cardLayout.show(rootPanel, "STATS");
        statsController.loadStatsData(idGame);
    }

    /**
     * Inicia la ejecución visual de la aplicación.
     * Hace visible la ventana y establece el punto de entrada inicial en la pantalla de Login.
     */
    public void start() {
        frame.setVisible(true);
        showView("LOGIN");
    }
}