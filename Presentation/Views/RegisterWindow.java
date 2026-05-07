package Presentation.Views;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Vista encargada de proporcionar la interfaz de registro de nuevos usuarios.
 * Esta clase extiende {@link JPanel} y organiza los componentes necesarios para capturar
 * el correo electrónico, nombre de usuario y contraseña (con confirmación).
 * Implementa un diseño visual moderno basado en una tarjeta central con bordes redondeados,
 * campos de texto con placeholders dinámicos y una animación de cabecera que refuerza
 * el tema cafetalero del juego.
 */
public class RegisterWindow extends JPanel {

    /** Comando de acción para registrar a un usuario e ir a la pantalla de Login. */
    public static final String BTN_REGISTER = "BTN_REGISTER";

    private final Color BACKGROUND_COLOR = new Color(248, 245, 240);
    private final Color CARD_COLOR = Color.WHITE;
    private final Color TEXT_DARK = new Color(51, 51, 51);
    private final Color TEXT_LIGHT = new Color(136, 136, 136);
    private final Color BUTTON_COLOR = new Color(139, 69, 19);

    private Timer animationTimer;

    private JTextField userField;
    private JTextField mailField;
    private JPasswordField passwordField;
    private JPasswordField confirmField;
    private JLabel errorLabel;
    private JLabel footerLabel;
    private JButton registerBtn;

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

    /**
     * Constructor de la ventana de registro.
     * Configura el layout principal mediante {@link BoxLayout} vertical.
     * Establece el fondo y añade de forma secuencial la cabecera animada y la tarjeta
     * del formulario, utilizando pegamento vertical para el centrado.
     */
    public RegisterWindow() {
        setBackground(BACKGROUND_COLOR);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        add(Box.createVerticalGlue());

        JPanel header = createHeaderPanel();
        header.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(header);

        add(Box.createVerticalStrut(20));

        JPanel card = createCardPanel();
        card.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(card);

        add(Box.createVerticalGlue());
    }

    /**
     * Crea el panel de cabecera con la animación cíclica.
     * Inicializa un {@link JImagePanel} y configura un {@link Timer} para iterar sobre
     * la lista de frames de la imagen de la taza, además de añadir los títulos principales.
     *
     * @return El panel de cabecera configurado y animado.
     */
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setOpaque(false);
        headerPanel.add(Box.createVerticalStrut(10));

        //Gif Taza
        JImagePanel cupGif = new JImagePanel(CUP1);
        ArrayList<String> cupGifList = new ArrayList<>(Arrays.asList(
                CUP1, CUP2, CUP3, CUP4, CUP5, CUP6, CUP7, CUP8, CUP9, CUP10, CUP11, CUP12
        ));

        if(animationTimer != null){
            animationTimer.stop();
            animationTimer = null;
        }

        animationTimer = cupGif.configureAnimation(200, cupGifList);
        cupGif.setMinimumSize(new Dimension(50, 50));
        cupGif.setPreferredSize(new Dimension(50, 50));
        cupGif.setOpaque(false);
        cupGif.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(cupGif);

        headerPanel.add(Box.createVerticalStrut(10));

        // Título
        JLabel titleLabel = new JLabel("Coffee Clicker");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(TEXT_DARK);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(titleLabel);

        headerPanel.add(Box.createVerticalStrut(5));

        // Subtítulo
        JLabel subtitleLabel = new JLabel("Brew your way to coffee empire");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(TEXT_LIGHT);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(subtitleLabel);

        return headerPanel;
    }

    /**
     * Construye la tarjeta central que contiene los campos del formulario.
     * Incluye la instanciación de los campos de email, usuario, contraseña y confirmación,
     * así como el botón de registro y el enlace de navegación hacia el login.
     *
     * @return Un {@link RoundedPanel} con todos los elementos interactivos del registro.
     */
    private JPanel createCardPanel() {
        JPanel cardPanel = new RoundedPanel(100, CARD_COLOR);
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setMaximumSize(new Dimension(380, 560)); // Aumentado un poco el alto para el nuevo campo

        RoundedBorder lineBorder = new RoundedBorder(BACKGROUND_COLOR, 50, 25);
        Border padding = BorderFactory.createEmptyBorder(0, 20, 30, 20);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(lineBorder, padding));

        // Titulo Card
        JLabel welcomeLabel = new JLabel("Welcome here!");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        welcomeLabel.setForeground(TEXT_DARK);
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        cardPanel.add(welcomeLabel);
        cardPanel.add(Box.createVerticalStrut(5));

        // Card Subtitulo
        JLabel signinSubLabel = new JLabel("Sign up to continue your coffee journey");
        signinSubLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        signinSubLabel.setForeground(TEXT_LIGHT);
        signinSubLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        cardPanel.add(signinSubLabel);
        cardPanel.add(Box.createVerticalStrut(20));

        // Email
        this.mailField = createUsernameField("Enter email");
        cardPanel.add(createInputGroup("Email", this.mailField));
        cardPanel.add(Box.createVerticalStrut(10));

        // Username
        this.userField = createUsernameField("Enter username");
        cardPanel.add(createInputGroup("Username", this.userField));
        cardPanel.add(Box.createVerticalStrut(10));

        // Password
        this.passwordField = createPasswordField("Enter password");
        cardPanel.add(createInputGroup("Password", this.passwordField));
        cardPanel.add(Box.createVerticalStrut(10));

        // Confirmar Password
        this.confirmField = createPasswordField("Confirm password");
        cardPanel.add(createInputGroup("Confirm Password", this.confirmField));
        cardPanel.add(Box.createVerticalStrut(10));

        // Texto Error
        this.errorLabel = new JLabel(" ");
        this.errorLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        this.errorLabel.setForeground(Color.RED);
        this.errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.errorLabel.setVisible(false);
        cardPanel.add(this.errorLabel);
        cardPanel.add(Box.createVerticalStrut(10));

        // Botón Registro
        this.registerBtn = new RoundedButton("Sign Up", 20, BUTTON_COLOR, CARD_COLOR, Color.WHITE, BUTTON_COLOR);
        this.registerBtn.setActionCommand(BTN_REGISTER);
        this.registerBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        this.registerBtn.setMaximumSize(new Dimension(300, 35));
        this.registerBtn.setPreferredSize(new Dimension(300, 35));
        this.registerBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.registerBtn.setBorder(new RoundedBorder(BUTTON_COLOR, 20, 1f));

        cardPanel.add(this.registerBtn);
        cardPanel.add(Box.createVerticalStrut(20));

        // Footer
        JPanel footerPanel = new JPanel();
        footerPanel.setLayout(new BoxLayout(footerPanel, BoxLayout.X_AXIS));
        footerPanel.setOpaque(false);
        footerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel textLabel = new JLabel("I already have an account ");
        textLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        textLabel.setForeground(TEXT_DARK);

        // Botón Login
        this.footerLabel = new JLabel("Sign in");
        this.footerLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        this.footerLabel.setForeground(BUTTON_COLOR);
        this.footerLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        this.footerLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                footerLabel.setText("Sign in");
            }
        });

        footerPanel.add(textLabel);
        footerPanel.add(this.footerLabel);

        cardPanel.add(footerPanel);
        return cardPanel;
    }

    /**
     * Crea grupos de inputs etiquetados.
     *
     * @param labelText Texto de la etiqueta.
     * @param inputField Componente de entrada (texto o contraseña).
     * @return Panel agrupador configurado.
     */
    private JPanel createInputGroup(String labelText, JComponent inputField) {
        JPanel group = new JPanel();
        group.setLayout(new BoxLayout(group, BoxLayout.Y_AXIS));
        group.setBackground(CARD_COLOR);

        group.setAlignmentX(Component.CENTER_ALIGNMENT);
        group.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(TEXT_DARK);

        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        inputField.setAlignmentX(Component.LEFT_ALIGNMENT);
        inputField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        inputField.setBorder(new RoundedBorder(TEXT_LIGHT, 20, 1));

        group.add(label);
        group.add(Box.createVerticalStrut(5));
        group.add(inputField);

        return group;
    }

    /**
     * Crea un campo de texto con gestión de "placeholder".
     *
     * @param placeholder Texto de ayuda inicial.
     * @return El campo de texto configurado.
     */
    private JTextField createUsernameField(String placeholder) {
        JTextField textField = new JTextField();
        textField.setPreferredSize(new Dimension(Integer.MAX_VALUE, 35));
        textField.setText(placeholder);
        textField.setForeground(TEXT_LIGHT);
        textField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (textField.getText().equals(placeholder)) {
                    textField.setText("");
                    textField.setForeground(TEXT_DARK);
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (textField.getText().isEmpty()) {
                    textField.setForeground(TEXT_LIGHT);
                    textField.setText(placeholder);
                }
            }
        });

        return textField;
    }

    /**
     * Crea un campo de contraseña con gestión de "placeholder".
     *
     * @param placeholder Texto de ayuda inicial.
     * @return El campo de contraseña configurado.
     */
    private JPasswordField createPasswordField(String placeholder) {
        JPasswordField passField = new JPasswordField();
        passField.setPreferredSize(new Dimension(Integer.MAX_VALUE, 35));

        char defaultEchoChar = passField.getEchoChar();

        passField.setText(placeholder);
        passField.setForeground(TEXT_LIGHT);
        passField.setEchoChar((char) 0);

        passField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                String currentText = String.valueOf(passField.getPassword());
                if (currentText.equals(placeholder)) {
                    passField.setText("");
                    passField.setForeground(TEXT_DARK);
                    passField.setEchoChar(defaultEchoChar);
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                String currentText = String.valueOf(passField.getPassword());
                if (currentText.isEmpty()) {
                    passField.setForeground(TEXT_LIGHT);
                    passField.setEchoChar((char) 0);
                    passField.setText(placeholder);
                }
            }
        });

        return passField;
    }

    /**
     * @return Referencia al campo de nombre de usuario.
     */
    public JTextField getUserField(){ return userField;}

    /**
     * @return Referencia al campo de correo electrónico.
     */
    public JTextField getMailField(){ return mailField;}

    /**
     * @return Referencia al campo de contraseña principal.
     */
    public JPasswordField getPasswordField(){ return passwordField;}

    /**
     * @return Referencia al campo de confirmación de contraseña.
     */
    public JPasswordField getConfirmField() { return confirmField;}

    /**
     * @return Referencia a la etiqueta de errores.
     */
    public JLabel getErrorLabel(){ return errorLabel;}

    /**
     * @return Referencia a la etiqueta del pie de página.
     */
    public JLabel getFooterLabel(){ return footerLabel;}

    /**
     * @return Referencia al botón principal de registro.
     */
    public JButton getRegisterButton(){ return registerBtn;}

    /**
     * Actualiza y hace visible la etiqueta de error con un mensaje específico.
     *
     * @param message El mensaje de error a mostrar al usuario.
     */
    public void showError(String message){
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
}
