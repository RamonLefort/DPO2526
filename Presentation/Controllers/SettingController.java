package Presentation.Controllers;

import Bussiness.Exceptions.DAOException;
import Bussiness.Managers.UserLogic;
import Presentation.Exceptions.CustomUIException;
import Presentation.Views.SettingView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SettingController implements ActionListener {

    private final SettingView settingView;
    private final UserLogic userLogic;
    private final ViewController viewController;

    public SettingController(SettingView settingView, UserLogic userLogic, ViewController viewController) {
        this.settingView = settingView;
        this.userLogic = userLogic;
        this.viewController = viewController;
        this.settingView.setActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case SettingView.BTN_LOGOUT:
                handleLogout();
                break;
            case SettingView.BTN_DELETE_ACCOUNT:
                handleDeleteAccount();
                break;
            default:
                System.err.println("Comando desconocido: " + e.getActionCommand());
        }
    }

    public void handleLogout() {
        userLogic.logout();
        viewController.showView("LOGIN");
    }

    public void handleDeleteAccount() {
        String username = userLogic.getCurrentUser().getUsername();
        try {
            userLogic.deleteAccount(username);
        } catch (DAOException e) {
            CustomUIException uiException = new CustomUIException("No se ha podido establecer comunicación con el servidor de base de datos", "Error de Conexión", JOptionPane.ERROR_MESSAGE);
            uiException.showDialog(null);
        }
        viewController.showView("LOGIN");
    }
}
