package Presentation.Controllers;

import Bussiness.Managers.StatLogic;
import Presentation.Views.StatsView;

public class StatsController {


	private StatsView statsView;
	private StatLogic statLogic;


	public StatsController(StatsView statsView, StatLogic statLogic) {
		this.statsView = statsView;
		this.statLogic = statLogic;
	}

	public void handleSelectUser() {

	}

	public void handleSelectGame() {

	}

	public void refreshChart() {

	}
}