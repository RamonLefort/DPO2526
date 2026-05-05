package Presentation.Views;

import Bussiness.Entities.Stat;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class StatGraphPanel extends JPanel {
    private List<Stat> stats;
    private final Color LINE_COLOR = new Color(74, 44, 23);
    private final Color GRID_COLOR = new Color(220, 220, 220);
    private final Color TEXT_COLOR = new Color(100, 100, 100);

    public void setStats(List<Stat> stats) {
        this.stats = stats;
        repaint();
    }

    /**
     * Convierte valores numéricos grandes en formatos legibles (K, M, B).
     */
    private String formatValue(double value) {
        if (value < 100000) return String.format("%.1f", value);
        if (value < 100000000) return String.format("%.1fK", value / 1000.0);
        return String.format("%.1fM", value / 1000000.0);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (stats == null || stats.isEmpty()){
            return;
        }

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int padding = 60;
        int width = getWidth() - 2 * padding;
        int height = getHeight() - 2 * padding;

        double maxCoffees = stats.stream().mapToDouble(Stat::getMoneyAtMinute).max().orElse(1);
        int numPoints = stats.size();

        //Eje Y
        g2.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        for (int i = 0; i <= 5; i++) {
            int y = getHeight() - padding - (i * height / 5);
            g2.setColor(GRID_COLOR);
            g2.drawLine(padding, y, getWidth() - padding, y);

            g2.setColor(TEXT_COLOR);
            String valLabel = formatValue((maxCoffees / 5) * i);
            g2.drawString(valLabel, 5, y + 4);
        }

        //Eje X
        g2.setColor(TEXT_COLOR);
        int labelInterval = Math.max(1, numPoints / 10);

        for (int i = 0; i < numPoints; i++) {
            if (i % labelInterval == 0 || i == numPoints - 1) {
                int x = padding + (i * width / (numPoints - 1));
                String minLabel = "Min. " + stats.get(i).getMinuteMark();
                g2.drawString(minLabel, x - 10, getHeight() - padding + 20);
            }
        }

        //Línea
        g2.setColor(LINE_COLOR);
        g2.setStroke(new BasicStroke(3f));

        for (int i = 0; i < numPoints - 1; i++) {
            int x1 = padding + (i * width / (numPoints - 1));
            int y1 = getHeight() - padding - (int) ((stats.get(i).getMoneyAtMinute() / maxCoffees) * height);
            int x2 = padding + ((i + 1) * width / (numPoints - 1));
            int y2 = getHeight() - padding - (int) ((stats.get(i + 1).getMoneyAtMinute() / maxCoffees) * height);

            g2.drawLine(x1, y1, x2, y2);
            g2.fillOval(x1 - 3, y1 - 3, 6, 6);
        }

        //Valor final
        int lastX = padding + width;
        double lastVal = stats.get(numPoints - 1).getMoneyAtMinute();
        int lastY = getHeight() - padding - (int) ((lastVal / maxCoffees) * height);

        g2.fillOval(lastX - 4, lastY - 4, 8, 8);
        g2.setFont(new Font("Segoe UI", Font.BOLD, 11));
        g2.drawString(formatValue(lastVal), lastX - 25, lastY - 12);
    }
}