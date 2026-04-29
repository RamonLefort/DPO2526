package Presentation.Controllers;

import Bussiness.Entities.Game;
import Bussiness.Entities.Generator;
import Bussiness.Managers.*;
import Persistance.DAO.GeneratorDAO;
import Presentation.Views.GameView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
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
	private List<Generator> generators = new ArrayList<>();

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
			case GameView.BTN_BARISTA -> handleBarista();
		}
	}

	public void loadGame(int idGame, String username) {
		this.idGame = idGame;
		this.username = username;
		this.coffeeCount = 0;
		this.currentGame = gameLogic.loadGame(username, idGame);
		gameView.updateCoffeeCount((int) currentGame.getMoney());
		generators = gameLogic.getGenerators(idGame);
		if(generators.isEmpty()) {
			generators = gameLogic.createGenerators(idGame);
		}
		gameView.updateCoffeeCount((int) currentGame.getMoney());
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

	public void handleBarista() {
		// 1. Buscamos el generador en la lista local (asumiendo que 'generators' es un atributo de clase)
		Generator barista = null;
		for (Generator g : generators) {
			if (g.getName() != null && g.getName().equalsIgnoreCase("Barista")) {
				barista = g;
				break;
			}
		}

		if (barista != null) {
			int currentPrice = (int) (barista.getPrice() * Math.pow(1.15, barista.getQuantity()));

			if (currentGame.getMoney() >= currentPrice) {
				synchronized (currentGame) {
					currentGame.setMoney(currentGame.getMoney() - currentPrice);
					barista.setQuantity(barista.getQuantity() + 1);
				}

				// Persistencia
				gameLogic.updateGenerators(idGame, barista);
				gameLogic.saveGame(username, idGame, currentGame.getMoney(), currentGame.getMinutes(), currentGame.getSeconds());

				gameView.updateCoffeeCount((int) currentGame.getMoney());
				System.out.println("Barista lanzado y produciendo");
			} else {
				System.out.println("No hay suficiente dinero para el Barista");
			}
		}
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