package Presentation.Controllers;

import Bussiness.Entities.Game;
import Bussiness.Entities.Stat;
import Bussiness.Managers.GameLogic;
import Bussiness.Managers.UserLogic;
import Presentation.Views.GameMenuView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import Bussiness.Managers.StatLogic;
public class GameMenuController implements ActionListener {

	private final GameMenuView gameMenuView;
	private final GameLogic gameLogic;
	private final UserLogic userLogic;
	private final ViewController viewController;
	private final StatLogic statLogic;

	public GameMenuController(GameMenuView gameMenuView, GameLogic gameLogic, StatLogic statLogic, UserLogic userLogic, ViewController viewController) {
		this.gameMenuView = gameMenuView;
		this.gameLogic = gameLogic;
		this.statLogic = statLogic;
		this.userLogic = userLogic;
		this.viewController = viewController;
		this.gameMenuView.setActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
			case GameMenuView.BTN_BACK     -> handleBack();
			case GameMenuView.BTN_LOGOUT   -> handleLogout();
			case GameMenuView.BTN_NEW_GAME -> handleNewGame();
			default -> {
				if (e.getActionCommand().contains(GameMenuView.BTN_CONTINUE)) {
					int idGame = Integer.parseInt(e.getActionCommand().replace(GameMenuView.BTN_CONTINUE, ""));
					handleResumeGame(idGame);
				} else if (e.getActionCommand().startsWith(GameMenuView.BTN_STATS)) {
					int idGame = Integer.parseInt(e.getActionCommand().replace(GameMenuView.BTN_STATS, ""));
					handleStats(idGame);
				}
			}
		}
	}

	public void handleResumeGame(int idGame) {
		viewController.showGameView(idGame, userLogic.getCurrentUser().getUsername());
	}

	private void handleStats(int idGame) {
		viewController.showStats(idGame);
	}

	private void handleBack() {
		viewController.showView("SETTINGS");
	}

	private void handleLogout() {
		userLogic.logout();
		viewController.showView("LOGIN");
	}

	public void handleNewGame() {
		viewController.showView("GAME CREATOR");
	}

	public void loadGames() {
		String username = userLogic.getCurrentUser().getUsername();
		List<Game> allGames = gameLogic.getUserGames(username);
		List<Stat> finishedGames = statLogic.getAllStats();

		List<Game> currentGames = new ArrayList<>();
		for (int i = 0; i < allGames.size(); i++) {
			Game game = allGames.get(i);
			boolean isFinished = false;
			for (int j = 0; j < finishedGames.size(); j++) {
				if (finishedGames.get(j).getIdGame() == game.getIdGame()) {
					isFinished = true;
					break;
				}
			}
			if (!isFinished) {
				currentGames.add(game);
			}
		}

		gameMenuView.loadCurrentGames(currentGames);
		gameMenuView.loadFinishedGames(finishedGames);
	}


	public void handleDeleteGame() {}
	public void handleCloneGame() {}
}