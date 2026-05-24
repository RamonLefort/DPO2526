package Bussiness.Managers;

import Bussiness.Entities.Game;
import Bussiness.Entities.Generator;
import Persistance.DAO.GeneratorDAO;
import Persistance.DAO.UpgradeDAO;
import Presentation.Controllers.GameController;
import Presentation.Views.GameView;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase encargada de gestionar la dinámica activa de la partida en tiempo real.
 * Controla la lógica de generación (manual y automática), la ejecución de hilos
 * de producción y el procesamiento de transacciones de compra dentro del juego.
 */
public class GameplayLogic {

	private GameLogic gameLogic;
	private GeneratorDAO generatorDAO;
	private UpgradeDAO upgradeDAO;
	private final List<GeneratorThread> activeThreads = new ArrayList<>();

	/**
	 * Constructor que inicializa las dependencias de lógica y persistencia necesarias para el gameplay.
	 *
	 * @param generatorDAO DAO para la gestión de datos de generadores.
	 * @param upgradeDAO DAO para la gestión de datos de mejoras.
	 * @param gameLogic Lógica de gestión de partidas.
	 */
	public GameplayLogic(GeneratorDAO generatorDAO, UpgradeDAO upgradeDAO, GameLogic gameLogic) {
		this.generatorDAO = generatorDAO;
		this.upgradeDAO = upgradeDAO;
		this.gameLogic = gameLogic;
	}

	/**
	 * Inicia los procesos en segundo plano para la producción automática de café.
	 *
	 * @param gameId Identificador de la partida.
	 * @param game Objeto de la partida actual para actualizar el estado.
	 * @param generators Lista de generadores que deben activarse.
	 * @param gameView Vista del juego para actualizar la interfaz gráfica.
	 * @param gameController Controlador para coordinar eventos de juego.
	 */
	public void startAutoGenerators(int gameId, Game game, List<Generator> generators, GameView gameView, GameController gameController) {
		stopAutoGenerators();
		for (Generator gen : generators) {
			GeneratorThread t = new GeneratorThread(gen, game, gameView, gameController);
			activeThreads.add(t);
			t.start();
		}
	}

	/**
	 * Detiene todos los hilos de generación automática activos de forma segura.
	 */
	public void stopAutoGenerators() {
		for (GeneratorThread t : activeThreads) {
			t.stopGenerator();
		}
		activeThreads.clear();
	}
}