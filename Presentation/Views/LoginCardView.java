package Presentation.Views;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class LoginCardView extends JPanel {

    // Paleta de colores extraída del diseño
    private final Color BACKGROUND_COLOR = new Color(248, 245, 240); // Beige claro
    private final Color CARD_COLOR = Color.WHITE;
    private final Color TEXT_DARK = new Color(51, 51, 51);
    private final Color TEXT_LIGHT = new Color(136, 136, 136);
    private final Color BUTTON_COLOR = new Color(139, 69, 19); // Marrón café
    private final Color BORDER_COLOR = new Color(224, 224, 224);

    private Timer animationTimer;

    private final static String CUP1 = "assets/gif-taza/gif1.png";
    private final static String CUP2 = "assets/gif-taza/gif2.png";
    private final static String CUP3 = "assets/gif-taza/gif3.png";
    private final static String CUP4 = "assets/gif-taza/gif4.png";
    private final static String CUP5 = "assets/gif-taza/gif5.png";
    private final static String CUP6 = "assets/gif-taza/gif6.png";
    private final static String CUP7 = "assets/gif-taza/gif7.png";
    private final static String CUP8 = "assets/gif-taza/gif8.png";
    private final static String CUP9 = "assets/gif-taza/gif9.png";
    private final static String CUP10 = "assets/gif-taza/gif10.png";
    private final static String CUP11 = "assets/gif-taza/gif11.png";
    private final static String CUP12 = "assets/gif-taza/gif12.png";

    public LoginCardView() {
        // 1. Configuración principal del contenedor (Responsividad)
        setBackground(BACKGROUND_COLOR);
        setLayout(new GridBagLayout()); // Centra el contenido dinámicamente

        // Panel agrupador para el encabezado (Logo/Títulos) y la Tarjeta
        JPanel wrapperPanel = new JPanel(new GridBagLayout());
        wrapperPanel.setOpaque(false);

        GridBagConstraints gbcMain = new GridBagConstraints();
        gbcMain.gridx = 0;
        gbcMain.gridy = 0;
        gbcMain.insets = new Insets(10, 10, 20, 10);
        gbcMain.anchor = GridBagConstraints.CENTER;

        // --- 2. CONSTRUCCIÓN DEL ENCABEZADO EXTERNO ---
        wrapperPanel.add(createHeaderPanel(), gbcMain);

        // --- 3. CONSTRUCCIÓN DE LA TARJETA BLANCA (Card) ---
        gbcMain.gridy = 1;
        wrapperPanel.add(createCardPanel(), gbcMain);

        // Añadir el agrupador al panel principal
        add(wrapperPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new GridBagLayout());
        headerPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(2, 0, 2, 0);

        JImagePanel cupGif = new JImagePanel(CUP1);
        ArrayList<String> cupGifList = new ArrayList<>(Arrays.asList(
                CUP1,
                CUP2,
                CUP3,
                CUP4,
                CUP5,
                CUP6,
                CUP7,
                CUP8,
                CUP9,
                CUP10,
                CUP11,
                CUP12
        ));

        if(animationTimer != null){
            animationTimer.stop();
            animationTimer = null;
        }

        animationTimer = cupGif.configureAnimation(200, cupGifList);
        cupGif.setMinimumSize(new Dimension(50, 50));
        cupGif.setOpaque(false);
        cupGif.setAlignmentX(Component.CENTER_ALIGNMENT);
        cupGif.setAlignmentY(Component.CENTER_ALIGNMENT);
        headerPanel.add(cupGif, gbc);

        gbc.gridy++;
        JLabel titleLabel = new JLabel("Coffee Clicker");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(TEXT_DARK);
        headerPanel.add(titleLabel, gbc);

        gbc.gridy++;
        JLabel subtitleLabel = new JLabel("Brew your way to coffee empire");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(TEXT_LIGHT);
        headerPanel.add(subtitleLabel, gbc);

        return headerPanel;
    }

    private JPanel createCardPanel() {
        JPanel cardPanel = new JPanel(new GridBagLayout());
        cardPanel.setBackground(CARD_COLOR);

        // Borde de la tarjeta
        RoundedBorder lineBorder = new RoundedBorder(BACKGROUND_COLOR, 50, 25, 1); // Clase propia
        Border padding = BorderFactory.createEmptyBorder(30, 40, 30, 40); // Margen interno
        cardPanel.setBorder(BorderFactory.createCompoundBorder(lineBorder, padding));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(5, 0, 5, 0);

        // Títulos de la tarjeta
        JLabel welcomeLabel = new JLabel("Welcome Back", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        welcomeLabel.setForeground(TEXT_DARK);
        cardPanel.add(welcomeLabel, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 20, 0);
        JLabel signinSubLabel = new JLabel("Sign in to continue your coffee journey", SwingConstants.CENTER);
        signinSubLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        signinSubLabel.setForeground(TEXT_LIGHT);
        cardPanel.add(signinSubLabel, gbc);

        // Input del usuario
        gbc.insets = new Insets(10, 0, 5, 0);
        gbc.gridy++;
        addInputLabel(cardPanel, "Username or Email", gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 15, 0);
        JTextField userField = createUsernameField("Enter username or email");
        userField.setBorder(new RoundedBorder(BORDER_COLOR, 20, 1, 0));
        cardPanel.add(userField, gbc);

        // Input de la contraseña
        gbc.insets = new Insets(5, 0, 5, 0);
        gbc.gridy++;
        addInputLabel(cardPanel, "Password", gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 25, 0);
        JPasswordField passField = createPasswordField("Enter password");
        passField.setBorder(new RoundedBorder(BORDER_COLOR, 20, 1, 0));
        cardPanel.add(passField, gbc);

        // Botón
        gbc.gridy++;
        gbc.insets = new Insets(10, 0, 15, 0);
        JButton loginBtn = new JButton("Sign In");
        loginBtn.setBackground(BUTTON_COLOR);
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginBtn.setFocusPainted(false);
        loginBtn.setPreferredSize(new Dimension(300, 40));
        loginBtn.setBorder(new RoundedBorder(CARD_COLOR, 20, 10, 1));
        loginBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cardPanel.add(loginBtn, gbc);

        // Footer
        gbc.gridy++;
        JLabel footerLabel = new JLabel("Don't have an account? Create one", SwingConstants.CENTER);
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cardPanel.add(footerLabel, gbc);

        return cardPanel;
    }

    private void addInputLabel(JPanel panel, String text, GridBagConstraints gbc) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(TEXT_DARK);
        panel.add(label, gbc);
    }

    private JTextField createUsernameField(String placeholder) {
        JTextField textField = new JTextField();
        textField.setPreferredSize(new Dimension(300, 35));

        // Estado inicial
        textField.setText(placeholder);
        textField.setForeground(TEXT_LIGHT);

        // Lógica de Placeholder
        textField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                // Si el texto actual es el placeholder, se vacía para que el usuario escriba
                if (textField.getText().equals(placeholder)) {
                    textField.setText("");
                    textField.setForeground(TEXT_DARK);
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                // Si el usuario sale del input dejándolo vacío, vuelve el placeholder
                if (textField.getText().isEmpty()) {
                    textField.setForeground(TEXT_LIGHT);
                    textField.setText(placeholder);
                }
            }
        });

        return textField;
    }

    private JPasswordField createPasswordField(String placeholder) {
        JPasswordField passField = new JPasswordField();
        passField.setPreferredSize(new Dimension(300, 35));

        // Obtenemos el carácter de censura por defecto del sistema (suele ser '•' o '*')
        char defaultEchoChar = passField.getEchoChar();

        // Estado inicial: Mostramos el texto del placeholder (sin censurar)
        passField.setText(placeholder);
        passField.setForeground(TEXT_LIGHT);
        passField.setEchoChar((char) 0); // El valor 0 desactiva la ocultación temporalmente

        // Lógica de Placeholder adaptada para contraseñas
        passField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                String currentText = String.valueOf(passField.getPassword());
                if (currentText.equals(placeholder)) {
                    passField.setText("");
                    passField.setForeground(TEXT_DARK);
                    passField.setEchoChar(defaultEchoChar); // Reactivamos la censura
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                String currentText = String.valueOf(passField.getPassword());
                if (currentText.isEmpty()) {
                    passField.setForeground(TEXT_LIGHT);
                    passField.setEchoChar((char) 0); // Desactivamos censura para leer el placeholder
                    passField.setText(placeholder);
                }
            }
        });

        return passField;
    }
}