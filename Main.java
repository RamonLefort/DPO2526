import Bussiness.Managers.UserLogic;
import Persistance.Configuration.JsonConfigurationDAO;
import Persistance.Configuration.MySQLDAO;
import Persistance.DAO.UserDAO;
import Presentation.Controllers.ViewController;
import Presentation.Views.LoginWindow;
import Presentation.Views.RegisterWindow;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            JsonConfigurationDAO jsonDAO = new JsonConfigurationDAO();
            MySQLDAO mySQLDAO = MySQLDAO.getInstance(jsonDAO);
            mySQLDAO.connect();


            UserDAO userDAO = new UserDAO(mySQLDAO);
            UserLogic userLogic = new UserLogic(userDAO);

            new ViewController(userLogic).start();
        });
    }
}