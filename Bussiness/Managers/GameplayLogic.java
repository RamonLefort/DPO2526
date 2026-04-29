package Bussiness.Managers;

import Bussiness.Entities.Game;
import Bussiness.Entities.Generator;
import Bussiness.Entities.Upgrade;
import Persistance.DAO.GeneratorDAO;
import Persistance.DAO.UpgradeDAO;
import Presentation.Views.GameView;

import java.util.ArrayList;
import java.util.List;

public class GameplayLogic {

	private GameLogic gameLogic;
	private GeneratorDAO generatorDAO;
	private UpgradeDAO upgradeDAO;
	private final List<GeneratorThread> activeThreads = new ArrayList<>();

	public GameplayLogic(GeneratorDAO generatorDAO, UpgradeDAO upgradeDAO, GameLogic gameLogic) {
		this.generatorDAO = generatorDAO;
		this.upgradeDAO = upgradeDAO;
		this.gameLogic = gameLogic;
	}

	public void startAutoGenerators(int gameId, Game game, List<Generator> generators, GameView gameView) {
		stopAutoGenerators();
		for (Generator gen : generators) {
			GeneratorThread t = new GeneratorThread(gen, game, gameView);
			activeThreads.add(t);
			t.start();
		}
	}

	public void stopAutoGenerators() {
		for (GeneratorThread t : activeThreads) {
			t.stopGenerator();
		}
		activeThreads.clear();
	}

	public void generateManual(int gameId) {
	}

	public void generateAuto(int gameId) {
	}

	public boolean buyGenerator(int gameId, Generator gen) {
		return true;
	}

	public boolean buyUpgrade(int gameId, Upgrade upg) {
		return true;
	}

	public void startAutoGenerators(int gameId) {
	}

	public int calcTotalProduction(int gameId) {
		return 0;
	}

	public int calcNextPrice(Generator gen) {
		return 0;
	}

	public void recordStat(int gameId) {
	}
}