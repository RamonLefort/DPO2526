package Presentation.Views;

import Bussiness.Entities.Stat;
import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Panel especializado en la representación gráfica de series temporales de datos.
 * Esta clase extiende {@link JPanel} y redefine la función de pintado para dibujar
 * un gráfico de líneas que muestra la evolución de los recursos del jugador
 * minuto a minuto.
 */
public class StatGraphPanel extends JPanel {

    private List<Stat> stats;
    private final Color LINE_COLOR = new Color(74, 44, 23);
    private final Color GRID_COLOR = new Color(220, 220, 220);
    private final Color TEXT_COLOR = new Color(100, 100, 100);

    /**
     * Actualiza la lista de datos a representar y solicita el redibujado del panel.
     *
     * @param stats Lista de objetos {@link Stat} que contienen la telemetría de la partida.
     */
    public void setStats(List<Stat> stats) {
        this.stats = stats;
        repaint();
    }

    /**
     * Transforma valores numéricos crudos en notaciones abreviadas legibles (K para miles, M para millones).
     *
     * @param value El valor numérico a formatear.
     * @return Una cadena de texto formateada.
     */
    private String formatValue(double value) {
        if (value < 100000) return String.format("%.1f", value);
        if (value < 100000000) return String.format("%.1fK", value / 1000.0);
        return String.format("%.1fM", value / 1000000.0);
    }

    /**
     * Realiza el renderizado completo del gráfico sobre el contexto gráfico del componente.
     *
     * El proceso de dibujo sigue este orden jerárquico:
     * 1. Validación de existencia de datos.
     * 2. Configuración de Antialiasing para trazados suaves.
     * 3. Cálculo de márgenes (padding) y factores de escala basados en el valor máximo de café.
     * 4. Dibujo de la cuadrícula horizontal y etiquetas del Eje Y.
     * 5. Dibujo de las etiquetas temporales del Eje X.
     * 6. Trazado de la línea de tendencia y puntos de datos.
     * 7. Resaltado del valor final actual.
     *
     * @param g El contexto gráfico {@link Graphics} proporcionado por Swing.
     */
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