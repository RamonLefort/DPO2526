package Presentation.Views;

import Bussiness.Entities.Stat;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public class StatsView extends JPanel {
    public static final String BTN_EXIT_STATS = "BTN_EXIT_STATS";

    private final Color BG_COLOR = new Color(248, 245, 240);
    private final Color PRIMARY_COFFEE = new Color(74, 44, 23);

    private DefaultTableModel tableModel;
    private JLabel lblTotalManual, lblTotalAuto, lblMaxProd, lblTotalSpent;
    private JButton btnExit;
    private StatGraphPanel graphPanel;

    public StatsView() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBackground(BG_COLOR);
        this.setBorder(new EmptyBorder(30, 50, 30, 50));

        //Título
        addHeader();
        add(Box.createRigidArea(new Dimension(0, 30)));

        //Estadísticas
        addEfficiencyPanel();
        add(Box.createRigidArea(new Dimension(0, 30)));

        //Tabla
        addHistoryTable();
        add(Box.createRigidArea(new Dimension(0, 30)));

        //Botón
        addFooter();
    }

    private void addHeader() {
        JLabel title = new JLabel("Game Performance Statistics");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(PRIMARY_COFFEE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.add(title);
    }

    private void addEfficiencyPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setOpaque(false);

        //Cartas de estadísticas
        lblTotalManual = createStatCard(panel, "Manual Clicks");
        panel.add(Box.createHorizontalGlue());
        lblTotalAuto = createStatCard(panel, "Auto Generation");
        panel.add(Box.createHorizontalGlue());
        lblMaxProd = createStatCard(panel, "Peak Production");
        panel.add(Box.createHorizontalGlue());
        lblTotalSpent = createStatCard(panel, "Total Investment");

        this.add(panel);
    }

    private JLabel createStatCard(JPanel container, String title) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(230, 230, 230), 1), new EmptyBorder(15, 20, 15, 20)));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblValue = new JLabel("0");
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblValue.setForeground(PRIMARY_COFFEE);
        lblValue.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(lblTitle);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(lblValue);
        container.add(card);

        return lblValue;
    }

    private void addHistoryTable() {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setOpaque(false);

        //Título
        JLabel lblSection = new JLabel("Growth Progression");
        lblSection.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblSection.setAlignmentX(Component.CENTER_ALIGNMENT);
        container.add(lblSection);
        container.add(Box.createRigidArea(new Dimension(0, 10)));

        //Gráfica
        graphPanel = new StatGraphPanel();
        graphPanel.setPreferredSize(new Dimension(800, 250));
        graphPanel.setBackground(Color.WHITE);
        graphPanel.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
        graphPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        container.add(graphPanel);

        container.add(Box.createRigidArea(new Dimension(0, 20)));

        //Tabla
        String[] cols = {"Time", "Coffees", "Clicks", "Auto"};
        tableModel = new DefaultTableModel(cols, 0);
        JTable table = new JTable(tableModel);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setPreferredSize(new Dimension(800, 200));
        container.add(scroll);

        this.add(container);
    }

    private void addFooter() {
        btnExit = new RoundedButton("Return to Menu", 20, PRIMARY_COFFEE, Color.WHITE, Color.WHITE, PRIMARY_COFFEE);
        btnExit.setActionCommand(BTN_EXIT_STATS);
        btnExit.setMaximumSize(new Dimension(200, 40));
        btnExit.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.add(btnExit);
    }

    public void setActionListener(ActionListener l) {
        btnExit.addActionListener(l);
    }

    /**
     * Carga los datos finales y la tabla de progresión
     */
    public void displayStats(List<Stat> stats) {
        tableModel.setRowCount(0);
        if (stats != null && !stats.isEmpty()) {
            Stat last = stats.get(stats.size() - 1);
            lblTotalManual.setText(String.valueOf(last.getManualClicksTotal()));
            lblTotalAuto.setText(String.format("%.1f", last.getAutoGeneratedTotal()));
            lblMaxProd.setText(String.format("%.2f/s", last.getMaxProductionRate()));
            lblTotalSpent.setText(String.format("%.0f", last.getUpgradesExpenses()));
            for (Stat s : stats) {
                Object[] row = {
                        "Minute " + s.getMinuteMark(),
                        String.format("%.2f", s.getMoneyAtMinute()),
                        s.getManualClicksTotal(),
                        String.format("%.1f", s.getAutoGeneratedTotal())
                };
                tableModel.addRow(row);
            }
        }
        graphPanel.setStats(stats);
    }
}