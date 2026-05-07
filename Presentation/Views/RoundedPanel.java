package Presentation.Views;

import javax.swing.*;
import java.awt.*;

/**
 * Componente de panel personalizado con esquinas redondeadas.
 * Esta clase extiende de {@link JPanel} y redefine el proceso de renderizado para
 * permitir fondos con una curvatura específica.
 */
public class RoundedPanel extends JPanel {
    private Color backgroundColor;
    private int cornerRadius;

    /**
     * Constructor para el panel redondeado.
     * Configura el componente como no opaco para asegurar que las esquinas
     * exteriores al radio de curvatura sean transparentes y no muestren
     * el fondo rectangular por defecto de Swing.
     *
     * @param radius  El radio de curvatura para las cuatro esquinas (en píxeles).
     * @param bgColor El color de fondo que rellenará la forma redondeada.
     */
    public RoundedPanel(int radius, Color bgColor) {
        super();
        this.cornerRadius = radius;
        this.backgroundColor = bgColor;
        setOpaque(false);
    }

    /**
     * Sobrescribe la función de pintado del componente para dibujar la geometría personalizada.
     * Utiliza {@link Graphics2D} con suavizado (Anti-aliasing) para garantizar que los
     * bordes curvos no presenten pixelación. Rellena un rectángulo redondeado
     * que ocupa la totalidad de las dimensiones actuales del panel.
     *
     * @param g El contexto gráfico utilizado para el pintado.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (backgroundColor != null) {

            //Le da un color de fondo al panel
            g2.setColor(backgroundColor);

            //Le añade el borde redondeado al panel
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
        }

        g2.dispose();
    }

    /**
     * Sobrescribe la función de asignación de fondo para mantener la sincronización
     * entre la propiedad estándar de Swing y el atributo de color personalizado.
     *
     * @param bg El nuevo {@link Color} de fondo.
     */
    @Override
    public void setBackground(Color bg) {
        this.backgroundColor = bg;
        super.setBackground(bg);
    }
}