package Presentation.Controllers;

import Bussiness.Entities.Game;
import Bussiness.Entities.Generator;
import Bussiness.Managers.*;
import Presentation.Views.GameView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Timer;

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
	private Timer gameTimer;

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
			case GameView.BTN_MACHINE -> handleMachine();
			case GameView.BTN_PLANTATION -> handlePlantation();
		}
	}

	public void loadGame(int idGame, String username) {
		this.idGame = idGame;
		this.username = username;
		this.coffeeCount = 1;
		this.currentGame = gameLogic.loadGame(username, idGame);
		generators = gameLogic.getGenerators(idGame);
		if(generators.isEmpty()) {
			generators = gameLogic.createGenerators(idGame);
		}
		gameView.updateCoffeeCount((int) currentGame.getMoney());
		gameView.updateGameName(currentGame.getNameGame());
		gameView.updateProductionXSec(currentGame.getProduction_per_sec());
		List<Generator> gens = gameLogic.getGenerators(currentGame.getIdGame());
		for(Generator gen : gens) {
			if(gen.getName().equals("Barista")){
				gameView.updateBaristaPrice(gen.getPrice());
			}else if(gen.getName().equals("Espresso Machine")){
				gameView.updateMachinePrice(gen.getPrice());
			}else if(gen.getName().equals("Coffee Plantation")){
				gameView.updatePlantationPrice(gen.getPrice());
			}
		}
		gameplayLogic.startAutoGenerators(idGame, currentGame, this.generators, gameView);
		gameView.updateGenerationsData(generators);
		startTimer();
	}

	public void startTimer() {
		if (gameTimer != null && gameTimer.isRunning()) return;

		gameTimer = new Timer(1000, e -> {
			int seconds = currentGame.getSeconds() + 1;

			if (seconds >= 60) {
				currentGame.setSeconds(0);
				currentGame.setMinutes(currentGame.getMinutes() + 1);
				System.out.println("Minutes: " +currentGame.getMinutes());
			} else {
				currentGame.setSeconds(seconds);
			}

			if (currentGame.getMinutes() >= 60) {
				currentGame.setMinutes(0);
				currentGame.setHours(currentGame.getHours() + 1);
				System.out.println("Hours: " +currentGame.getHours());
			}

			if (currentGame.getSeconds() % 30 == 0) {
				saveCurrentProgress();
			}
		});
		gameTimer.start();
	}

	private void saveCurrentProgress() {
		gameLogic.saveGame(username, idGame, currentGame.getMoney(), currentGame.getHours(), currentGame.getMinutes(), currentGame.getSeconds(), currentGame.getCoffeePerClick(), currentGame.getProduction_per_sec());
	}

	private void handleBack() {
		saveCurrentProgress();
		gameplayLogic.stopAutoGenerators();
		gameTimer.stop();
		viewController.showView("GAME MENU");
	}

	private void handleFinishGame() {
		saveCurrentProgress();
		statLogic.saveStat(idGame, currentGame.getMoney(), currentGame.getMinutes(), currentGame.getNameGame());
		gameplayLogic.stopAutoGenerators();
		gameTimer.stop();
		viewController.showView("GAME MENU");
	}

	private void handleClickGenerate() {
		currentGame.addMoney(coffeeCount);
		gameView.updateCoffeeCount((int) currentGame.getMoney());
	}

	public void handleBarista() {
		Generator barista = null;
		for (Generator g : generators) {
			if (g.getName() != null && g.getName().equalsIgnoreCase("Barista")) {
				barista = g;
				break;
			}
		}

		if (barista != null) {

			if (currentGame.getMoney() >= barista.getPrice()) {
				synchronized (currentGame){
					currentGame.setMoney(currentGame.getMoney() - barista.getPrice());
					barista.setQuantity(barista.getQuantity() + 1);
				}
				barista.setPrice((int) (barista.getPrice() + (0.5 * barista.getPrice())));
				gameView.updateBaristaPrice(barista.getPrice());
				currentGame.setProduction_per_sec((float) (currentGame.getProduction_per_sec() + 0.2));
				gameView.updateProductionXSec(currentGame.getProduction_per_sec());
				// Persistencia
				gameLogic.updateGenerators(idGame, barista);
				gameLogic.saveGame(username, idGame, currentGame.getMoney(), currentGame.getHours(), currentGame.getMinutes(), currentGame.getSeconds(), currentGame.getCoffeePerClick(), currentGame.getProduction_per_sec());

				gameView.updateCoffeeCount((int) currentGame.getMoney());
				gameView.updateGenerationsData(generators);
				System.out.println("Barista lanzado y produciendo");
			} else {
				System.out.println("No hay suficiente dinero para el Barista");
			}
		}
	}

	public void handleMachine() {
		Generator machine = null;
		for (Generator g : generators) {
			if (g.getName() != null && g.getName().equalsIgnoreCase("Espresso Machine")) {
				machine = g;
				break;
			}
		}

		if (machine != null) {

			if (currentGame.getMoney() >= machine.getPrice()) {
				synchronized (currentGame){
					currentGame.setMoney(currentGame.getMoney() - machine.getPrice());
					machine.setQuantity(machine.getQuantity() + 1);
				}
				machine.setPrice((int) (machine.getPrice() + (0.5 * machine.getPrice())));
				gameView.updateMachinePrice(machine.getPrice());
				currentGame.setProduction_per_sec((float) (currentGame.getProduction_per_sec() + 0.66));
				gameView.updateProductionXSec(currentGame.getProduction_per_sec());
				// Persistencia
				gameLogic.updateGenerators(idGame, machine);
				gameLogic.saveGame(username, idGame, currentGame.getMoney(), currentGame.getHours(), currentGame.getMinutes(), currentGame.getSeconds(), currentGame.getCoffeePerClick(), currentGame.getProduction_per_sec());

				gameView.updateCoffeeCount((int) currentGame.getMoney());
				gameView.updateGenerationsData(generators);
				System.out.println("Espresso Machine lanzado y produciendo");
			} else {
				System.out.println("No hay suficiente dinero para el Espresso Machine");
			}
		}
	}

	public void handlePlantation() {
		Generator plantation = null;
		for (Generator g : generators) {
			if (g.getName() != null && g.getName().equalsIgnoreCase("Coffee Plantation")) {
				plantation = g;
				break;
			}
		}

		if (plantation != null) {
			if (currentGame.getMoney() >= plantation.getPrice()) {
				synchronized (currentGame){
					currentGame.setMoney(currentGame.getMoney() - plantation.getPrice());
					plantation.setQuantity(plantation.getQuantity() + 1);
				}
				plantation.setPrice((int) (plantation.getPrice() + (0.5 * plantation.getPrice())));
				gameView.updatePlantationPrice(plantation.getPrice());
				// Persistencia
				currentGame.setProduction_per_sec(currentGame.getProduction_per_sec() + 1);
				gameView.updateProductionXSec(currentGame.getProduction_per_sec());
				gameLogic.updateGenerators(idGame, plantation);
				gameLogic.saveGame(username, idGame, currentGame.getMoney(), currentGame.getHours(), currentGame.getMinutes(), currentGame.getSeconds(), currentGame.getCoffeePerClick(), currentGame.getProduction_per_sec());

				gameView.updateCoffeeCount((int) currentGame.getMoney());
				gameView.updateGenerationsData(generators);
				System.out.println("Plantación lanzado y produciendo");
			} else {
				System.out.println("No hay suficiente dinero para el Plantación");
			}
		}
	}

	public void handleBuyUpgrade() {

	}
}