package Presentation.Controllers;

import Bussiness.Managers.GameLogic;
import Presentation.Views.GameMenuView;

public class GameMenuController {

	private GameMenuView gameMenuView;
	private GameLogic gameLogic;

	public GameMenuController(GameMenuView gameMenuView, GameLogic gameLogic) {
		this.gameMenuView = gameMenuView;
		this.gameLogic = gameLogic;
	}
	public void handleNewGame() {

	}

	public void handleResumeGame() {

	}

	public void handleDeleteGame() {

	}

	public void handleCloneGame() {

	}
}