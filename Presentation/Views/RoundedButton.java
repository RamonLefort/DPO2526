package Presentation.Views;

import javax.swing.*;
import java.awt.*;

/**
 * Componente de botón personalizado con esquinas redondeadas y gestión de estados de color.
 * Esta clase extiende de {@link JButton} para ofrecer una estética moderna y reactiva.
 */
public class RoundedButton extends JButton {
    private int cornerRadius;
    private Color normalBackgroundColor;
    private Color pressedBackgroundColor;
    private Color normalTextColor;
    private Color pressedTextColor;

    /**
     * Constructor principal del botón redondeado.
     * Configura el comportamiento base de Swing para permitir el renderizado personalizado,
     * desactivando el relleno de área por defecto y habilitando el cursor de mano.
     *
     * @param text       Texto que mostrará el botón.
     * @param radius     Radio de curvatura de las esquinas en píxeles.
     * @param normalBg   Color de fondo en estado de reposo.
     * @param pressedBg  Color de fondo cuando el botón es pulsado.
     * @param normalText Color de la fuente en estado de reposo.
     * @param pressedText Color de la fuente cuando el botón es pulsado.
     */
    public RoundedButton(String text, int radius, Color normalBg, Color pressedBg, Color normalText, Color pressedText) {
        super(text);
        this.cornerRadius = radius;
        this.normalBackgroundColor = normalBg;
        this.pressedBackgroundColor = pressedBg;
        this.normalTextColor = normalText;
        this.pressedTextColor = pressedText;

        setContentAreaFilled(false);
        setFocusPainted(false);
        setOpaque(false);

        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    /**
     * Permite reconfigurar dinámicamente la paleta de colores del botón en tiempo de ejecución.
     *
     * @param normalBg   Nuevo color de fondo normal.
     * @param pressedBg  Nuevo color de fondo pulsado.
     * @param normalText Nuevo color de texto normal.
     * @param pressedText Nuevo color de texto pulsado.
     */
    public void resetButtonColors(Color normalBg, Color pressedBg, Color normalText, Color pressedText){
        this.normalBackgroundColor = normalBg;
        this.pressedBackgroundColor = pressedBg;
        this.normalTextColor = normalText;
        this.pressedTextColor = pressedText;
    }

    /**
     * Realiza el renderizado gráfico del cuerpo del botón.
     * Utiliza el modelo del componente ({@link ButtonModel}) para detectar si el usuario
     * está interactuando con el botón y aplica la lógica de color correspondiente.
     * Implementa suavizado (Anti-aliasing) para asegurar que la geometría redondeada
     * sea nítida y libre de artefactos visuales.
     *
     * @param g El contexto gráfico del sistema.
     */
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (getModel().isPressed()) {
            //Le pone el color de fondo y del texto
            g2.setColor(pressedBackgroundColor);
            setForeground(pressedTextColor);
        } else {
            //Le pone el color de fondo y del texto
            g2.setColor(normalBackgroundColor);
            setForeground(normalTextColor);
        }

        //Le da el borde redondeado al botón
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
        g2.dispose();

        super.paintComponent(g);
    }
}