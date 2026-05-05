package Presentation.Controllers;

import Bussiness.Entities.Stat;
import Bussiness.Managers.StatLogic;
import Presentation.Views.StatsView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class StatsController implements ActionListener {

	private StatsView statsView;
	private StatLogic statLogic;
	private ViewController viewController;

	public StatsController(StatsView statsView, StatLogic statLogic, ViewController viewController) {
		this.statsView = statsView;
		this.statLogic = statLogic;
		this.viewController = viewController;
		this.statsView.setActionListener(this);
	}

	/**
	 * Carga y muestra las estadísticas de una partida específica.
	 * @param idGame El ID de la partida que acaba de terminar o que se ha seleccionado.
	 */
	public void loadStatsData(int idGame) {
		List<Stat> gameHistory = statLogic.getAllStats(idGame);
		if (gameHistory != null && !gameHistory.isEmpty()) {
			statsView.displayStats(gameHistory);
		} else {
			System.err.println("No se encontraron estadísticas para la partida: " + idGame);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
			case StatsView.BTN_EXIT_STATS:
				handleExit();
				break;
		}
	}

	private void handleExit() {
		viewController.showView("GAME MENU");
	}
}