package Presentation.Views;

import Bussiness.Entities.Stat;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Vista de tablero de estadísticas que muestra el rendimiento final de una partida.
 * Esta clase hereda de {@link JPanel} y organiza la información en tres niveles visuales:
 * indicadores rápidos, una representación gráfica de la evolución del capital
 * y una tabla detallada con el historial de eventos por minuto.
 */
public class StatsView extends JPanel {

    /** Comando de acción para cerrar la vista de estadísticas y volver al menú. */
    public static final String BTN_EXIT_STATS = "BTN_EXIT_STATS";
    /** Comando de acción para ir a la pantalla de settings. */
    public static final String BTN_SETTINGS = "SETTINGS";

    public static final String COMBO_USER_CHANGED = "COMBO_USER_CHANGED";
    public static final String COMBO_GAME_CHANGED = "COMBO_GAME_CHANGED";

    private final Color BG_COLOR = new Color(248, 245, 240);
    private final Color PRIMARY_COFFEE = new Color(74, 44, 23);

    private DefaultTableModel tableModel;
    private JLabel lblTotalManual, lblTotalAuto, lblMaxProd, lblTotalSpent;
    private JButton btnExit;
    private JButton btnSettings;
    private StatGraphPanel graphPanel;

    private JComboBox<String> cbUsers;
    private JComboBox<String> cbGames;

    /**
     * Constructor de la vista.
     * Configura un {@link BoxLayout} vertical y orquesta la
     * construcción de la interfaz mediante la adición secuencial de la cabecera,
     * el panel de eficiencia, la tabla de historial con gráfico y el pie de página.
     */
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

    /**
     * Añade el título principal de la vista centrado en la parte superior.
     */
    private void addHeader() {
        JPanel headerContainer = new JPanel();
        headerContainer.setLayout(new BoxLayout(headerContainer, BoxLayout.X_AXIS));
        headerContainer.setOpaque(false);
        headerContainer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JLabel title = new JLabel("Game Performance Statistics");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(PRIMARY_COFFEE);
        title.setAlignmentY(Component.CENTER_ALIGNMENT);

        // Selector de Usuarios
        JLabel lblUser = new JLabel("User: ");
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblUser.setForeground(PRIMARY_COFFEE);
        lblUser.setAlignmentY(Component.CENTER_ALIGNMENT);
        headerContainer.add(lblUser);

        cbUsers = new JComboBox<>();
        cbUsers.setActionCommand(COMBO_USER_CHANGED);
        cbUsers.setMaximumSize(new Dimension(140, 30));
        cbUsers.setAlignmentY(Component.CENTER_ALIGNMENT);
        headerContainer.add(cbUsers);

        headerContainer.add(Box.createRigidArea(new Dimension(15, 0)));

        // Selector de Partidas
        JLabel lblGame = new JLabel("Game: ");
        lblGame.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblGame.setForeground(PRIMARY_COFFEE);
        lblGame.setAlignmentY(Component.CENTER_ALIGNMENT);
        headerContainer.add(lblGame);

        cbGames = new JComboBox<>();
        cbGames.setActionCommand(COMBO_GAME_CHANGED);
        cbGames.setMaximumSize(new Dimension(160, 30));
        cbGames.setAlignmentY(Component.CENTER_ALIGNMENT);
        headerContainer.add(cbGames);

        headerContainer.add(Box.createHorizontalGlue());

        btnSettings = new RoundedButton("Ajustes", 20, PRIMARY_COFFEE, BG_COLOR, BG_COLOR, PRIMARY_COFFEE);
        btnSettings.setActionCommand(BTN_SETTINGS);
        btnSettings.setBorder(new RoundedBorder(PRIMARY_COFFEE, 20, 1));
        btnSettings.setAlignmentY(Component.CENTER_ALIGNMENT);

        Dimension backDim = new Dimension(85, 35);
        btnSettings.setPreferredSize(backDim);
        btnSettings.setMinimumSize(backDim);
        btnSettings.setMaximumSize(backDim);

        headerContainer.add(title);
        headerContainer.add(Box.createHorizontalGlue());
        headerContainer.add(btnSettings);

        headerContainer.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.add(headerContainer);
    }

    /**
     * Crea y añade una fila horizontal que contiene las cartas de estadísticas rápidas.
     * Gestiona la distribución proporcional de los indicadores de clics manuales,
     * generación automática, producción pico e inversión total.
     */
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

    /**
     * Función para crear una tarjeta de estadística.
     * Crea un panel redondeado con un título de métrica y un valor numérico destacado.
     *
     * @param container El panel horizontal donde se insertará la carta.
     * @param title     El nombre de la métrica .
     * @return La referencia al {@link JLabel} del valor para poder actualizarlo dinámicamente.
     */
    private JLabel createStatCard(JPanel container, String title) {
        RoundedPanel card = new RoundedPanel(20, Color.WHITE);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new RoundedBorder(PRIMARY_COFFEE, 20, 1));

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

    /**
     * Construye la sección central de análisis de progresión.
     * Integra el componente {@link StatGraphPanel} para la visualización de tendencias
     * y un {@link JTable} para el desglose numérico exhaustivo por minuto.
     */
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

    /**
     * Añade el botón de salida en la parte inferior de la vista.
     */
    private void addFooter() {
        btnExit = new RoundedButton("Return to Menu", 20, PRIMARY_COFFEE, Color.WHITE, Color.WHITE, PRIMARY_COFFEE);
        btnExit.setActionCommand(BTN_EXIT_STATS);
        btnExit.setBorder(new RoundedBorder(PRIMARY_COFFEE, 20, 1));
        btnExit.setMaximumSize(new Dimension(200, 40));
        btnExit.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.add(btnExit);
    }

    /**
     * Vincula el controlador de eventos al botón de retorno al menú.
     *
     * @param l El {@link ActionListener} que procesará la acción de salida.
     */
    public void setActionListener(ActionListener l) {
        btnExit.addActionListener(l);
        btnSettings.addActionListener(l);
        cbUsers.addActionListener(l);
        cbGames.addActionListener(l);
    }

    public void populateUsers(List<String> usernames) {
        ActionListener[] listeners = cbUsers.getActionListeners();
        for (ActionListener l : listeners) cbUsers.removeActionListener(l);

        cbUsers.removeAllItems();
        if (usernames != null) {
            for (String user : usernames) {
                cbUsers.addItem(user);
            }
        }

        for (ActionListener l : listeners) cbUsers.addActionListener(l);
    }

    public void populateGames(List<String> gameNames) {
        ActionListener[] listeners = cbGames.getActionListeners();
        for (ActionListener l : listeners) cbGames.removeActionListener(l);

        cbGames.removeAllItems();
        if (gameNames != null) {
            for (String game : gameNames) {
                cbGames.addItem(game);
            }
        }

        for (ActionListener l : listeners) cbGames.addActionListener(l);
    }

    public String getSelectedUser() {
        return (String) cbUsers.getSelectedItem();
    }

    public String getSelectedGame() {
        return (String) cbGames.getSelectedItem();
    }

    public void setSelectedUser(String username) {
        cbUsers.setSelectedItem(username);
    }

    public void setSelectedGameIndex(int index) {
        if (index >= 0 && index < cbGames.getItemCount()) {
            ActionListener[] listeners = cbGames.getActionListeners();
            for (ActionListener l : listeners) cbGames.removeActionListener(l);

            cbGames.setSelectedIndex(index);

            for (ActionListener l : listeners) cbGames.addActionListener(l);
        }
    }

    /**
     * Carga los datos finales y la tabla de progresión
     *
     * @param stats Lista de estadísticas a mostrar en la tabla
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
        graphPanel.revalidate();
        graphPanel.repaint();
    }
}