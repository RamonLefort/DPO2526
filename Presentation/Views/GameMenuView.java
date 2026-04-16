package Presentation.Views;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class GameMenuView extends JPanel {

    private final Color BG_COLOR = new Color(248, 245, 240);
    private final Color CARD_COLOR = Color.WHITE;
    private final Color PRIMARY_COFFEE = new Color(139, 69, 19);

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
        JPanel currentGrid = createResponsiveCenteredGrid(4, "Continue →");
        this.add(currentGrid);
        this.add(Box.createRigidArea(new Dimension(0, 20)));

        // Finished Games
        JPanel finishedHeader = createSectionHeader("Finished Games", null);
        finishedHeader.setAlignmentX(Component.CENTER_ALIGNMENT);
        finishedHeader.setMaximumSize(new Dimension(1000, 50));
        this.add(finishedHeader);
        JPanel finishedGrid = createResponsiveCenteredGrid(4, "See statistics →");
        this.add(finishedGrid);
        this.add(Box.createVerticalGlue());
    }

    /**
     * Crea un panel que envuelve las cartas (wrapping) y las mantiene centradas.
     */
    private JPanel createResponsiveCenteredGrid(int count, String btnText) {
        JPanel grid = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        grid.setOpaque(false);
        grid.setAlignmentX(Component.CENTER_ALIGNMENT);
        for (int i = 0; i < count; i++) {
            grid.add(createGameCard(20, CARD_COLOR, btnText));
        }
        grid.setMaximumSize(new Dimension(1100, grid.getPreferredSize().height));

        return grid;
    }

    private JPanel createTopNavigationHeader() {
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.X_AXIS));
        header.setOpaque(false);
        header.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        header.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Botón de back
        JButton btnBack = new RoundedButton("< Atrás", 20, PRIMARY_COFFEE, BG_COLOR, BG_COLOR, PRIMARY_COFFEE);
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
        JButton btnLogout = new RoundedButton("Logout", 20, PRIMARY_COFFEE, BG_COLOR, BG_COLOR, PRIMARY_COFFEE);
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
            JButton btnNew = new RoundedButton(btnText, 20, PRIMARY_COFFEE, BG_COLOR, BG_COLOR, PRIMARY_COFFEE);
            btnNew.setBorder(new RoundedBorder(PRIMARY_COFFEE, 20, 1));

            Dimension logoutDim = new Dimension(100, 40);
            btnNew.setPreferredSize(logoutDim);
            btnNew.setMaximumSize(logoutDim);
            btnNew.setMinimumSize(logoutDim);
            panel.add(btnNew);
        } else {
            panel.add(Box.createHorizontalGlue());
        }

        return panel;
    }

    private JPanel createGameGrid(int count, String btnText) {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));
        container.setOpaque(false);
        container.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));

        for (int i = 0; i < count; i++) {
            container.add(createGameCard(20, CARD_COLOR, btnText));
            container.add(Box.createRigidArea(new Dimension(15, 0)));
        }
        container.add(Box.createHorizontalGlue());

        return container;
    }

    // GameCard
    private JPanel createGameCard(int radius, Color colorbg, String btnText) {
        RoundedPanel card = new RoundedPanel(radius, colorbg);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(220, 200));
        card.setMaximumSize(new Dimension(220, 200));
        card.setBorder(BorderFactory.createCompoundBorder(new RoundedBorder(PRIMARY_COFFEE, radius, 1), new EmptyBorder(0, 15, 0, 15)));

        // Nombre de la partida
        JLabel title = new JLabel("My coffee empire");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(PRIMARY_COFFEE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(title);
        card.add(Box.createRigidArea(new Dimension(0, 10)));

        // Estadísticas
        String[] stats = {"Coffees: 2.4k", "Coffees/sec: 12", "Time spend: 25 mins"};
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

        Dimension btnDim = new Dimension(180, 30);
        btn.setPreferredSize(btnDim);
        btn.setMaximumSize(btnDim);
        btn.setMinimumSize(btnDim);

        card.add(btn);

        return card;
    }
}