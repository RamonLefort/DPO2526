package Presentation.Views;

import javax.swing.*;
import java.awt.*;

public class RoundedButton extends JButton {
    private int cornerRadius;
    private Color normalBackgroundColor;
    private Color pressedBackgroundColor;
    private Color normalTextColor;
    private Color pressedTextColor;

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

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (getModel().isPressed()) {
            g2.setColor(pressedBackgroundColor);
            setForeground(pressedTextColor);
        } else {
            g2.setColor(normalBackgroundColor);
            setForeground(normalTextColor);
        }

        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
        g2.dispose();

        super.paintComponent(g);
    }
}