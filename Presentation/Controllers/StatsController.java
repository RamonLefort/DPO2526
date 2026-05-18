package Presentation.Controllers;

import Bussiness.Entities.Stat;
import Bussiness.Exceptions.DAOException;
import Bussiness.Managers.StatLogic;
import Presentation.Exceptions.CustomUIException;
import Presentation.Views.StatsView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Controlador encargado de gestionar la visualización de estadísticas de las partidas.
 * Implementa {@link ActionListener} para responder a las interacciones del usuario en la vista
 * de estadísticas, coordinando la recuperación de datos históricos y la transición de regreso
 * al menú principal.
 */
public class StatsController implements ActionListener {

	private StatsView statsView;
	private StatLogic statLogic;
	private ViewController viewController;

	/**
	 * Constructor que inicializa el controlador con las dependencias necesarias de vista y lógica.
	 * Configura este controlador como el escuchador de eventos para la vista de estadísticas.
	 *
	 * @param statsView      La vista encargada de representar gráficamente las estadísticas.
	 * @param statLogic      La lógica de negocio para la recuperación de datos de telemetría.
	 * @param viewController El gestor de navegación entre las diferentes ventanas de la aplicación.
	 */
	public StatsController(StatsView statsView, StatLogic statLogic, ViewController viewController) {
		this.statsView = statsView;
		this.statLogic = statLogic;
		this.viewController = viewController;
		this.statsView.setActionListener(this);
	}

	/**
	 * Carga y muestra las estadísticas de una partida específica.
	 *
	 * @param idGame El ID de la partida que acaba de terminar o que se ha seleccionado.
	 */
	public void loadStatsData(int idGame) {
        List<Stat> gameHistory = null;
        try {
            gameHistory = statLogic.getAllStats(idGame);
        } catch (DAOException e) {
			CustomUIException uiEx = new CustomUIException("No se ha podido establecer comunicación con el servidor de base de datos", "Error de Conexión", JOptionPane.ERROR_MESSAGE);
			uiEx.showDialog(null);
        }
        if (gameHistory != null && !gameHistory.isEmpty()) {
			statsView.displayStats(gameHistory);
		} else {
			System.err.println("No se encontraron estadísticas para la partida: " + idGame);
		}
	}

	/**
	 * Gestiona las acciones disparadas por los componentes interactivos de la vista de estadísticas.
	 * @param e El evento de acción capturado desde la interfaz.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
			case StatsView.BTN_EXIT_STATS:
				handleExit();
				break;
		}
	}

	/**
	 * Finaliza la visualización de estadísticas y devuelve al usuario al menú principal del juego.
	 */
	private void handleExit() {
		viewController.showView("GAME MENU");
	}
}