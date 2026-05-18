package Presentation.Controllers;

import Bussiness.Entities.Game;
import Bussiness.Entities.Generator;
import Bussiness.Entities.Stat;
import Bussiness.Entities.Upgrade;
import Bussiness.Exceptions.DAOException;
import Bussiness.Managers.*;
import Presentation.Exceptions.CustomUIException;
import Presentation.Views.GameView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

/**
 * Controlador principal del flujo de juego.
 * Implementa {@link ActionListener} para gestionar todas las acciones del usuario en la vista de juego,
 * desde la generación de recursos por clic hasta la compra de infraestructuras y mejoras.
 * Además, gestiona el hilo de tiempo del juego para el registro de estadísticas y autoguardado.
 */
public class GameController implements ActionListener {

	private Game currentGame;
	private final GameView gameView;
	private final GameplayLogic gameplayLogic;
	private final ViewController viewController;
	private final GameLogic gameLogic;
	private int idGame, clicks = 0, clicks_per_min = 0;
	private int coffeeCount = 0;
	private String username;
	private final StatLogic statLogic;
	private List<Generator> generators = new ArrayList<>();
	private Timer gameTimer;
	private float maxprod = 0;
	private double expenses = 0, autogen;
	private List<Upgrade> upgrades;

	/**
	 * Constructor que inicializa el controlador con todas las dependencias de lógica y vista.
	 *
	 * @param gameView Vista interactiva del juego.
	 * @param gameplayLogic Lógica de ejecución en tiempo real (hilos).
	 * @param viewController Gestor de navegación entre vistas.
	 * @param idGame ID de la partida actual.
	 * @param username Nombre del usuario en sesión.
	 * @param gameLogic Lógica de gestión de datos de partida.
	 * @param statLogic Lógica de gestión de telemetría y estadísticas.
	 */
	public GameController(GameView gameView, GameplayLogic gameplayLogic, ViewController viewController, int idGame, String username, GameLogic gameLogic, StatLogic statLogic) {
		this.gameView = gameView;
		this.gameplayLogic = gameplayLogic;
		this.viewController = viewController;
		this.idGame = idGame;
		this.username = username;
		this.gameLogic = gameLogic;
		this.statLogic = statLogic;
		this.gameView.setActionListener(this);
	}

	/**
	 * Punto de entrada para los eventos generados en la interfaz gráfica.
	 * Distribuye la lógica según el comando de acción recibido desde los botones de la vista.
	 *
	 * @param e El evento de acción capturado.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
			case GameView.BTN_BACK   -> handleBack();
			case GameView.BTN_FINISH -> handleFinishGame();
			case GameView.BTN_COFFEE -> handleClickGenerate();
			case GameView.BTN_BARISTA -> handleBarista();
			case GameView.BTN_MACHINE -> handleMachine();
			case GameView.BTN_PLANTATION -> handlePlantation();
			case GameView.BTN_UP_BARISTA -> handleUpgradeBarista();
			case GameView.BTN_UP_MACHINE -> handleUpgradeMachine();
			case GameView.BTN_UP_PLANTATION -> handleUpgradePlantation();
			case GameView.BTN_GENERATORS -> handleGenerators();
			case GameView.BTN_UPGRADES -> handleUpgrades();
		}
	}

	/**
	 * Configura el estado inicial de una partida cargando datos de persistencia.
	 * Sincroniza los precios de los generadores, el estado de las mejoras y
	 * restaura las métricas del último minuto registrado.
	 *
	 * @param idGame Identificador de la partida a cargar.
	 * @param username Usuario propietario.
	 */
	public void loadGame(int idGame, String username) {
		this.idGame = idGame;
		this.username = username;
		this.coffeeCount = 1;
        try {
            this.currentGame = gameLogic.loadGame(username, idGame);
        } catch (DAOException e) {
			CustomUIException uiEx = new CustomUIException("No se ha podido establecer comunicación con el servidor de base de datos", "Error de Conexión", JOptionPane.ERROR_MESSAGE);
			uiEx.showDialog(null);
        }
        try {
            generators = gameLogic.getGenerators(idGame);
        } catch (DAOException e) {
			CustomUIException uiEx = new CustomUIException("No se ha podido establecer comunicación con el servidor de base de datos", "Error de Conexión", JOptionPane.ERROR_MESSAGE);
			uiEx.showDialog(null);
        }
        try {
            upgrades = gameLogic.getUpgrades(idGame);
        } catch (DAOException e) {
			CustomUIException uiEx = new CustomUIException("No se ha podido establecer comunicación con el servidor de base de datos", "Error de Conexión", JOptionPane.ERROR_MESSAGE);
			uiEx.showDialog(null);
        }
        gameView.updateCoffeeCount((int) currentGame.getMoney());
		gameView.updateGameName(currentGame.getNameGame());
		gameView.updateProductionXSec(currentGame.getProduction_per_sec());
		for(Generator gen : generators) {
			if(gen.getName().equals("Barista")){
				gameView.updateBaristaPrice(gen.getPrice());
			}else if(gen.getName().equals("Espresso Machine")){
				gameView.updateMachinePrice(gen.getPrice());
			}else if(gen.getName().equals("Coffee Plantation")){
				gameView.updatePlantationPrice(gen.getPrice());
			}
			for(Upgrade upgrade: upgrades){
				if(upgrade.getIdGenerator() == gen.getIdGenerator() && upgrade.isActive()){
					gen.setEarning(gen.getEarning() * 2);
					if(gen.getName().equals("Barista")){
						gameView.updateUpgradeBaristaText();
					}else if(gen.getName().equals("Espresso Machine")){
						gameView.updateUpgradeMachineText();
					}else if(gen.getName().equals("Coffee Plantation")){
						gameView.updateUpgradePlantationText();
					}
				}
			}
		}
		gameView.updateProductionXSec(currentGame.getProduction_per_sec());
        Stat stat = null;
        try {
            stat = statLogic.getLastMinuteStat(idGame);
        } catch (DAOException e) {
			CustomUIException uiEx = new CustomUIException("No se ha podido establecer comunicación con el servidor de base de datos", "Error de Conexión", JOptionPane.ERROR_MESSAGE);
			uiEx.showDialog(null);
        }
        clicks = stat.getManualClicksTotal();
		maxprod = stat.getMaxProductionRate();
		expenses = stat.getUpgradesExpenses();
		gameplayLogic.startAutoGenerators(idGame, currentGame, this.generators, gameView, this);
		gameView.updateGenerationsData(generators);
		startTimer();
	}

	/**
	 * Inicia el temporizador de juego.
	 * Gestiona el paso de segundos, minutos y horas. Cada 60 segundos realiza
	 * un volcado de estadísticas a la base de datos y cada 30 segundos ejecuta un autoguardado.
	 */
	public void startTimer() {
		if (gameTimer != null && gameTimer.isRunning()) return;

		gameTimer = new Timer(1000, e -> {
			int seconds = currentGame.getSeconds() + 1;

			if (seconds >= 60) {
				currentGame.setSeconds(0);
				currentGame.setMinutes(currentGame.getMinutes() + 1);
				float temp_maxprod = (float) (clicks_per_min + autogen);
				maxprod = Math.max(maxprod, temp_maxprod);
                try {
                    statLogic.saveStat(idGame, currentGame.getMinutes(), currentGame.getMoney(), clicks, autogen, maxprod, expenses);
                } catch (DAOException ex) {
					CustomUIException uiEx = new CustomUIException("No se ha podido establecer comunicación con el servidor de base de datos", "Error de Conexión", JOptionPane.ERROR_MESSAGE);
					uiEx.showDialog(null);
                }
                autogen = clicks_per_min = 0;
			} else {
				currentGame.setSeconds(seconds);
			}

			if (currentGame.getMinutes() >= 60) {
				currentGame.setMinutes(0);
				currentGame.setHours(currentGame.getHours() + 1);
			}

			if (currentGame.getSeconds() % 30 == 0) {
				saveCurrentProgress();
			}
		});
		gameTimer.start();
	}

	/**
	 * Guarda el estado actual de la partida (dinero, tiempo y producción) de forma persistente.
	 */
	private void saveCurrentProgress() {
        try {
            gameLogic.saveGame(username, idGame, currentGame.getMoney(), currentGame.getHours(), currentGame.getMinutes(), currentGame.getSeconds(), currentGame.getCoffeePerClick(), currentGame.getProduction_per_sec());
        } catch (DAOException e) {
			CustomUIException uiEx = new CustomUIException("No se ha podido establecer comunicación con el servidor de base de datos", "Error de Conexión", JOptionPane.ERROR_MESSAGE);
			uiEx.showDialog(null);
        }
    }

	/**
	 * Cambia la vista del juego a la página del menú
	 */
	private void handleBack() {
		saveCurrentProgress();
		gameplayLogic.stopAutoGenerators();
		gameTimer.stop();
		viewController.showView("GAME MENU");
	}

	/**
	 * Cambia la vista del juego a la página del menú y finaliza el juego
	 */
	private void handleFinishGame() {
		saveCurrentProgress();
        try {
            gameLogic.finishGame(idGame);
        } catch (DAOException e) {
			CustomUIException uiEx = new CustomUIException("No se ha podido establecer comunicación con el servidor de base de datos", "Error de Conexión", JOptionPane.ERROR_MESSAGE);
			uiEx.showDialog(null);
        }
        try {
            statLogic.saveStat(idGame, currentGame.getMinutes(), currentGame.getMoney(), clicks, currentGame.getProduction_per_sec(), maxprod, expenses);
        } catch (DAOException e) {
			CustomUIException uiEx = new CustomUIException("No se ha podido establecer comunicación con el servidor de base de datos", "Error de Conexión", JOptionPane.ERROR_MESSAGE);
			uiEx.showDialog(null);
        }
        gameplayLogic.stopAutoGenerators();
		gameTimer.stop();
		viewController.showView("GAME MENU");
	}

	/**
	 * Gestiona la acción de clic manual sobre el recurso principal.
	 * Incrementa los contadores de clics para estadísticas y el saldo de dinero.
	 */
	private void handleClickGenerate() {
		this.clicks++;
		this.clicks_per_min++;
		currentGame.addMoney(coffeeCount);
		gameView.updateCoffeeCount((int) currentGame.getMoney());
	}

	/**
	 * Procesa la compra de un Barista. Valída fondos, actualiza la producción por segundo
	 * y recalcula el precio inflado para la siguiente unidad.
	 */
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
				expenses += barista.getPrice();
				currentGame.setProduction_per_sec((currentGame.getProduction_per_sec() + ((float) barista.getEarning() / (barista.getPeriod() / 1000))));
				gameView.updateProductionXSec(currentGame.getProduction_per_sec());
				// Persistencia
                try {
                    gameLogic.updateGenerators(idGame, barista);
                } catch (DAOException e) {
					CustomUIException uiEx = new CustomUIException("No se ha podido establecer comunicación con el servidor de base de datos", "Error de Conexión", JOptionPane.ERROR_MESSAGE);
					uiEx.showDialog(null);
                }
                try {
                    gameLogic.saveGame(username, idGame, currentGame.getMoney(), currentGame.getHours(), currentGame.getMinutes(), currentGame.getSeconds(), currentGame.getCoffeePerClick(), currentGame.getProduction_per_sec());
                } catch (DAOException e) {
					CustomUIException uiEx = new CustomUIException("No se ha podido establecer comunicación con el servidor de base de datos", "Error de Conexión", JOptionPane.ERROR_MESSAGE);
					uiEx.showDialog(null);
                }
                gameView.updateCoffeeCount((int) currentGame.getMoney());
				gameView.updateGenerationsData(generators);
			} else {
				CustomUIException uiException = new CustomUIException("No tienes suficiente dinero para comprar el generador", "Falta de Fondos", JOptionPane.ERROR_MESSAGE);
				uiException.showDialog(null);
			}
		}
	}

	/**
	 * Procesa la compra de una Maquina. Valída fondos, actualiza la producción por segundo
	 * y recalcula el precio inflado para la siguiente unidad.
	 */
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
				expenses += machine.getPrice();
				currentGame.setProduction_per_sec((float) (currentGame.getProduction_per_sec() + ((float) machine.getEarning() / (machine.getPeriod() / 1000))));
				gameView.updateProductionXSec(currentGame.getProduction_per_sec());
				// Persistencia
                try {
                    gameLogic.updateGenerators(idGame, machine);
                } catch (DAOException e) {
					CustomUIException uiEx = new CustomUIException("No se ha podido establecer comunicación con el servidor de base de datos", "Error de Conexión", JOptionPane.ERROR_MESSAGE);
					uiEx.showDialog(null);
                }
                try {
                    gameLogic.saveGame(username, idGame, currentGame.getMoney(), currentGame.getHours(), currentGame.getMinutes(), currentGame.getSeconds(), currentGame.getCoffeePerClick(), currentGame.getProduction_per_sec());
                } catch (DAOException e) {
					CustomUIException uiEx = new CustomUIException("No se ha podido establecer comunicación con el servidor de base de datos", "Error de Conexión", JOptionPane.ERROR_MESSAGE);
					uiEx.showDialog(null);
                }

                gameView.updateCoffeeCount((int) currentGame.getMoney());
				gameView.updateGenerationsData(generators);
			} else {
				CustomUIException uiException = new CustomUIException("No tienes suficiente dinero para comprar el generador", "Falta de Fondos", JOptionPane.ERROR_MESSAGE);
				uiException.showDialog(null);
			}
		}
	}

	/**
	 * Procesa la compra de un Plantación. Valída fondos, actualiza la producción por segundo
	 * y recalcula el precio inflado para la siguiente unidad.
	 */
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
				expenses += plantation.getPrice();
				// Persistencia
				currentGame.setProduction_per_sec(currentGame.getProduction_per_sec() + ((float) plantation.getEarning() / (plantation.getPeriod() / 1000)));
				gameView.updateProductionXSec(currentGame.getProduction_per_sec());
                try {
                    gameLogic.updateGenerators(idGame, plantation);
                } catch (DAOException e) {
					CustomUIException uiEx = new CustomUIException("No se ha podido establecer comunicación con el servidor de base de datos", "Error de Conexión", JOptionPane.ERROR_MESSAGE);
					uiEx.showDialog(null);
                }
                try {
                    gameLogic.saveGame(username, idGame, currentGame.getMoney(), currentGame.getHours(), currentGame.getMinutes(), currentGame.getSeconds(), currentGame.getCoffeePerClick(), currentGame.getProduction_per_sec());
                } catch (DAOException e) {
					CustomUIException uiEx = new CustomUIException("No se ha podido establecer comunicación con el servidor de base de datos", "Error de Conexión", JOptionPane.ERROR_MESSAGE);
					uiEx.showDialog(null);
                }

                gameView.updateCoffeeCount((int) currentGame.getMoney());
				gameView.updateGenerationsData(generators);
			} else {
				CustomUIException uiException = new CustomUIException("No tienes suficiente dinero para comprar el generador", "Falta de Fondos", JOptionPane.ERROR_MESSAGE);
				uiException.showDialog(null);
			}
		}
	}

	/**
	 * Procesa la compra de la mejora para el Barista.
	 * Duplica la eficiencia del generador y actualiza la tasa de producción global.
	 */
	public void handleUpgradeBarista() {
		Generator barista = null;
		for (Generator g : generators) {
			if (g.getName() != null && g.getName().equalsIgnoreCase("Barista")) {
				barista = g;
				break;
			}
		}

		Upgrade upgrade = null;
		for (Upgrade g : upgrades) {
			if (g.getIdGenerator() == barista.getIdGenerator()) {
				upgrade = g;
				break;
			}
		}

		if (barista != null && currentGame.getMoney() >= upgrade.getPrice() && !upgrade.isActive()) {
			synchronized (currentGame) {
				currentGame.setMoney(currentGame.getMoney() - upgrade.getPrice());
				barista.setEarning(barista.getEarning() * 2);
			}

			float extraProd = (float) (barista.getQuantity() * 0.2);
			currentGame.setProduction_per_sec(currentGame.getProduction_per_sec() + extraProd);
			expenses += upgrade.getPrice();

            try {
                gameLogic.updateGenerators(idGame, barista);
            } catch (DAOException e) {
				CustomUIException uiEx = new CustomUIException("No se ha podido establecer comunicación con el servidor de base de datos", "Error de Conexión", JOptionPane.ERROR_MESSAGE);
				uiEx.showDialog(null);
            }
            try {
                gameLogic.updateUpgrades(idGame, barista.getIdGenerator());
            } catch (DAOException e) {
				CustomUIException uiEx = new CustomUIException("No se ha podido establecer comunicación con el servidor de base de datos", "Error de Conexión", JOptionPane.ERROR_MESSAGE);
				uiEx.showDialog(null);
            }
            saveCurrentProgress();

			gameView.updateCoffeeCount((int) currentGame.getMoney());
			gameView.updateProductionXSec(currentGame.getProduction_per_sec());
			gameView.updateUpgradeBaristaText();
		}else{
			CustomUIException uiException = new CustomUIException("No tienes suficiente dinero para comprar la mejora", "Falta de Fondos", JOptionPane.ERROR_MESSAGE);
			uiException.showDialog(null);
		}
	}

	/**
	 * Procesa la compra de la mejora para la Maquina.
	 * Duplica la eficiencia del generador y actualiza la tasa de producción global.
	 */
	public void handleUpgradeMachine() {
		Generator machine = null;
		for (Generator g : generators) {
			if (g.getName() != null && g.getName().equalsIgnoreCase("Espresso Machine")) {
				machine = g;
				break;
			}
		}

		Upgrade upgrade = null;
		for (Upgrade g : upgrades) {
			if (g.getIdGenerator() == machine.getIdGenerator()) {
				upgrade = g;
				break;
			}
		}

		if (machine != null && currentGame.getMoney() >= upgrade.getPrice() && !upgrade.isActive()) {
			synchronized (currentGame) {
				currentGame.setMoney(currentGame.getMoney() - upgrade.getPrice());
				machine.setEarning(machine.getEarning() * 2);
			}

			float extraProd = (float) (machine.getQuantity() * 0.66);
			currentGame.setProduction_per_sec(currentGame.getProduction_per_sec() + extraProd);
			expenses += upgrade.getPrice();

            try {
                gameLogic.updateGenerators(idGame, machine);
            } catch (DAOException e) {
				CustomUIException uiEx = new CustomUIException("No se ha podido establecer comunicación con el servidor de base de datos", "Error de Conexión", JOptionPane.ERROR_MESSAGE);
				uiEx.showDialog(null);
            }
            try {
                gameLogic.updateUpgrades(idGame, machine.getIdGenerator());
            } catch (DAOException e) {
				CustomUIException uiEx = new CustomUIException("No se ha podido establecer comunicación con el servidor de base de datos", "Error de Conexión", JOptionPane.ERROR_MESSAGE);
				uiEx.showDialog(null);
            }
            saveCurrentProgress();

			gameView.updateCoffeeCount((int) currentGame.getMoney());
			gameView.updateProductionXSec(currentGame.getProduction_per_sec());
			gameView.updateUpgradeMachineText();
		}else{
			CustomUIException uiException = new CustomUIException("No tienes suficiente dinero para comprar la mejora", "Falta de Fondos", JOptionPane.ERROR_MESSAGE);
			uiException.showDialog(null);
		}
	}

	/**
	 * Procesa la compra de la mejora para la Plantación.
	 * Duplica la eficiencia del generador y actualiza la tasa de producción global.
	 */
	public void handleUpgradePlantation() {
		Generator plantation = null;
		for (Generator g : generators) {
			if (g.getName() != null && g.getName().equalsIgnoreCase("Coffee Plantation")) {
				plantation = g;
				break;
			}
		}

		Upgrade upgrade = null;
		for (Upgrade g : upgrades) {
			if (g.getIdGenerator() == plantation.getIdGenerator()) {
				upgrade = g;
				break;
			}
		}

		if (plantation != null && currentGame.getMoney() >= upgrade.getPrice() && !upgrade.isActive()) {
			synchronized (currentGame) {
				currentGame.setMoney(currentGame.getMoney() - upgrade.getPrice());
				plantation.setEarning(plantation.getEarning() * 2);
			}

			float extraProd = (float) (plantation.getQuantity() * 1);
			currentGame.setProduction_per_sec(currentGame.getProduction_per_sec() + extraProd);
			expenses += upgrade.getPrice();

            try {
                gameLogic.updateGenerators(idGame, plantation);
            } catch (DAOException e) {
				CustomUIException uiEx = new CustomUIException("No se ha podido establecer comunicación con el servidor de base de datos", "Error de Conexión", JOptionPane.ERROR_MESSAGE);
				uiEx.showDialog(null);
            }
            try {
                gameLogic.updateUpgrades(idGame, plantation.getIdGenerator());
            } catch (DAOException e) {
				CustomUIException uiEx = new CustomUIException("No se ha podido establecer comunicación con el servidor de base de datos", "Error de Conexión", JOptionPane.ERROR_MESSAGE);
				uiEx.showDialog(null);
            }
            saveCurrentProgress();

			gameView.updateCoffeeCount((int) currentGame.getMoney());
			gameView.updateProductionXSec(currentGame.getProduction_per_sec());
			gameView.updateUpgradePlantationText();
		}else{
			CustomUIException uiException = new CustomUIException("No tienes suficiente dinero para comprar la mejora", "Falta de Fondos", JOptionPane.ERROR_MESSAGE);
			uiException.showDialog(null);
		}
	}

	/**
	 * Cambia la vista del menú izquierdo a los Generadores.
	 */
	public void handleGenerators() {
		gameView.putGenerators();
	}

	/**
	 * Cambia la vista del menú izquierdo a las Mejoras.
	 */
	public void handleUpgrades() {
		gameView.putUpgrades();
	}

	/**
	 * Registra la producción generada automáticamente por los hilos secundarios.
	 *
	 * @param quantity Cantidad de recursos producidos en un ciclo de hilo.
	 */
	public void addAutogen(double quantity){
		autogen += quantity;
	}
}