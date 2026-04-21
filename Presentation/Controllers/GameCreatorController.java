package Presentation.Controllers;

import Bussiness.Managers.GameLogic;
import Presentation.Views.GameCreator;
import Bussiness.Managers.UserLogic;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Bussiness.Entities.User;

public class GameCreatorController implements ActionListener {

    private final GameCreator view;
    private final GameLogic gameLogic;
    private final UserLogic userLogic;
    private final ViewController viewController;
    private String username;

    public GameCreatorController(GameCreator view, GameLogic gameLogic, UserLogic userLogic, ViewController viewController, String username) {
        this.view = view;
        this.gameLogic = gameLogic;
        this.userLogic = userLogic;
        this.viewController = viewController;
        this.username = username;

        this.view.setActionListener(this);
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case GameCreator.BTN_BACK:
                handleBack();
                break;
            case GameCreator.BTN_LOGOUT:
                handleLogout();
                break;
            case GameCreator.BTN_CREATE:
                handleCreateGame();
                break;
            default:
                System.err.println("Unknown action command: " + e.getActionCommand());
        }
    }

    private void handleBack() {
        viewController.showView("GAME MENU");
    }

    private void handleLogout() {
        userLogic.logout();
        viewController.showView("LOGIN");
    }

    private void handleCreateGame() {
        String gameName = view.getGameName();

        if (gameName.isEmpty()) {
            view.showError("Por favor, introduce un nombre para la partida.");
            return;
        }

        if (gameName.equals("My Coffee Empire")) {
            view.showError("Por favor, introduce un nombre para la partida.");
            return;
        }

        User currentUser = userLogic.getCurrentUser();
        String currentUsername = currentUser.getUsername();

        int idGame = gameLogic.createGame(gameName, currentUsername);

        if (idGame == -1) {
            view.showError("No se pudo crear la partida. Inténtalo de nuevo.");
            return;
        }

        if (idGame == -2) {
            view.showError("Ya tienes una partida con ese nombre.");
            return;
        }

        if (idGame != -1) {
            viewController.showGameView(idGame, currentUsername);
        }
    }
}