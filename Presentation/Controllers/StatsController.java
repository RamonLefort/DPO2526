package Presentation.Controllers;

import Bussiness.Entities.Stat;
import Bussiness.Exceptions.DAOException;
import Bussiness.Managers.StatLogic;
import Presentation.Exceptions.CustomUIException;
import Presentation.Views.StatsView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class StatsController implements ActionListener {

	private final StatsView statsView;
	private final StatLogic statLogic;
	private final ViewController viewController;
	private final GameMenuController gameMenuController;

	// Lista auxiliar en memoria RAM para mapear el índice del ComboBox con los ID físicos reales de BD
	private List<Integer> currentFilteredGameIds;

	public StatsController(StatsView statsView, StatLogic statLogic, ViewController viewController, GameMenuController gameMenuController) {
		this.statsView = statsView;
		this.statLogic = statLogic;
		this.viewController = viewController;
		this.gameMenuController = gameMenuController;
		this.currentFilteredGameIds = new ArrayList<>();
		this.statsView.setActionListener(this);
	}

	/**
	 * Carga inicial del panel de estadísticas. Carga la jerarquía completa de controles.
	 */
	public void loadStatsData(int idGame) {
		try {
			List<String> usernames = statLogic.getAllUsernames();
			statsView.populateUsers(usernames);

			String ownerUsername = statLogic.getGameOwner(idGame);
			statsView.setSelectedUser(ownerUsername);

			updateGameComboBox(ownerUsername);

			// 4. Seleccionar visualmente la partida y pintar las estadísticas
			int indexInList = currentFilteredGameIds.indexOf(idGame);
			if (indexInList != -1) {
				statsView.setSelectedGameIndex(indexInList);
				refreshStatsVisuals(idGame);
			}

		} catch (DAOException e) {
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
			} catch (DAOException e) {
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
			} catch (DAOException e) {
				showDatabaseError();
			}
		}
	}

	/**
	 * Actualiza el combo secundario de partidas basándose en el nombre de usuario.
	 */
	private void updateGameComboBox(String username) throws DAOException {
		// Obtenemos los nombres legibles de las partidas para la interfaz
		List<String> gameNames = statLogic.getFinishedGameNamesByUser(username);
		statsView.populateGames(gameNames);

		// Almacenamos sincrónicamente sus IDs correspondientes en RAM para búsquedas rápidas locales
		this.currentFilteredGameIds = statLogic.getFinishedGameIdsByUser(username);
	}

	/**
	 * Pide a las entidades de negocio los indicadores y los inyecta en el JTable y el Canvas.
	 */
	private void refreshStatsVisuals(int idGame) {
		try {
			List<Stat> gameHistory = statLogic.getAllStats(idGame);
			statsView.displayStats(gameHistory);
		} catch (DAOException e) {
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
		CustomUIException uiEx = new CustomUIException("No se ha podido establecer comunicación con el servidor para filtrar estadísticas.", "Error de Conexión", JOptionPane.ERROR_MESSAGE);
		uiEx.showDialog(null);
	}
}