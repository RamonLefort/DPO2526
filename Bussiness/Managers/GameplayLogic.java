package Bussiness.Managers;

import Bussiness.Entities.Generator;
import Bussiness.Entities.Upgrade;
import Persistance.DAO.GeneratorDAO;
import Persistance.DAO.UpgradeDAO;

public class GameplayLogic {

	private GameLogic gameLogic;
	private GeneratorDAO generatorDAO;
	private UpgradeDAO upgradeDAO;

	public GameplayLogic(GeneratorDAO generatorDAO, UpgradeDAO upgradeDAO, GameLogic gameLogic) {
		this.generatorDAO = generatorDAO;
		this.upgradeDAO = upgradeDAO;
		this.gameLogic = gameLogic;
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