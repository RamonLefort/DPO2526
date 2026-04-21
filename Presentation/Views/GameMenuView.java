package Presentation.Views;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import Bussiness.Entities.Game;
import Bussiness.Entities.Stat;
import java.util.List;

public class GameMenuView extends JPanel {
    public static final String BTN_BACK = "BACK";
    public static final String BTN_LOGOUT = "LOGOUT";
    public static final String BTN_NEW_GAME = "NEW_GAME";
    public static final String BTN_CONTINUE = "CONTINUE";
    public static final String BTN_STATS = "STATS";

    private final Color BG_COLOR = new Color(248, 245, 240);
    private final Color CARD_COLOR = Color.WHITE;
    private final Color PRIMARY_COFFEE = new Color(139, 69, 19);
    private JPanel currentGrid;
    private JPanel finishedGrid;

    private JButton btnNew;
    private JButton btnBack;
    private JButton btnLogout;
    private ActionListener actionListener;

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
     * Crea un panel que envuelve las cartas (wrapping) y las mantiene centradas.
     */
    private JPanel createResponsiveCenteredGrid() {
        JPanel grid = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        grid.setOpaque(false);
        grid.setAlignmentX(Component.CENTER_ALIGNMENT);
        grid.setMaximumSize(new Dimension(1100, 300));
        return grid;
    }

    private JPanel createTopNavigationHeader() {
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.X_AXIS));
        header.setOpaque(false);
        header.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        header.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Botón de back
        btnBack = new RoundedButton("< Atrás", 20, PRIMARY_COFFEE, BG_COLOR, BG_COLOR, PRIMARY_COFFEE);
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

        header.add(btnBack);
        header.add(Box.createHorizontalGlue());
        header.add(title);
        header.add(Box.createHorizontalGlue());
        header.add(btnLogout);

        return header;
    }

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

    private JPanel createGameCard(int radius, Color colorbg, String btnText, String name, String money, int minutes, int idGame, String actionCommand) {
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
        JPanel iconsRow = new JPanel();
        iconsRow.setLayout(new BoxLayout(iconsRow, BoxLayout.X_AXIS));
        iconsRow.setOpaque(false);
        iconsRow.setAlignmentX(Component.CENTER_ALIGNMENT);

        String[] iconData = {"👤 x2", "☕ x2", "\uD83C\uDF3F x2"};
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

    public void setActionListener(ActionListener listener) {
        this.actionListener = listener;
        btnBack.addActionListener(listener);
        btnLogout.addActionListener(listener);
        btnNew.addActionListener(listener);
    }

    public void clearCurrentGames() {
        currentGrid.removeAll();
    }

    public void addCurrentGameCard(String name, String money, int minutes, int idGame) {
        currentGrid.add(createGameCard(20, CARD_COLOR, "Continue →", name, money, minutes, idGame, BTN_CONTINUE));
    }

    public void refreshCurrentGames() {
        currentGrid.revalidate();
        currentGrid.repaint();
    }

    public void clearFinishedGames() {
        finishedGrid.removeAll();
    }

    public void addFinishedGameCard(String name, String money, int minutes, int idGame) {
        finishedGrid.add(createGameCard(20, CARD_COLOR, "See statistics →", name, money, minutes, idGame, BTN_STATS));
    }

    public void refreshFinishedGames() {
        finishedGrid.revalidate();
        finishedGrid.repaint();
    }
}