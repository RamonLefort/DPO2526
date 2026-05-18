import Bussiness.Managers.GameLogic;
import Bussiness.Managers.GameplayLogic;
import Bussiness.Managers.StatLogic;
import Bussiness.Managers.UserLogic;
import Persistance.Configuration.JsonConfigurationDAO;
import Persistance.Configuration.MySQLDAO;
import Persistance.DAO.*;
import Presentation.Controllers.ViewController;
import Presentation.Exceptions.CustomUIException;

import javax.swing.*;
import java.io.IOException;
import java.sql.SQLException;

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
            MySQLDAO mySQLDAO = null;
            try {
                mySQLDAO = MySQLDAO.getInstance(jsonDAO);
            } catch (IOException e) {
                CustomUIException uiException = new CustomUIException("No se ha podido realizar la lectura del archivo de configuración del sistema", "Error de Configuración", JOptionPane.ERROR_MESSAGE);
                uiException.showDialog(null);
            }
            try {
                mySQLDAO.connect();
            } catch (SQLException e) {
                CustomUIException uiException = new CustomUIException("No se ha podido establecer comunicación con el servidor de base de datos", "Error de Conexión", JOptionPane.ERROR_MESSAGE);
                uiException.showDialog(null);
            }

            UserDAO userDAO = new UserDAO(mySQLDAO);
            GameDAO gameDAO = new GameDAO(mySQLDAO);
            GeneratorDAO generatorDAO = new GeneratorDAO(mySQLDAO);
            StatDAO statDAO = new StatDAO(mySQLDAO);
            UpgradeDAO upgradeDAO = new UpgradeDAO(mySQLDAO);
            UserLogic userLogic = new UserLogic(userDAO);
            GameLogic gameLogic = new GameLogic(gameDAO, generatorDAO, upgradeDAO);
            GameplayLogic gameplayLogic = new GameplayLogic(generatorDAO, upgradeDAO, gameLogic);
            StatLogic statLogic = new StatLogic(statDAO, gameDAO, userDAO);

            ViewController viewController = new ViewController(userLogic, gameLogic, gameplayLogic, statLogic);
            viewController.start();
        });
    }
}