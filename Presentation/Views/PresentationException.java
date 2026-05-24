package Presentation.Views;

import javax.swing.*;

/**
 * Vista encargada de renderizar los diálogos de error del sistema.
 */
public class PresentationException {

    /**
     * Muestra un diálogo de error modal con un icono de fallo.
     *
     * @param message El mensaje detallado que describe el problema ocurrido.
     * @param title   El título que aparecerá en la barra superior de la ventana.
     */
    public void showErrorDialog(String message, String title) {
        JOptionPane.showMessageDialog(
                null,
                message,
                title,
                JOptionPane.ERROR_MESSAGE
        );
    }

    /**
     * Muestra un diálogo de advertencia modal con un icono de precaución.
     *
     * @param message El mensaje de advertencia para el usuario.
     * @param title   El título que aparecerá en la barra superior de la ventana.
     */
    public void showWarningDialog(String message, String title) {
        JOptionPane.showMessageDialog(
                null,
                message,
                title,
                JOptionPane.WARNING_MESSAGE
        );
    }
}