package Presentation.Views;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class GameCreator extends JPanel {

    private final Color BG_COLOR = new Color(248, 245, 240);
    private final Color PRIMARY_COFFEE = new Color(74, 44, 23);
    private final Color TEXT_SECONDARY = new Color(110, 110, 110);
    private final Color TEXT_DARK = new Color(51, 51, 51);
    private final Color TEXT_LIGHT = new Color(136, 136, 136);

    public static final String BTN_BACK = "BACK";
    public static final String BTN_LOGOUT = "LOGOUT";
    public static final String BTN_CREATE = "CREATE";

    private JButton btnBack;
    private JButton btnLogout;
    private JButton btnCreate;
    private JTextField nameField;

    public GameCreator() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBackground(BG_COLOR);
        this.setBorder(new EmptyBorder(20, 40, 20, 40));

        this.add(createTopHeader());
        this.add(Box.createVerticalGlue());
        this.add(createFormContent());
        this.add(Box.createVerticalGlue());
    }

    private JPanel createTopHeader() {
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.X_AXIS));
        header.setOpaque(false);
        header.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        header.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Botón de Back
        btnBack = new RoundedButton("< Atrás", 20, PRIMARY_COFFEE, BG_COLOR, BG_COLOR, PRIMARY_COFFEE);
        btnBack.setActionCommand(BTN_BACK);
        btnBack.setBorder(new RoundedBorder(PRIMARY_COFFEE, 20, 1));
        btnBack.setPreferredSize(new Dimension(100, 35));
        btnBack.setMaximumSize(new Dimension(100, 35));

        // Título
        JLabel title = new JLabel("Coffee Clicker");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(PRIMARY_COFFEE);

        // Botón Logout
        btnLogout = new RoundedButton("Logout", 20, PRIMARY_COFFEE, BG_COLOR, BG_COLOR, PRIMARY_COFFEE);
        btnLogout.setActionCommand(BTN_LOGOUT);
        btnLogout.setBorder(new RoundedBorder(PRIMARY_COFFEE, 20, 1));
        btnLogout.setPreferredSize(new Dimension(100, 35));
        btnLogout.setMaximumSize(new Dimension(100, 35));

        header.add(btnBack);
        header.add(Box.createHorizontalGlue());
        header.add(title);
        header.add(Box.createHorizontalGlue());
        header.add(btnLogout);

        return header;
    }

    private JPanel createFormContent() {
        JPanel formWrapper = new JPanel();
        formWrapper.setLayout(new BoxLayout(formWrapper, BoxLayout.Y_AXIS));
        formWrapper.setOpaque(false);
        formWrapper.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Título Principal
        JLabel mainTitle = new JLabel("Create your next coffee empire");
        mainTitle.setFont(new Font("Segoe UI", Font.BOLD, 32));
        mainTitle.setForeground(PRIMARY_COFFEE);
        mainTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Subtítulo
        JLabel subTitle = new JLabel("This could be the start of your coffee franchise");
        subTitle.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subTitle.setForeground(TEXT_SECONDARY);
        subTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        formWrapper.add(mainTitle);
        formWrapper.add(Box.createRigidArea(new Dimension(0, 10)));
        formWrapper.add(subTitle);
        formWrapper.add(Box.createRigidArea(new Dimension(0, 30)));

        // CardPanel
        JPanel cardPanel = new RoundedPanel(20, Color.WHITE);
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBorder(new EmptyBorder(30, 40, 30, 40));
        cardPanel.setMaximumSize(new Dimension(500, 250));
        cardPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Name input
        nameField = createUsernameField("My Coffee Empire");
        cardPanel.add(createInputGroup("Game Name", nameField));


        //Botón Create Game
        btnCreate = new RoundedButton("Create Game", 20, PRIMARY_COFFEE, Color.WHITE, Color.WHITE, PRIMARY_COFFEE);
        btnCreate.setActionCommand(BTN_CREATE);
        btnCreate.setBorder(new RoundedBorder(PRIMARY_COFFEE, 20,1));
        btnCreate.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        btnCreate.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCreate.setAlignmentX(Component.CENTER_ALIGNMENT);

        cardPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        cardPanel.add(btnCreate);

        formWrapper.add(cardPanel);

        return formWrapper;
    }

    private JPanel createInputGroup(String labelText, JComponent inputField) {
        JPanel group = new JPanel();
        group.setLayout(new BoxLayout(group, BoxLayout.Y_AXIS));
        group.setOpaque(false);
        group.setAlignmentX(Component.CENTER_ALIGNMENT);
        group.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(TEXT_DARK);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        inputField.setAlignmentX(Component.RIGHT_ALIGNMENT);
        inputField.setPreferredSize(new Dimension(Integer.MAX_VALUE, 40));
        inputField.setMinimumSize(new Dimension(Integer.MAX_VALUE, 40));
        inputField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        inputField.setBorder(new RoundedBorder(TEXT_LIGHT, 20, 1));

        group.add(label);
        group.add(Box.createVerticalStrut(8));
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
    public void setActionListener(ActionListener listener) {
        btnBack.addActionListener(listener);
        btnLogout.addActionListener(listener);
        btnCreate.addActionListener(listener);
    }

    public String getGameName() {
        return nameField.getText().trim();
    }
    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}