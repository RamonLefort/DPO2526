package Presentation.Views;

import javax.swing.*;

/**
 * Vista encargada de renderizar los diálogos de error del sistema.
 */
public class PresentationException {

    /**
     * Muestra una ventana de error genérica.
     *
     * @param message El mensaje detallado del error.
     * @param title El título de la ventana.
     */
    public void showErrorDialog(String message, String title) {
        // Aquí encapsulamos la lógica de Swing. Si mañana quieres cambiar el diseño,
        // poner un icono personalizado, o cambiar de JOptionPane a un JDialog a medida,
        // solo tocas este archivo.
        JOptionPane.showMessageDialog(
                null,
                message,
                title,
                JOptionPane.ERROR_MESSAGE
        );
    }

    /**
     * Muestra una ventana de advertencia.
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