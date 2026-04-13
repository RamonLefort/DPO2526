package Presentation.Views;

import javax.swing.border.Border;
import java.awt.*;

public class RoundedBorder implements Border {
    private final int radius;
    private final Color borderColor;
    private final float thickness;

    public RoundedBorder(Color borderColor, int radius, float thickness) {
        this.borderColor = borderColor;
        this.radius = radius;
        this.thickness = thickness;
    }

    @Override
    public Insets getBorderInsets(Component c) {
        int padding = (int) Math.ceil((radius / 2f) + thickness);
        return new Insets(padding, padding, padding, padding);
    }

    @Override
    public boolean isBorderOpaque() {
        return false;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        float offset = thickness / 2f;

        g2.setColor(borderColor);
        g2.setStroke(new BasicStroke(thickness));
        g2.drawRoundRect((int)(x + offset), (int)(y + offset),
                (int)(width - thickness), (int)(height - thickness),
                radius, radius);

        g2.dispose();
    }
}