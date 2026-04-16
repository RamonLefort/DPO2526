package Presentation.Controllers;

import Bussiness.Managers.UserLogic;
import Presentation.Views.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;


public class ViewController {
    private final JFrame frame;
    private final CardLayout cardLayout;
    private final JPanel rootPanel;
    private final UserLogic userLogic;

    public ViewController(UserLogic userLogic) {
        this.userLogic = userLogic;
        this.frame = new JFrame("Coffee Clicker");
        this.cardLayout = new CardLayout();
        this.rootPanel = new JPanel(cardLayout);

        JFrame frame = new JFrame("Coffee Clicker");
        Image icon = new ImageIcon("assets/icono.png").getImage();
        frame.setIconImage(icon);

        setupFrame();
        setupViews();
    }

    private void setupFrame() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1100, 700);
        frame.setLocationRelativeTo(null);
        frame.setContentPane(rootPanel);
    }

    private void setupViews() {
        LoginWindow loginView = new LoginWindow();
        RegisterWindow registerView = new RegisterWindow();
        GameView gameView = new GameView();
        SettingView settingView = new SettingView();
        GameMenuView gameMenuView = new GameMenuView();
        GameCreator gameCreator = new GameCreator();

        new LoginController(loginView, userLogic, this);
        new RegisterController(registerView, userLogic, this);
        new SettingController(settingView,userLogic, this);

        rootPanel.add(loginView, "LOGIN");
        rootPanel.add(registerView, "REGISTER");
        rootPanel.add(gameView, "GAME");
        rootPanel.add(settingView, "SETTINGS");
        rootPanel.add(gameMenuView, "GAME MENU");
        rootPanel.add(gameCreator, "GAME CREATOR");
    }

    public void showView(String viewName) {
        cardLayout.show(rootPanel, viewName);
    }

    public void start() {
        frame.setVisible(true);
        showView("LOGIN");
    }


}