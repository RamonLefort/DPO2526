package Presentation.Controllers;

import Bussiness.Managers.GameLogic;
import Bussiness.Managers.StatLogic;
import Bussiness.Managers.UserLogic;
import Presentation.Views.*;
import Bussiness.Managers.GameplayLogic;

import javax.swing.*;
import java.awt.*;

public class ViewController {
    private final JFrame frame;
    private final CardLayout cardLayout;
    private final JPanel rootPanel;
    private final UserLogic userLogic;
    private final GameLogic gameLogic;
    private final GameplayLogic gameplayLogic;
    private GameCreator gameCreator;
    private GameCreatorController gameCreatorController;
    private GameController gameController;
    private final StatLogic statLogic;
    private GameMenuController gameMenuController;

    public ViewController(UserLogic userLogic, GameLogic gameLogic, GameplayLogic gameplayLogic, StatLogic statLogic) {
        this.userLogic = userLogic;
        this.gameLogic = gameLogic;
        this.gameplayLogic = gameplayLogic;
        this.frame = new JFrame("Coffee Clicker");
        this.cardLayout = new CardLayout();
        this.rootPanel = new JPanel(cardLayout);
        this.statLogic = statLogic;

        Image icon = new ImageIcon("assets/icono.png").getImage();
        frame.setIconImage(icon);

        setupFrame();
        setupViews();
    }

    private void setupViews() {
        LoginWindow loginView = new LoginWindow();
        RegisterWindow registerView = new RegisterWindow();
        GameView gameView = new GameView();
        SettingView settingView = new SettingView();
        GameMenuView gameMenuView = new GameMenuView();
        gameCreator = new GameCreator();

        gameCreatorController = new GameCreatorController(gameCreator, gameLogic, userLogic, this, "");
        gameController = new GameController(gameView, gameplayLogic, this, 0, "", gameLogic, userLogic, statLogic);

        new LoginController(loginView, userLogic, this);
        new RegisterController(registerView, userLogic, this);
        new SettingController(settingView, userLogic, this);
        gameMenuController = new GameMenuController(gameMenuView, gameLogic, statLogic, userLogic, this);
        StatsView statsView = new StatsView();


        rootPanel.add(statsView, "STATS");
        rootPanel.add(loginView, "LOGIN");
        rootPanel.add(registerView, "REGISTER");
        rootPanel.add(gameView, "GAME");
        rootPanel.add(settingView, "SETTINGS");
        rootPanel.add(gameMenuView, "GAME MENU");
        rootPanel.add(gameCreator, "GAME CREATOR");
    }

    public void showView(String viewName) {
        if (viewName.equals("GAME MENU")) {
            gameMenuController.loadGames();
        }
        cardLayout.show(rootPanel, viewName);
    }
    public void showGameView(int idGame, String username) {
        gameController.loadGame(idGame, username);
        cardLayout.show(rootPanel, "GAME");
    }

    private void setupFrame() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1100, 700);
        frame.setLocationRelativeTo(null);
        frame.setContentPane(rootPanel);
    }

    public void showStats(int idGame) {
        cardLayout.show(rootPanel, "STATS");
    }

    public void start() {
        frame.setVisible(true);
        showView("LOGIN");
    }


}