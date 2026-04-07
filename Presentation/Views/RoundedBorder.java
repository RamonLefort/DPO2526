package Presentation.Views;

import javax.swing.border.Border;
import java.awt.*;

public class RoundedBorder implements Border {
    private final int radius;
    private final Color color;
    private final int thickness;
    private final int borderType;

    public final int INSIDE_BORDER = 0;
    public final int OUTSIDE_BORDER = 0;

    public RoundedBorder(Color color, int radius, int thickness, int borderType) {
        this.color = color;
        this.radius = radius;
        this.thickness = thickness;
        this.borderType = borderType;
    }

    @Override
    public Insets getBorderInsets(Component c) {
        // Devuelve el margen interno para que el texto no se superponga con la curva
        int padding = radius / 2;
        return new Insets(padding, padding, padding, padding);
    }

    @Override
    public boolean isBorderOpaque() {
        return false; // Crucial para que se vean las esquinas transparentes
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        // Conversión a Graphics2D para habilitar el motor de renderizado avanzado
        Graphics2D g2 = (Graphics2D) g.create();

        // Activamos el Antialiasing para que las curvas no se vean pixeladas (efecto escalera)
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(color);
        g2.setStroke(new BasicStroke(thickness));

        if(borderType == 0){
            // Dibujamos el rectángulo redondeado hacia dentro
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        }else{
            // Dibujamos el rectángulo redondeado hacia fuera
            g2.drawRoundRect(x, y, width + 1, height + 1, radius, radius);
        }

        // Liberamos los recursos de memoria del objeto gráfico
        g2.dispose();
    }
}