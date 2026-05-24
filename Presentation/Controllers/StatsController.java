package Presentation.Controllers;

import Bussiness.Entities.Stat;
import Bussiness.Exceptions.BusinessException;
import Bussiness.Managers.StatLogic;
import Presentation.Views.PresentationException;
import Presentation.Views.StatsView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador de la capa de presentación que gestiona la visualización y filtrado
 * de las estadísticas de juego.
 */
public class StatsController implements ActionListener {

	private final StatsView statsView;
	private final StatLogic statLogic;
	private final ViewController viewController;
	private final GameMenuController gameMenuController;

	private List<Integer> currentFilteredGameIds;

	/**
	 * Construye el controlador y establece las dependencias necesarias.
	 *
	 * @param statsView          La vista de estadísticas a gestionar.
	 * @param statLogic          Servicio de lógica de negocio para obtener datos estadísticos.
	 * @param viewController     Gestor de navegación de la aplicación.
	 * @param gameMenuController Controlador del menú principal.
	 */
	public StatsController(StatsView statsView, StatLogic statLogic, ViewController viewController, GameMenuController gameMenuController) {
		this.statsView = statsView;
		this.statLogic = statLogic;
		this.viewController = viewController;
		this.gameMenuController = gameMenuController;
		this.currentFilteredGameIds = new ArrayList<>();
		this.statsView.setActionListener(this);
	}

	/**
	 * Inicializa la vista con los datos correspondientes a una partida específica.
	 *
	 * @param idGame El identificador de la partida que se debe seleccionar inicialmente.
	 */
	public void loadStatsData(int idGame) {
		try {
			List<String> usernames = statLogic.getAllUsernames();
			statsView.populateUsers(usernames);

			String ownerUsername = statLogic.getGameOwner(idGame);
			statsView.setSelectedUser(ownerUsername);

			updateGameComboBox(ownerUsername);

			int indexInList = currentFilteredGameIds.indexOf(idGame);
			if (indexInList != -1) {
				statsView.setSelectedGameIndex(indexInList);
				refreshStatsVisuals(idGame);
			}

		} catch (BusinessException e) {
			showDatabaseError();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
			case StatsView.BTN_EXIT_STATS:
				handleExit();
				break;
			case StatsView.BTN_SETTINGS:
				handleSettings();
				break;
			case StatsView.COMBO_USER_CHANGED:
				handleUserFilterChanged();
				break;
			case StatsView.COMBO_GAME_CHANGED:
				handleGameFilterChanged();
				break;
		}
	}

	/**
	 * Cuando cambia el usuario del JComboBox, recalculamos sus partidas y refrescamos.
	 */
	private void handleUserFilterChanged() {
		String selectedUser = statsView.getSelectedUser();
		if (selectedUser != null) {
			try {
				updateGameComboBox(selectedUser);
				if (!currentFilteredGameIds.isEmpty()) {
					refreshStatsVisuals(currentFilteredGameIds.get(0));
				} else {
					statsView.displayStats(new ArrayList<>());
				}
			} catch (BusinessException e) {
				showDatabaseError();
			}
		}
	}

	/**
	 * Cuando cambia la partida elegida, simplemente recargamos el historial en los componentes Swing.
	 */
	private void handleGameFilterChanged() {
		String selectedUser = statsView.getSelectedUser();
		String selectedGame = statsView.getSelectedGame();

		if (selectedUser != null && selectedGame != null) {
			try {
				int idGame = statLogic.getGameIdByNameAndUser(selectedGame, selectedUser);
				refreshStatsVisuals(idGame);
			} catch (BusinessException e) {
				showDatabaseError();
			}
		}
	}

	/**
	 * Actualiza el combo secundario de partidas basándose en el nombre de usuario.
	 */
	private void updateGameComboBox(String username) throws BusinessException {
		List<String> gameNames = statLogic.getFinishedGameNamesByUser(username);
		statsView.populateGames(gameNames);

		this.currentFilteredGameIds = statLogic.getFinishedGameIdsByUser(username);
	}

	/**
	 * Pide a las entidades de negocio los indicadores y los inyecta en el JTable y el Canvas.
	 */
	private void refreshStatsVisuals(int idGame) {
		try {
			List<Stat> gameHistory = statLogic.getAllStats(idGame);
			statsView.displayStats(gameHistory);
		} catch (BusinessException e) {
			showDatabaseError();
		}
	}

	private void handleExit() {
		gameMenuController.loadGames();
		viewController.showView("GAME MENU");
	}

	private void handleSettings() {
		viewController.showView("SETTINGS");
	}

	private void showDatabaseError() {
		PresentationException presentationException = new PresentationException();
		presentationException.showErrorDialog("No se ha podido establecer comunicación con el servidor de base de datos", "Error de Conexión");
	}
}