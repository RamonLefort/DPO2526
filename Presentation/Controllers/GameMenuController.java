package Presentation.Controllers;

import Bussiness.Entities.Game;
import Bussiness.Entities.Generator;
import Bussiness.Exceptions.BusinessException;
import Bussiness.Managers.GameLogic;
import Bussiness.Managers.UserLogic;
import Presentation.Views.GameMenuView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import Bussiness.Managers.StatLogic;
import Presentation.Views.PresentationException;

/**
 * Controlador encargado de gestionar el menú principal de selección de partidas.
 * Implementa {@link ActionListener} para procesar la navegación hacia nuevas partidas,
 * la reanudación de partidas existentes, la visualización de estadísticas y el cierre de sesión.
 */
public class GameMenuController implements ActionListener {

	private final GameMenuView gameMenuView;
	private final GameLogic gameLogic;
	private final UserLogic userLogic;
	private final ViewController viewController;
	private GameController gameController;
	private StatsController statsController;
	private final StatLogic statLogic;

	/**
	 * Constructor que inicializa el controlador con las dependencias de lógica y vista necesarias.
	 *
	 * @param gameMenuView   Vista que muestra el listado de partidas disponibles.
	 * @param gameLogic      Lógica de gestión de datos de partida.
	 * @param statLogic      Lógica de gestión de telemetría y partidas finalizadas.
	 * @param userLogic      Lógica de gestión de usuarios para identificar la sesión activa.
	 * @param viewController Gestor de navegación entre ventanas.
	 */
	public GameMenuController(GameMenuView gameMenuView, GameLogic gameLogic, StatLogic statLogic, UserLogic userLogic, ViewController viewController) {
		this.gameMenuView = gameMenuView;
		this.gameLogic = gameLogic;
		this.statLogic = statLogic;
		this.userLogic = userLogic;
		this.viewController = viewController;
		this.gameMenuView.setActionListener(this);
	}

	/**
	 * Establece el controlador de juego asociado a esta instancia.
	 *
	 * @param gameController El controlador de lógica de juego a inyectar.
	 */
	public void setGameController(GameController gameController) {
		this.gameController = gameController;
	}

	/**
	 * Establece el controlador de estadísticas asociado a esta instancia.
	 *
	 * @param statsController El controlador encargado de gestionar y reportar
	 *                        las estadísticas del usuario a inyectar.
	 */
	public void setStatsController(StatsController statsController) {
		this.statsController = statsController;
	}
	/**
	 * Procesa los eventos de acción disparados desde el menú de partidas.
	 * Gestiona comandos estáticos (atrás, logout, nueva partida) y comandos dinámicos
	 * asociados a las tarjetas de partidas específicas (continuar, ver estadísticas).
	 *
	 * @param e El evento de acción capturado.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
			case GameMenuView.BTN_BACK     -> handleBack();
			case GameMenuView.BTN_LOGOUT   -> handleLogout();
			case GameMenuView.BTN_NEW_GAME -> handleNewGame();
			default -> {
				if (e.getActionCommand().startsWith(GameMenuView.BTN_CONTINUE)) {
					int idGame = Integer.parseInt(e.getActionCommand().replace(GameMenuView.BTN_CONTINUE, ""));
					handleResumeGame(idGame);
				} else if (e.getActionCommand().startsWith(GameMenuView.BTN_STATS)) {
					int idGame = Integer.parseInt(e.getActionCommand().replace(GameMenuView.BTN_STATS, ""));
					handleStats(idGame);
				}
			}
		}
	}

	/**
	 * Transiciona la aplicación hacia la vista activa de una partida guardada.
	 *
	 * @param idGame Identificador único de la partida a reanudar.
	 */
	public void handleResumeGame(int idGame) {
		gameController.loadGame(idGame, userLogic.getCurrentUser().getUsername());
		viewController.showView("GAME");
	}

	/**
	 * Redirige a la vista de análisis estadístico de una partida específica.
	 *
	 * @param idGame ID de la partida a analizar.
	 */
	private void handleStats(int idGame) {
		statsController.loadStatsData(idGame);
		viewController.showView("STATS");
	}

	/**
	 * Regresa al usuario a la pantalla de configuración del sistema.
	 */
	private void handleBack() {
		viewController.showView("SETTINGS");
	}

	/**
	 * Cierra la sesión del usuario actual y regresa a la pantalla de inicio de sesión.
	 */
	private void handleLogout() {
		userLogic.logout();
		viewController.showView("LOGIN");
	}

	/**
	 * Redirige a la pantalla de creación de una nueva partida.
	 */
	public void handleNewGame() {
		viewController.showView("GAME CREATOR");
	}

	/**
	 * Realiza la carga de datos para poblar el menú.
	 * Filtra la lista total de partidas del usuario para separar aquellas que están
	 * en curso de las que ya han sido finalizadas.
	 */
	public void loadGames() {
		String username = userLogic.getCurrentUser().getUsername();
        List<Game> allGames = null;
		List<Game> finishedGames = null;
        try {
            allGames = gameLogic.getUserGames(username);
            finishedGames = statLogic.getFinishedGames(username);
        } catch (BusinessException e) {
			PresentationException presentationException = new PresentationException();
			presentationException.showErrorDialog("No se ha podido establecer comunicación con el servidor de base de datos", "Error de Conexión");
        }

        List<Game> currentGames = new ArrayList<>();
		for (Game game : allGames) {
			boolean isFinished = false;
			for (Game g : finishedGames) {
				if (g.getIdGame() == game.getIdGame()) {
					isFinished = true;
				}
			}
			if (!isFinished) {
				currentGames.add(game);
			}
		}

		loadCurrentGames(currentGames);
		loadFinishedGames(finishedGames);
	}

	/**
	 * Llena la sección de partidas en curso de la vista.
	 * Por cada partida, recupera la cantidad de generadores poseídos para mostrarlos
	 * en la tarjeta de resumen.
	 *
	 * @param games Lista de partidas activas del usuario.
	 */
	public void loadCurrentGames(List<Game> games) {
		gameMenuView.clearCurrentGames();
		for (Game game : games) {
            List<Generator> gens = null;
            try {
                gens = gameLogic.getGenerators(game.getIdGame());
            } catch (BusinessException e) {
				PresentationException presentationException = new PresentationException();
				presentationException.showErrorDialog("No se ha podido establecer comunicación con el servidor de base de datos", "Error de Conexión");
            }
            int baristas = 0, machines = 0, plantations = 0;
			for(Generator gen : gens) {
				if(gen.getName().equals("Barista")){
					baristas = gen.getQuantity();
				}else if(gen.getName().equals("Espresso Machine")){
					machines = gen.getQuantity();
				}else if(gen.getName().equals("Coffee Plantation")){
					plantations = gen.getQuantity();
				}
			}
			gameMenuView.addCurrentGameCard(
					game.getNameGame(),
					String.valueOf((int) game.getMoney()),
					game.getMinutes(),
					game.getIdGame(),
					baristas,
					machines,
					plantations
			);
		}
		gameMenuView.refreshCurrentGames();
	}

	/**
	 * Llena la sección de partidas finalizadas de la vista.
	 * @param games Lista de partidas completadas del usuario.
	 */
	public void loadFinishedGames(List<Game> games) {
		gameMenuView.clearFinishedGames();
		for (Game game : games) {
            List<Generator> gens = null;
            try {
                gens = gameLogic.getGenerators(game.getIdGame());
            } catch (BusinessException e) {
				PresentationException presentationException = new PresentationException();
				presentationException.showErrorDialog("No se ha podido establecer comunicación con el servidor de base de datos", "Error de Conexión");
            }
            int baristas = 0, machines = 0, plantations = 0;
			for(Generator gen : gens) {
				if(gen.getName().equals("Barista")){
					baristas = gen.getQuantity();
				}else if(gen.getName().equals("Espresso Machine")){
					machines = gen.getQuantity();
				}else if(gen.getName().equals("Coffee Plantation")){
					plantations = gen.getQuantity();
				}
			}
			gameMenuView.addFinishedGameCard(
					game.getNameGame(),
					String.valueOf((int) game.getMoney()),
					game.getMinutes(),
					game.getIdGame(),
					baristas,
					machines,
					plantations
			);
		}
		gameMenuView.refreshFinishedGames();
	}
}