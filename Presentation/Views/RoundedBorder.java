package Presentation.Views;

import javax.swing.border.Border;
import java.awt.*;

/**
 * Clase encargada de dibujar un borde con esquinas redondeadas para componentes de Swing.
 * Implementa la interfaz {@link Border} para permitir la personalización estética de
 * botones, paneles y campos de texto, permitiendo definir el color, el radio de
 * curvatura y el grosor de la línea.
 */
public class RoundedBorder implements Border {
    private final int radius;
    private final Color borderColor;
    private final float thickness;

    /**
     * Constructor para la creación de un borde redondeado personalizado.
     *
     * @param borderColor El color que tendrá la línea del borde.
     * @param radius      El radio de curvatura de las esquinas (en píxeles).
     * @param thickness   El grosor de la línea del borde.
     */
    public RoundedBorder(Color borderColor, int radius, float thickness) {
        this.borderColor = borderColor;
        this.radius = radius;
        this.thickness = thickness;
    }

    /**
     * Define los márgenes internos que el borde ocupa dentro del componente.
     * Calcula dinámicamente el espacio necesario basándose en la mitad del radio
     * y el grosor para asegurar que el contenido del componente no se solape con el borde.
     *
     * @param c El componente al que se le aplica este borde.
     * @return Los {@link Insets} calculados para el margen superior, izquierdo, inferior y derecho.
     */
    @Override
    public Insets getBorderInsets(Component c) {
        int padding = (int) Math.ceil((radius / 2f) + thickness);
        return new Insets(padding, padding, padding, padding);
    }

    /**
     * Indica si el borde es opaco.
     *
     * @return Siempre {@code false} ya que las esquinas redondeadas dejan pasar
     * la visibilidad del fondo en las áreas no dibujadas.
     */
    @Override
    public boolean isBorderOpaque() {
        return false;
    }

    /**
     * Realiza el renderizado gráfico del borde sobre el componente.
     * Utiliza {@link Graphics2D} para habilitar el suavizado (Anti-aliasing) y
     * aplica un desplazamiento (offset) basado en el grosor para evitar que
     * la línea sea recortada por los límites del contenedor.
     *
     * @param c      Componente sobre el que se pinta.
     * @param g      Contexto gráfico original.
     * @param x      Coordenada X inicial.
     * @param y      Coordenada Y inicial.
     * @param width  Ancho total disponible.
     * @param height Alto total disponible.
     */
    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        float offset = thickness / 2f;

        //Le da el color al borde
        g2.setColor(borderColor);

        //Le da el grosor al borde
        g2.setStroke(new BasicStroke(thickness));

        //Le da el redondeo al borde
        g2.drawRoundRect((int)(x + offset), (int)(y + offset), (int)(width - thickness), (int)(height - thickness), radius, radius);

        g2.dispose();
    }
}