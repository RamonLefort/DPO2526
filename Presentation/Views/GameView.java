package Presentation.Views;

import Bussiness.Entities.Generator;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Vista principal del entorno de juego.
 * Esta clase extiende de {@link JPanel} y representa el centro de interacción del usuario durante la partida.
 * Organiza visualmente el contador de recursos, el área de clic manual, la tabla de producción
 * y la tienda dividida en pestañas (Generadores y Mejoras) mediante un {@link CardLayout}.
 */
public class GameView extends JPanel {

    /** Comando de acción para cerrar la vista del juego y volver al menú. */
    public static final String BTN_BACK   = "BTN_BACK";
    /** Comando de acción para finalizar una partida y volver al menú. */
    public static final String BTN_FINISH = "BTN_FINISH";
    /** Comando de acción para realizar un café. */
    public static final String BTN_COFFEE = "BTN_COFFEE";
    /** Comando de acción para comprar un Barista. */
    public static final String BTN_BARISTA = "BTN_BARISTA";
    /** Comando de acción para comprar una Maquina. */
    public static final String BTN_MACHINE = "BTN_MACHINE";
    /** Comando de acción para comprar una Plantación. */
    public static final String BTN_PLANTATION = "BTN_PLANTATION";
    /** Comando de acción para subir el nivel de los Baristas. */
    public static final String BTN_UP_BARISTA = "BTN_UP_BARISTA";
    /** Comando de acción para subir el nivel de las Maquinas. */
    public static final String BTN_UP_MACHINE = "BTN_UP_MACHINE";
    /** Comando de acción para subir el nivel de las Plantaciones. */
    public static final String BTN_UP_PLANTATION = "BTN_UP_PLANTATION";
    /** Comando de acción para cambiar al menú de 'Generadores'. */
    public static final String BTN_GENERATORS = "BTN_GENERATORS";
    /** Comando de acción para cambiar al menú de 'Upgrades'. */
    public static final String BTN_UPGRADES = "BTN_UPGRADES";

    private JButton btnBack;
    private JButton btnFinish;
    private JButton coffeeBtn;
    private JLabel countNum;
    private JLabel gameName;
    private JLabel gameProduction;
    private JButton btnBarista;
    private JButton btnMachine;
    private JButton btnCoffee;
    private JButton btnUpBarista;
    private JButton btnUpMachine;
    private JButton btnUpPlantation;
    private DefaultTableModel tableModel;
    private RoundedButton navGeneratorsBtn;
    private RoundedButton navUpgradesBtn;
    private CardLayout cardLayout;
    private JPanel cardsContainer;
    private JPanel centerPanel;

    private final Color BG_COLOR = new Color(248, 245, 240);
    private final Color PRIMARY_COFFEE = new Color(74, 44, 23);

    /**
     * Constructor de la vista. Inicializa la estructura principal de la interfaz utilizando
     * un {@link BorderLayout}. Divide la pantalla en una cabecera superior y un panel central
     * compuesto por una columna de estadísticas fija y un panel lateral intercambiable para la tienda.
     */
    public GameView() {
        this.setLayout(new BorderLayout());
        this.setBackground(BG_COLOR);
        this.add(createTopHeader(), BorderLayout.NORTH);

        centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.X_AXIS));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(new EmptyBorder(20, 40, 20, 40));

        // Columna Izquierda Fija
        centerPanel.add(createLeftColumn());
        centerPanel.add(Box.createRigidArea(new Dimension(40, 0)));

        // Columna Derecha con CardLayout
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setOpaque(false);
        rightPanel.setMaximumSize(new Dimension(300, Integer.MAX_VALUE));

        // 1. SELECTOR ÚNICO (Solo se crea una vez aquí)
        rightPanel.add(createSelectorPanel());
        rightPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // 2. CONTENEDOR DE TARJETAS
        cardLayout = new CardLayout();
        cardsContainer = new JPanel(cardLayout);
        cardsContainer.setOpaque(false);

        cardsContainer.add(createGeneratorsContent(), "GENERATORS");
        cardsContainer.add(createUpgradesContent(), "UPGRADES");

        rightPanel.add(cardsContainer);
        centerPanel.add(rightPanel);

        this.add(centerPanel, BorderLayout.CENTER);
    }

    /**
     * Crea el panel superior de navegación.
     * Contiene el botón para regresar al menú, el título dinámico de la partida actual
     * y el botón para finalizar el juego.
     *
     * @return Un {@link JPanel} configurado como cabecera.
     */
    private JPanel createTopHeader() {
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.X_AXIS));
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(15, 20, 15, 20));
        header.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        // Botón Atrás
        btnBack = new RoundedButton("< Atrás", 20, PRIMARY_COFFEE, BG_COLOR, BG_COLOR, PRIMARY_COFFEE);
        btnBack.setActionCommand(BTN_BACK);
        btnBack.setBorder(new RoundedBorder(PRIMARY_COFFEE, 20, 1));

        Dimension dim = new Dimension(80, 35);
        btnBack.setPreferredSize(dim);
        btnBack.setMaximumSize(dim);
        btnBack.setMinimumSize(dim);

        // Título del Juego
        gameName = new JLabel("Game Title");
        gameName.setFont(new Font("Segoe UI", Font.BOLD, 24));
        gameName.setForeground(PRIMARY_COFFEE);

        // Botón Finish Game
        btnFinish = new RoundedButton("Finish Game", 20, PRIMARY_COFFEE, BG_COLOR, BG_COLOR, PRIMARY_COFFEE);
        btnFinish.setActionCommand(BTN_FINISH);
        btnFinish.setBorder(new RoundedBorder(PRIMARY_COFFEE, 20, 1));

        Dimension dimFinish = new Dimension(100, 35);
        btnFinish.setPreferredSize(dimFinish);
        btnFinish.setMaximumSize(dimFinish);
        btnFinish.setMinimumSize(dimFinish);

        header.add(btnBack);
        header.add(Box.createRigidArea(new Dimension(20, 0)));
        header.add(gameName);
        header.add(Box.createHorizontalGlue());
        header.add(btnFinish);
        return header;
    }

    /**
     * Crea la columna izquierda de la interfaz.
     * Centraliza los elementos de estado: el contador principal de cafés, la tasa de producción global,
     * el botón interactivo de generación manual y la tabla de desglose de generadores.
     *
     * @return Un {@link JPanel} con la información de estado y acción manual.
     */
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
        countNum = new JLabel("0");
        countNum.setFont(new Font("Segoe UI", Font.BOLD, 40));
        countNum.setForeground(Color.BLACK);
        countNum.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel countText = new JLabel("coffees");
        countText.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        countText.setForeground(Color.DARK_GRAY);
        countText.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Cafés por segundo
        gameProduction = new JLabel("+1.0 per second");
        gameProduction.setFont(new Font("Segoe UI", Font.BOLD, 14));
        gameProduction.setForeground(PRIMARY_COFFEE);
        gameProduction.setAlignmentX(Component.CENTER_ALIGNMENT);

        labelsContainer.add(countNum);
        labelsContainer.add(countText);
        labelsContainer.add(Box.createRigidArea(new Dimension(0, 10)));
        labelsContainer.add(gameProduction);
        labelsContainer.add(Box.createRigidArea(new Dimension(0, 10)));

        scorePanel.add(labelsContainer);

        // Botón café
        coffeeBtn = new RoundedButton("☕", 100, PRIMARY_COFFEE, BG_COLOR, BG_COLOR, PRIMARY_COFFEE);
        coffeeBtn.setActionCommand(BTN_COFFEE);
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

    /**
     * Genera el panel de selección de categorías para la tienda lateral.
     * Permite al usuario alternar entre la vista de "Generadores" y "Mejoras" actualizando
     * visualmente el estado de los botones.
     *
     * @return Un {@link JPanel} con los botones de navegación de la tienda.
     */
    private JPanel createSelectorPanel() {
        JPanel selector = new JPanel(new GridLayout(1, 2, 5, 0));
        selector.setOpaque(false);
        selector.setMaximumSize(new Dimension(350, 40));
        selector.setPreferredSize(new Dimension(350, 40));

        navGeneratorsBtn = new RoundedButton("Generators", 20, PRIMARY_COFFEE, BG_COLOR, BG_COLOR, PRIMARY_COFFEE);
        navGeneratorsBtn.setBorder(new RoundedBorder(PRIMARY_COFFEE, 20, 1));
        navGeneratorsBtn.setActionCommand(BTN_GENERATORS);

        navUpgradesBtn = new RoundedButton("Upgrades", 20, Color.WHITE, Color.LIGHT_GRAY, Color.LIGHT_GRAY, Color.WHITE);
        navUpgradesBtn.setBorder(new RoundedBorder(Color.LIGHT_GRAY, 20, 1));
        navUpgradesBtn.setActionCommand(BTN_UPGRADES);

        selector.add(navGeneratorsBtn);
        selector.add(navUpgradesBtn);
        return selector;
    }

    /**
     * Inicializa el contenido de la pestaña de generadores.
     * Agrega los ítems de compra base (Barista, Máquina, Plantación) con sus descripciones
     * y tasas de producción iniciales.
     *
     * @return Un {@link JPanel} con el listado de generadores disponibles para compra.
     */
    private JPanel createGeneratorsContent() {
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);

        // Al crear el item, asignamos el botón a la variable de clase específica
        content.add(createStoreItem("The Sleepy Intern Barista", "He’s only here for the college credits and free caffeine. Occasionally puts milk in the espresso.", "0.20/s", "15", BTN_BARISTA, 1));
        content.add(Box.createRigidArea(new Dimension(0, 15)));
        content.add(createStoreItem("The Steam-Punk 3000 Machine", "A rusty contraption that hisses like a dragon. Makes great coffee, but might explode at any moment.", "0.67/s", "150", BTN_MACHINE, 1));
        content.add(Box.createRigidArea(new Dimension(0, 15)));
        content.add(createStoreItem("Java Jungle Coffee Plantation", "An entire ecosystem dedicated to the bean. The birds here don't chirp, they just vibrate.", "1/s", "2K", BTN_PLANTATION, 1));

        content.add(Box.createVerticalGlue());
        return content;
    }

    /**
     * Inicializa el contenido de la pestaña de mejoras (Upgrades).
     * Agrega las opciones de mejora tecnológica que potencian la eficiencia de los generadores.
     *
     * @return Un {@link JPanel} con el listado de mejoras disponibles.
     */
    private JPanel createUpgradesContent() {
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);

        content.add(createStoreItem("Liquid Courage", "We replaced the intern's water with pure ristretto. He hasn't blinked in three days, but production is doubled!", "x2", "15k", BTN_UP_BARISTA, 2));
        content.add(Box.createRigidArea(new Dimension(0, 15)));
        content.add(createStoreItem("Nuclear Boiler", "By overclocking the steam valves, we’ve reached 'Mach-Coffee' speeds. Use of safety goggles is highly advised.", "x2", "150k", BTN_UP_MACHINE, 2));
        content.add(Box.createRigidArea(new Dimension(0, 15)));
        content.add(createStoreItem("Glow-in-the-Dark Beans", "Genetically modified seeds that grow via sheer willpower and radioactive fertilizer. Twice the beans, half the DNA integrity.", "x2", "200k", BTN_UP_PLANTATION, 2));

        content.add(Box.createVerticalGlue());
        return content;
    }

    /**
     * Crea los componentes de ítem en la tienda de forma genérica.
     * Configura el diseño, texto, iconos y vincula los botones a las variables de clase
     * correspondientes según su tipo.
     *
     * @param title Título del ítem.
     * @param desc Descripción narrativa del ítem.
     * @param rate Tasa de beneficio (ej: "0.20/s" o "x2").
     * @param price Precio inicial mostrado en el botón.
     * @param ActionCommand Comando de acción para el controlador.
     * @param tab Identificador de pestaña (1 para Generadores, 2 para Upgrades).
     * @return Un {@link JPanel} estilizado representando un artículo de la tienda.
     */
    private JPanel createStoreItem(String title, String desc, String rate, String price, String ActionCommand, int tab) {
        RoundedPanel item = new RoundedPanel(15, Color.WHITE);
        item.setLayout(new BorderLayout(15, 5));
        item.setBorder(new EmptyBorder(15, 15, 15, 15));
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 140));

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
        areaDesc.setMaximumSize(new Dimension(300, 45));
        areaDesc.setPreferredSize(new Dimension(300, 45));

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
        btnBuy.setActionCommand(ActionCommand);

        if (tab == 1) { // Generadores
            if (ActionCommand.equals(BTN_BARISTA)) {
                this.btnBarista = btnBuy;
            } else if (ActionCommand.equals(BTN_MACHINE)) {
                this.btnMachine = btnBuy;
            } else if (ActionCommand.equals(BTN_PLANTATION)) {
                this.btnCoffee = btnBuy;
            }
        } else { // Upgrades
            if (ActionCommand.equals(BTN_UP_BARISTA)) {
                this.btnUpBarista = btnBuy;
            } else if (ActionCommand.equals(BTN_UP_MACHINE)) {
                this.btnUpMachine = btnBuy;
            } else if (ActionCommand.equals(BTN_UP_PLANTATION)) {
                this.btnUpPlantation = btnBuy;
            }
        }
        btnBuy.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnBuy.setPreferredSize(new Dimension(Integer.MAX_VALUE, 35));
        btnBuy.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBuy.setBorder(new RoundedBorder(PRIMARY_COFFEE, 20, 1));

        item.add(icon, BorderLayout.WEST);
        item.add(textContainer, BorderLayout.CENTER);
        item.add(btnBuy, BorderLayout.SOUTH);

        return item;
    }

    /**
     * Crea el panel que contiene la tabla de desglose de producción.
     * Define el modelo de tabla no editable y personaliza el estilo visual de la cabecera y celdas.
     *
     * @return Un {@link JPanel} que contiene un {@link JScrollPane} con la tabla de generaciones.
     */
    private JPanel createGenerationsTable() {
        RoundedPanel p = new RoundedPanel(20, Color.WHITE);
        p.setLayout(new BorderLayout());
        p.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Your Generations");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        p.add(title, BorderLayout.NORTH);

        String[] cols = {"Name", "Qty", "Rate", "Total", "%"};

        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(tableModel);
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

    /**
     * Vincula un escuchador de eventos a todos los botones interactivos de la vista.
     * Esto incluye botones de navegación, el botón de clic manual y todos los botones de la tienda.
     *
     * @param listener El {@link ActionListener} que procesará las acciones.
     */
    public void setActionListener(ActionListener listener) {
        btnBack.addActionListener(listener);
        btnFinish.addActionListener(listener);
        coffeeBtn.addActionListener(listener);
        navGeneratorsBtn.addActionListener(listener);
        navUpgradesBtn.addActionListener(listener);
        // Tienda Generators
        btnBarista.addActionListener(listener);
        btnMachine.addActionListener(listener);
        btnCoffee.addActionListener(listener);
        // Tienda Upgrades
        btnUpBarista.addActionListener(listener);
        btnUpMachine.addActionListener(listener);
        btnUpPlantation.addActionListener(listener);
    }

    /**
     * Actualiza el texto del contador principal de cafés en la UI.
     *
     * @param count Cantidad actual de cafés.
     */
    public void updateCoffeeCount(int count) {
        countNum.setText(String.valueOf(count));
    }

    /**
     * Actualiza el texto del botón de compra del Barista con su precio actual.
     *
     * @param price Precio actualizado en cafés.
     */
    public void updateBaristaPrice(int price){
        btnBarista.setText("Buy for " + price + " coffees");
    }

    /**
     * Actualiza el texto del botón de compra de la Máquina con su precio actual.
     *
     * @param price Precio actualizado en cafés.
     */
    public void updateMachinePrice(int price){
        btnMachine.setText("Buy for " + price + " coffees");
    }

    /**
     * Actualiza el texto del botón de compra de la Plantación con su precio actual.
     *
     * @param price Precio actualizado en cafés.
     */
    public void updatePlantationPrice(int price){
        btnCoffee.setText("Buy for " + price + " coffees");
    }

    /**
     * Actualiza el título mostrado en la cabecera del juego.
     *
     * @param name Nombre de la partida.
     */
    public void updateGameName(String name){
        gameName.setText(name);
    }

    /**
     * Actualiza la etiqueta que muestra la producción pasiva global por segundo.
     *
     * @param production Valor flotante de la producción actual por segundo.
     */
    public void updateProductionXSec(float production){
        gameProduction.setText("+ " + String.format("%.2f/s", production) + " per second");
    }

    /**
     * Refresca los datos mostrados en la tabla de generaciones.
     * Calcula dinámicamente el porcentaje de contribución de cada generador sobre el total
     * y actualiza las filas con las cantidades y tasas de producción actuales.
     *
     * @param generators Lista de objetos {@link Generator} con los datos actualizados.
     */
    public void updateGenerationsData(List<Generator> generators) {
        tableModel.setRowCount(0);

        double globalTotal = 0;
        for (Generator g : generators) {
            globalTotal += g.getEarning() * g.getQuantity();
        }

        for (Generator g : generators) {
            double rowTotal = (double) g.getEarning() / (g.getPeriod() / 1000.0);
            double percentage = (globalTotal > 0) ? ((g.getEarning() * g.getQuantity()) / globalTotal) * 100 : 0;

            Object[] row = {
                    g.getName(),
                    String.valueOf(g.getQuantity()),
                    String.format("%.2f/s", rowTotal),
                    String.format("%.2f/s", rowTotal * g.getQuantity()),
                    String.format("%.1f%%", percentage)
            };
            tableModel.addRow(row);
        }
    }

    /**
     * Cambia el estado visual del botón de mejora del Barista a "Completado".
     */
    public void updateUpgradeBaristaText(){
        btnUpBarista.setText("Upgraded");
    }

    /**
     * Cambia el estado visual del botón de mejora de la Máquina a "Completado".
     */
    public void updateUpgradeMachineText(){
        btnUpMachine.setText("Upgraded");
    }

    /**
     * Cambia el estado visual del botón de mejora de la Plantación a "Completado".
     */
    public void updateUpgradePlantationText(){
        btnUpPlantation.setText("Upgraded");
    }

    /**
     * Cambia la pestaña visible de la tienda a "Generadores" y actualiza los estilos
     * de los botones de navegación para reflejar la selección activa.
     */
    public void putGenerators() {
        cardLayout.show(cardsContainer, "GENERATORS");
        navGeneratorsBtn.resetButtonColors(PRIMARY_COFFEE, BG_COLOR, BG_COLOR, PRIMARY_COFFEE);
        navGeneratorsBtn.setBorder(new RoundedBorder(PRIMARY_COFFEE, 20, 1));
        navUpgradesBtn.resetButtonColors(Color.WHITE, Color.LIGHT_GRAY, Color.LIGHT_GRAY, Color.WHITE);
        navUpgradesBtn.setBorder(new RoundedBorder(Color.LIGHT_GRAY, 20, 1));
        navGeneratorsBtn.repaint();
        navUpgradesBtn.repaint();
    }

    /**
     * Cambia la pestaña visible de la tienda a "Upgrades" y actualiza los estilos
     * de los botones de navegación para reflejar la selección activa.
     */
    public void putUpgrades() {
        cardLayout.show(cardsContainer, "UPGRADES");
        navUpgradesBtn.resetButtonColors(PRIMARY_COFFEE, BG_COLOR, BG_COLOR, PRIMARY_COFFEE);
        navUpgradesBtn.setBorder(new RoundedBorder(PRIMARY_COFFEE, 20, 1));
        navGeneratorsBtn.resetButtonColors(Color.WHITE, Color.LIGHT_GRAY, Color.LIGHT_GRAY, Color.WHITE);
        navGeneratorsBtn.setBorder(new RoundedBorder(Color.LIGHT_GRAY, 20, 1));
        navGeneratorsBtn.repaint();
        navUpgradesBtn.repaint();
    }
}