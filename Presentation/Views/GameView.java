package Presentation.Views;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class GameView extends JPanel {

    private final Color BG_COLOR = new Color(248, 245, 240);
    private final Color PRIMARY_COFFEE = new Color(74, 44, 23);

    public GameView() {
        this.setLayout(new BorderLayout());
        this.setBackground(BG_COLOR);

        // Header
        this.add(createTopHeader(), BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.X_AXIS));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(new EmptyBorder(20, 40, 20, 40));

        // Columna botón café
        centerPanel.add(createLeftColumn());
        centerPanel.add(Box.createRigidArea(new Dimension(40, 0))); // Espacio entre columnas

        // Columna tienda
        centerPanel.add(createRightColumn());

        this.add(centerPanel, BorderLayout.CENTER);
    }

    private JPanel createTopHeader() {
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.X_AXIS));
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(15, 20, 15, 20));
        header.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        // Botón Atrás
        JButton btnBack = new RoundedButton("< Atrás", 20, PRIMARY_COFFEE, BG_COLOR, BG_COLOR, PRIMARY_COFFEE);
        btnBack.setBorder(new RoundedBorder(PRIMARY_COFFEE, 20, 1));

        Dimension dim = new Dimension(80, 35);
        btnBack.setPreferredSize(dim);
        btnBack.setMaximumSize(dim);
        btnBack.setMinimumSize(dim);

        // Título del Juego
        JLabel title = new JLabel("Game Title");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(PRIMARY_COFFEE);

        // Botón Finish Game
        JButton btnFinish = new RoundedButton("Finish Game", 20, PRIMARY_COFFEE, BG_COLOR, BG_COLOR, PRIMARY_COFFEE);
        btnFinish.setBorder(new RoundedBorder(PRIMARY_COFFEE, 20, 1));

        Dimension dimFinish = new Dimension(100, 35);
        btnFinish.setPreferredSize(dimFinish);
        btnFinish.setMaximumSize(dimFinish);
        btnFinish.setMinimumSize(dimFinish);

        header.add(btnBack);
        header.add(Box.createRigidArea(new Dimension(20, 0)));
        header.add(title);
        header.add(Box.createHorizontalGlue());
        header.add(btnFinish);
        return header;
    }

    private JPanel createLeftColumn() {
        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setOpaque(false);

        // JPanel de info
        RoundedPanel scorePanel = new RoundedPanel(20, BG_COLOR);
        scorePanel.setLayout(new GridBagLayout());
        scorePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

        JPanel labelsContainer = new JPanel();
        labelsContainer.setLayout(new BoxLayout(labelsContainer, BoxLayout.Y_AXIS));
        labelsContainer.setOpaque(false);

        //Número de cafes
        JLabel countNum = new JLabel("42");
        countNum.setFont(new Font("Segoe UI", Font.BOLD, 40));
        countNum.setForeground(Color.BLACK);
        countNum.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel countText = new JLabel("coffees");
        countText.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        countText.setForeground(Color.DARK_GRAY);
        countText.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Cafés por segundo
        JLabel rateText = new JLabel("+1.0 per second");
        rateText.setFont(new Font("Segoe UI", Font.BOLD, 14));
        rateText.setForeground(PRIMARY_COFFEE);
        rateText.setAlignmentX(Component.CENTER_ALIGNMENT);

        labelsContainer.add(countNum);
        labelsContainer.add(countText);
        labelsContainer.add(Box.createRigidArea(new Dimension(0, 10)));
        labelsContainer.add(rateText);
        labelsContainer.add(Box.createRigidArea(new Dimension(0, 10)));

        scorePanel.add(labelsContainer);

        // Botón café
        JButton coffeeBtn = new RoundedButton("☕", 100, PRIMARY_COFFEE, BG_COLOR, BG_COLOR, PRIMARY_COFFEE);
        coffeeBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        coffeeBtn.setBorder(new RoundedBorder(PRIMARY_COFFEE, 100, 1));
        Dimension coffeeDim = new Dimension(150, 150);
        coffeeBtn.setPreferredSize(coffeeDim);
        coffeeBtn.setMaximumSize(coffeeDim);
        coffeeBtn.setMinimumSize(coffeeDim);

        // JTable de los generadores
        JPanel tablePanel = createGenerationsTable();
        tablePanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        left.add(scorePanel);
        left.add(Box.createRigidArea(new Dimension(0, 30)));
        left.add(coffeeBtn);
        left.add(Box.createRigidArea(new Dimension(0, 30)));
        left.add(tablePanel);

        return left;
    }

    private JPanel createRightColumn() {
        JPanel right = new JPanel();
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        right.setOpaque(false);
        right.setMaximumSize(new Dimension(300, Integer.MAX_VALUE));
        right.setPreferredSize(new Dimension(300, Integer.MAX_VALUE));

        // Botón cambio de tienda
        JPanel selector = new JPanel(new GridLayout(1, 2, 5, 0));
        selector.setOpaque(false);
        selector.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        RoundedButton generatorsBtn = new RoundedButton("Generators", 20, PRIMARY_COFFEE, BG_COLOR, BG_COLOR, PRIMARY_COFFEE);
        generatorsBtn.setBorder(new RoundedBorder(PRIMARY_COFFEE, 20, 1));
        selector.add(generatorsBtn);
        RoundedButton upgradesBtn = new RoundedButton("Upgrades", 20, Color.WHITE, Color.LIGHT_GRAY, Color.LIGHT_GRAY, Color.WHITE);
        upgradesBtn.setBorder(new RoundedBorder(Color.LIGHT_GRAY, 20, 1));
        selector.add(upgradesBtn);

        right.add(selector);
        right.add(Box.createRigidArea(new Dimension(0, 20)));

        // Lista de la tienda
        right.add(createStoreItem("Barista", "A skilled barista who makes espresso shots", "0.20/s", "14 coffees"));
        right.add(Box.createRigidArea(new Dimension(0, 15)));
        right.add(createStoreItem("Espresso Machine", "Automatic espresso machine for quick brews", "0.71/s", "150 coffees"));
        right.add(Box.createRigidArea(new Dimension(0, 15)));
        right.add(createStoreItem("Coffee Plantation", "Your own coffee bean plantation", "0.23/s", "2.00K coffees"));

        right.add(Box.createVerticalGlue());
        return right;
    }

    private JPanel createStoreItem(String title, String desc, String rate, String price) {
        RoundedPanel item = new RoundedPanel(15, Color.WHITE);
        item.setLayout(new BorderLayout(15, 5)); // Aumentamos el gap vertical a 5
        item.setBorder(new EmptyBorder(15, 15, 15, 15));
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 140)); // Aumentamos un poco el alto máximo

        // Foto tienda
        RoundedPanel icon = new RoundedPanel(10, BG_COLOR);
        icon.setPreferredSize(new Dimension(50, 50));
        icon.setMinimumSize(new Dimension(50, 50));

        // JPanel Texto
        JPanel textContainer = new JPanel();
        textContainer.setLayout(new BoxLayout(textContainer, BoxLayout.Y_AXIS));
        textContainer.setOpaque(false);

        // Título
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTitle.setForeground(PRIMARY_COFFEE);

        // Descripción
        JTextArea areaDesc = new JTextArea(desc);
        areaDesc.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        areaDesc.setForeground(Color.GRAY);
        areaDesc.setLineWrap(true);
        areaDesc.setWrapStyleWord(true);
        areaDesc.setEditable(false);
        areaDesc.setFocusable(false);
        areaDesc.setOpaque(false);
        areaDesc.setBackground(new Color(0, 0, 0, 0));
        areaDesc.setAlignmentX(Component.LEFT_ALIGNMENT);
        areaDesc.setMaximumSize(new Dimension(300, 40));
        areaDesc.setPreferredSize(new Dimension(300, 40));

        // Cafes/seg
        JLabel lblRate = new JLabel("+" + rate + " per unit");
        lblRate.setForeground(new Color(210, 105, 30));
        lblRate.setFont(new Font("Segoe UI", Font.BOLD, 11));

        textContainer.add(lblTitle);
        textContainer.add(Box.createRigidArea(new Dimension(0, 2)));
        textContainer.add(areaDesc);
        textContainer.add(Box.createRigidArea(new Dimension(0, 2)));
        textContainer.add(lblRate);

        // Botón
        RoundedButton btnBuy = new RoundedButton("Buy for " + price, 20, PRIMARY_COFFEE, Color.WHITE, Color.WHITE, PRIMARY_COFFEE);
        btnBuy.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnBuy.setPreferredSize(new Dimension(Integer.MAX_VALUE, 35));
        btnBuy.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBuy.setBorder(new RoundedBorder(PRIMARY_COFFEE, 20, 1));

        item.add(icon, BorderLayout.WEST);
        item.add(textContainer, BorderLayout.CENTER);
        item.add(btnBuy, BorderLayout.SOUTH);

        return item;
    }

    private JPanel createGenerationsTable() {
        RoundedPanel p = new RoundedPanel(20, Color.WHITE);
        p.setLayout(new BorderLayout());
        p.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Your Generations");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        p.add(title, BorderLayout.NORTH);

        String[] cols = {"Name", "Qty", "Rate", "Total", "%"};
        Object[][] data = {{"John", "5", "0.20/s", "1.0/s", "100.0%"}};

        JTable table = new JTable(data, cols);
        table.setRowHeight(35);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));

        table.getTableHeader().setBackground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(Color.WHITE);
        p.add(scroll, BorderLayout.CENTER);

        return p;
    }
}