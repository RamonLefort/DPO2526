package Presentation.Views;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class RegisterWindow extends JPanel {

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

    public JTextField getUserField(){ return userField;}
    public JTextField getMailField(){ return mailField;}
    public JPasswordField getPasswordField(){ return passwordField;}
    public JPasswordField getConfirmField() { return confirmField;}
    public JLabel getErrorLabel(){ return errorLabel;}
    public JLabel getFooterLabel(){ return footerLabel;}
    public JButton getRegisterButton(){ return registerBtn;}
    public void showError(String message){
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
}
