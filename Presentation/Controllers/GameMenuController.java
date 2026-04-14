package Presentation.Controllers;

import Bussiness.Managers.GameLogic;
import Presentation.Views.GameMenuView;

public class GameMenuController {

	private GameMenuView gameMenuView;
	private GameLogic gameLogic;

	public GameMenuController(GameMenuView gameMenuView, GameLogic gameLogic) {
		// 2. Usamos "gameMenuView" en minúscula para que coincida con la declaración
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