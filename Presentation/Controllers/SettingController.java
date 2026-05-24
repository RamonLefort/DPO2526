package Presentation.Controllers;

import Bussiness.Exceptions.BusinessException;
import Bussiness.Managers.UserLogic;
import Presentation.Views.PresentationException;
import Presentation.Views.SettingView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Controlador de la capa de presentación responsable de gestionar la lógica
 * de la vista de configuración ({@link SettingView}).
 */
public class SettingController implements ActionListener {

    private final SettingView settingView;
    private final UserLogic userLogic;
    private final ViewController viewController;

    /**
     * Construye un nuevo controlador de configuración e inyecta las dependencias necesarias.
     * Vincula este controlador como el escuchador de eventos de la vista proporcionada.
     *
     * @param settingView    La vista de configuración asociada.
     * @param userLogic      Instancia de la capa de negocio para gestionar sesiones y datos de usuario.
     * @param viewController El gestor de navegación global de la aplicación.
     */
    public SettingController(SettingView settingView, UserLogic userLogic, ViewController viewController) {
        this.settingView = settingView;
        this.userLogic = userLogic;
        this.viewController = viewController;
        this.settingView.setActionListener(this);
    }

    /**
     * Procesa los eventos de acción provenientes de la vista de configuración.
     *
     * @param e El evento de acción capturado, identificado por su comando.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case SettingView.BTN_LOGOUT:
                handleLogout();
                break;
            case SettingView.BTN_DELETE_ACCOUNT:
                handleDeleteAccount();
                break;
        }
    }

    /**
     * Ejecuta el cierre de sesión del usuario actual y redirige a la vista de login.
     */
    public void handleLogout() {
        userLogic.logout();
        viewController.showView("LOGIN");
    }

    /**
     * Intenta eliminar la cuenta del usuario actual y redirige a la vista de login.
     */
    public void handleDeleteAccount() {
        String username = userLogic.getCurrentUser().getUsername();
        try {
            userLogic.deleteAccount(username);
        } catch (BusinessException e) {
            PresentationException presentationException = new PresentationException();
            presentationException.showErrorDialog("No se ha podido establecer comunicación con el servidor de base de datos", "Error de Conexión");
        }
        viewController.showView("LOGIN");
    }
}
