package Presentation.Controllers;

import Bussiness.Entities.Game;
import Bussiness.Managers.*;
import Presentation.Views.GameView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;

public class GameController implements ActionListener {

	private Game currentGame;
	private final GameView gameView;
	private final GameplayLogic gameplayLogic;
	private final ViewController viewController;
	private final GameLogic gameLogic;
	private int idGame;
	private int coffeeCount = 0;
	private String username;
	private UserLogic userLogic;
	private final StatLogic statLogic;

	public GameController(GameView gameView, GameplayLogic gameplayLogic, ViewController viewController, int idGame, String username, GameLogic gameLogic, UserLogic userLogic, StatLogic statLogic) {
		this.gameView = gameView;
		this.gameplayLogic = gameplayLogic;
		this.viewController = viewController;
		this.idGame = idGame;
		this.username = username;
		this.gameLogic = gameLogic;
		this.userLogic = userLogic;
		this.statLogic = statLogic;
		this.gameView.setActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
			case GameView.BTN_BACK   -> handleBack();
			case GameView.BTN_FINISH -> handleFinishGame();
			case GameView.BTN_COFFEE -> handleClickGenerate();
		}
	}

	public void loadGame(int idGame, String username) {
		this.idGame = idGame;
		this.username = username;
		this.coffeeCount = 0;
		this.currentGame = gameLogic.loadGame(username, idGame);
		gameView.updateCoffeeCount((int) currentGame.getMoney());  // <-- dinero real
	}

	private void handleBack() {
		double totalMoney = currentGame.getMoney() + coffeeCount;
		gameLogic.saveGame(username, idGame, totalMoney, currentGame.getMinutes(), currentGame.getSeconds());
		viewController.showView("GAME MENU");
	}

	private void handleFinishGame() {
		double totalMoney = currentGame.getMoney() + coffeeCount;
		gameLogic.saveGame(username, idGame, totalMoney, currentGame.getMinutes(), currentGame.getSeconds());
		statLogic.saveStat(idGame, totalMoney, currentGame.getMinutes(), currentGame.getNameGame());
		viewController.showView("GAME MENU");
	}

	private void handleClickGenerate() {
		coffeeCount++;
		gameView.updateCoffeeCount((int) currentGame.getMoney() + coffeeCount);

	}


	public void handleBuyGenerator() {

	}

	public void handleBuyUpgrade() {

	}

	private void startAutoTimer() {

	}

	private void stopAutoTimer() {

	}
}