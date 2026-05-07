import Bussiness.Managers.GameLogic;
import Bussiness.Managers.GameplayLogic;
import Bussiness.Managers.StatLogic;
import Bussiness.Managers.UserLogic;
import Persistance.Configuration.JsonConfigurationDAO;
import Persistance.Configuration.MySQLDAO;
import Persistance.DAO.*;
import Presentation.Controllers.ViewController;
import Persistance.DAO.SettingDAO;
import javax.swing.*;

/**
 * Clase principal que actúa como el motor de arranque de la aplicación.
 */
public class Main {

    /**
     * Función de entrada principal de la aplicación.
     *
     * @param args Argumentos de la línea de comandos (no utilizados en esta aplicación).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            JsonConfigurationDAO jsonDAO = new JsonConfigurationDAO();
            MySQLDAO mySQLDAO = MySQLDAO.getInstance(jsonDAO);
            mySQLDAO.connect();

            UserDAO userDAO = new UserDAO(mySQLDAO);
            SettingDAO settingDAO = new SettingDAO(mySQLDAO);
            GameDAO gameDAO = new GameDAO(mySQLDAO);
            GeneratorDAO generatorDAO = new GeneratorDAO(mySQLDAO);
            StatDAO statDAO = new StatDAO(mySQLDAO);
            UpgradeDAO upgradeDAO = new UpgradeDAO(mySQLDAO);
            UserLogic userLogic = new UserLogic(userDAO, settingDAO);
            GameLogic gameLogic = new GameLogic(gameDAO, generatorDAO, upgradeDAO);
            GameplayLogic gameplayLogic = new GameplayLogic(generatorDAO, upgradeDAO, gameLogic);
            StatLogic statLogic = new StatLogic(statDAO, gameDAO, userDAO);

            ViewController viewController = new ViewController(userLogic, gameLogic, gameplayLogic, statLogic);
            viewController.start();
        });
    }
}