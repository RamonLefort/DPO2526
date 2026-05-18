package Presentation.Views;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Vista que representa el menú principal de selección de partidas del usuario.
 * Hereda de {@link JPanel} y actúa como un panel de control donde se listan las partidas
 * actuales en curso y las partidas ya finalizadas. Proporciona acceso rápido a la
 * creación de nuevos juegos, ajustes del sistema y estadísticas históricas.
 */
public class GameMenuView extends JPanel {

    /** Comando de acción para cerrar la vista del menú y volver al login. */
    public static final String BTN_BACK = "BACK";
    /** Comando de acción para cerrar la sesión del juego y volver al login. */
    public static final String BTN_LOGOUT = "LOGOUT";
    /** Comando de acción para crear un nuevo juego. */
    public static final String BTN_NEW_GAME = "NEW_GAME";
    /** Comando de acción para continuar un juego ya creado. */
    public static final String BTN_CONTINUE = "CONTINUE";
    /** Comando de acción para visualizar las estadísticas de una partida ya finalizada. */
    public static final String BTN_STATS = "STATS";

    private final Color BG_COLOR = new Color(248, 245, 240);
    private final Color CARD_COLOR = Color.WHITE;
    private final Color PRIMARY_COFFEE = new Color(139, 69, 19);
    private JPanel currentGrid;
    private JPanel finishedGrid;
    private JPanel iconsRow;

    private JButton btnNew;
    private JButton btnBack;
    private JButton btnLogout;
    private ActionListener actionListener;

    /**
     * Constructor de la vista. Configura el layout principal de tipo {@link BoxLayout}
     * en el eje vertical y organiza las tres secciones principales: navegación superior,
     * rejilla de partidas actuales y rejilla de partidas finalizadas.
     */
    public GameMenuView() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBackground(BG_COLOR);
        this.setBorder(new EmptyBorder(20, 40, 20, 40));

        // Nav
        JPanel header = createTopNavigationHeader();
        header.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.add(header);
        this.add(Box.createRigidArea(new Dimension(0, 40)));

        // Current Games
        JPanel currentHeader = createSectionHeader("Current Games", "+ New Game");
        currentHeader.setAlignmentX(Component.CENTER_ALIGNMENT);
        currentHeader.setMaximumSize(new Dimension(1000, 50));
        this.add(currentHeader);
        currentGrid = createResponsiveCenteredGrid();
        this.add(currentGrid);
        this.add(Box.createRigidArea(new Dimension(0, 20)));

        // Finished Games
        JPanel finishedHeader = createSectionHeader("Finished Games", null);
        finishedHeader.setAlignmentX(Component.CENTER_ALIGNMENT);
        finishedHeader.setMaximumSize(new Dimension(1000, 50));
        this.add(finishedHeader);
        finishedGrid = createResponsiveCenteredGrid();
        this.add(finishedGrid);
        this.add(Box.createVerticalGlue());
    }

    /**
     * Crea un panel contenedor con {@link FlowLayout} centrado.
     * Este componente permite que las tarjetas de juego se organicen de forma fluida
     * según el ancho disponible en la ventana.
     *
     * @return Un panel configurado para albergar rejillas de tarjetas.
     */
    private JPanel createResponsiveCenteredGrid() {
        JPanel grid = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        grid.setOpaque(false);
        grid.setAlignmentX(Component.CENTER_ALIGNMENT);
        grid.setMaximumSize(new Dimension(1100, 300));
        return grid;
    }

    /**
     * Crea el panel de navegación situado en el header de la vista
     *
     * @return Un panel de navegación.
     */
    private JPanel createTopNavigationHeader() {
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.X_AXIS));
        header.setOpaque(false);
        header.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        header.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Botón de ajustes
        btnBack = new RoundedButton("Ajustes", 20, PRIMARY_COFFEE, BG_COLOR, BG_COLOR, PRIMARY_COFFEE);
        btnBack.setActionCommand(BTN_BACK);
        btnBack.setBorder(new RoundedBorder(PRIMARY_COFFEE, 20, 1));
        Dimension backDim = new Dimension(85, 35);
        btnBack.setPreferredSize(backDim);
        btnBack.setMinimumSize(backDim);
        btnBack.setMaximumSize(backDim);

        // Título
        JLabel title = new JLabel("Coffee Clicker");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(PRIMARY_COFFEE);

        // Botón de logout
        btnLogout = new RoundedButton("Logout", 20, PRIMARY_COFFEE, BG_COLOR, BG_COLOR, PRIMARY_COFFEE);
        btnLogout.setActionCommand(BTN_LOGOUT);
        btnLogout.setBorder(new RoundedBorder(PRIMARY_COFFEE, 20, 1));
        Dimension logoutDim = new Dimension(75, 35);
        btnLogout.setPreferredSize(logoutDim);
        btnLogout.setMaximumSize(logoutDim);
        btnLogout.setMinimumSize(logoutDim);

        header.add(btnLogout);
        header.add(Box.createHorizontalGlue());
        header.add(title);
        header.add(Box.createHorizontalGlue());
        header.add(btnBack);

        return header;
    }

    /**
     * Crea el panel del título con un botón
     *
     * @return Un panel de título con un texto.
     */
    private JPanel createSectionHeader(String titleText, String btnText) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JLabel lblTitle = new JLabel(titleText);
        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        panel.add(lblTitle);

        if (btnText != null) {
            panel.add(Box.createHorizontalGlue());
            btnNew = new RoundedButton(btnText, 20, PRIMARY_COFFEE, BG_COLOR, BG_COLOR, PRIMARY_COFFEE);
            btnNew.setActionCommand(BTN_NEW_GAME);
            btnNew.setBorder(new RoundedBorder(PRIMARY_COFFEE, 20, 1));
            Dimension d = new Dimension(100, 40);
            btnNew.setPreferredSize(d);
            btnNew.setMaximumSize(d);
            btnNew.setMinimumSize(d);
            panel.add(btnNew);
        } else {
            panel.add(Box.createHorizontalGlue());
        }

        return panel;
    }

    /**
     * Genera una tarjeta visual para representar una partida individual.
     * La tarjeta incluye el nombre de la partida, el total de café recolectado,
     * el tiempo de juego y un resumen de la infraestructura (Baristas, Máquinas y Plantaciones).
     *
     * @param radius         Radio de redondeo de la tarjeta.
     * @param colorbg        Color de fondo de la tarjeta.
     * @param btnText        Texto a mostrar en el botón de acción (Continuar o Estadísticas).
     * @param name           Nombre de la partida.
     * @param money          Cantidad de café recolectado.
     * @param minutes        Minutos totales de juego.
     * @param idGame         Identificador único de la partida.
     * @param actionCommand  Comando base para el botón de acción.
     * @param baristas       Cantidad de baristas contratados.
     * @param machines       Cantidad de máquinas de espresso adquiridas.
     * @param plantations    Cantidad de plantaciones en propiedad.
     * @return Un {@link RoundedPanel} que representa visualmente la partida.
     */
    private JPanel createGameCard(int radius, Color colorbg, String btnText, String name, String money, int minutes, int idGame, String actionCommand, int baristas, int machines, int plantations) {
        RoundedPanel card = new RoundedPanel(radius, colorbg);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(220, 200));
        card.setMaximumSize(new Dimension(220, 200));
        card.setBorder(BorderFactory.createCompoundBorder(new RoundedBorder(PRIMARY_COFFEE, radius, 1), new EmptyBorder(0, 15, 0, 15)));

        // Nombre de la partida
        JLabel title = new JLabel(name);
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(PRIMARY_COFFEE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(title);
        card.add(Box.createRigidArea(new Dimension(0, 10)));

        // Estadísticas
        String[] stats = {"Coffees: " + money, "Time: " + minutes + " mins"};
        for (String s : stats) {
            JLabel lbl = new JLabel(s);
            lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            lbl.setForeground(PRIMARY_COFFEE);
            lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
            card.add(lbl);
            card.add(Box.createRigidArea(new Dimension(0, 5)));
        }

        card.add(Box.createRigidArea(new Dimension(0, 10)));

        // Generadores
        iconsRow = new JPanel();
        iconsRow.setLayout(new BoxLayout(iconsRow, BoxLayout.X_AXIS));
        iconsRow.setOpaque(false);
        iconsRow.setAlignmentX(Component.CENTER_ALIGNMENT);

        String[] iconData = {"👤 x" + baristas, "☕ x" + machines, "\uD83C\uDF3F x" + plantations};
        for (int i = 0; i < iconData.length; i++) {
            JLabel iconLbl = new JLabel(iconData[i]);
            iconLbl.setForeground(PRIMARY_COFFEE);
            iconLbl.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 14));
            iconsRow.add(iconLbl);
            if (i < iconData.length - 1) {
                iconsRow.add(Box.createRigidArea(new Dimension(10, 0)));
            }
        }

        card.add(iconsRow);
        card.add(Box.createVerticalGlue());
        card.add(Box.createRigidArea(new Dimension(0, 10)));

        // Botón
        JButton btn = new RoundedButton(btnText, 20, PRIMARY_COFFEE, BG_COLOR, BG_COLOR, PRIMARY_COFFEE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBorder(new RoundedBorder(PRIMARY_COFFEE, 20, 1));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setActionCommand(actionCommand + idGame);
        btn.addActionListener(actionListener);

        Dimension btnDim = new Dimension(180, 30);
        btn.setPreferredSize(btnDim);
        btn.setMaximumSize(btnDim);
        btn.setMinimumSize(btnDim);

        card.add(btn);

        return card;
    }

    /**
     * Vincula el controlador de eventos a los botones de navegación estáticos.
     *
     * @param listener El {@link ActionListener} que procesará las interacciones.
     */
    public void setActionListener(ActionListener listener) {
        this.actionListener = listener;
        btnBack.addActionListener(listener);
        btnLogout.addActionListener(listener);
        btnNew.addActionListener(listener);
    }

    /**
     * Limpia todos los componentes de la rejilla de partidas en curso.
     */
    public void clearCurrentGames() {
        currentGrid.removeAll();
    }

    /**
     * Añade una tarjeta de partida a la sección de juegos en curso.
     *
     * @param name         Nombre de la partida.
     * @param money        Saldo de café.
     * @param minutes      Tiempo transcurrido.
     * @param idGame       ID de partida.
     * @param baristas     Cantidad de baristas.
     * @param machines     Cantidad de máquinas.
     * @param plantations  Cantidad de plantaciones.
     */
    public void addCurrentGameCard(String name, String money, int minutes, int idGame, int baristas, int machines, int plantations) {
        currentGrid.add(createGameCard(20, CARD_COLOR, "Continue →", name, money, minutes, idGame, BTN_CONTINUE, baristas, machines, plantations));
    }

    /**
     * Fuerza la actualización visual de la rejilla de partidas actuales.
     */
    public void refreshCurrentGames() {
        currentGrid.revalidate();
        currentGrid.repaint();
    }

    /**
     * Limpia todos los componentes de la rejilla de partidas finalizadas.
     */
    public void clearFinishedGames() {
        finishedGrid.removeAll();
    }

    /**
     * Añade una tarjeta de partida a la sección de juegos finalizados.
     *
     * @param name         Nombre de la partida.
     * @param money        Saldo de café.
     * @param minutes      Tiempo transcurrido.
     * @param idGame       ID de partida.
     * @param baristas     Cantidad de baristas.
     * @param machines     Cantidad de máquinas.
     * @param plantations  Cantidad de plantaciones.
     */
    public void addFinishedGameCard(String name, String money, int minutes, int idGame, int baristas, int machines, int plantations) {
        finishedGrid.add(createGameCard(20, CARD_COLOR, "See statistics →", name, money, minutes, idGame, BTN_STATS, baristas, machines, plantations));
    }

    /**
     * Fuerza la actualización visual de la rejilla de partidas finalizadas.
     */
    public void refreshFinishedGames() {
        finishedGrid.revalidate();
        finishedGrid.repaint();
    }
}