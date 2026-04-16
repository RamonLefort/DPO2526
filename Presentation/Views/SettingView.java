package Presentation.Views;

import javax.swing.*;
import java.awt.*;
public class SettingView extends JPanel {


    public static final String BTN_LOGOUT = "BTN_LOGOUT";
    public static final String BTN_DELETE_ACCOUNT = "BTN_DELETE_ACCOUNT";

    private JButton logoutBtn;
    private JButton deleteAccountBtn;

    private final Color BACKGROUND_COLOR = new Color(248, 245, 240);
    private final Color DANGER_COLOR = new Color(220, 53, 69);
    private final Color BUTTON_COLOR = new Color(139, 69, 19);
    private final Color CARD_COLOR = Color.WHITE;

    public SettingView() {
        setBackground(BACKGROUND_COLOR);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        logoutBtn = new RoundedButton("Log Out", 20, BUTTON_COLOR, CARD_COLOR, CARD_COLOR, BUTTON_COLOR);
        formatButton(logoutBtn, BUTTON_COLOR);

        deleteAccountBtn = new RoundedButton("Delete Account", 20, DANGER_COLOR, CARD_COLOR, CARD_COLOR, DANGER_COLOR);
        formatButton(deleteAccountBtn, DANGER_COLOR);

        // Empuja hacia abajo
        add(Box.createVerticalGlue());

        // Título
        JLabel titleLabel = new JLabel("Settings");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(titleLabel);

        add(Box.createVerticalStrut(40));

        // Botón de logout
        add(logoutBtn);
        add(Box.createVerticalStrut(20));
        // Botón de eliminar cuenta
        add(deleteAccountBtn);

        // Empuja hacia arriba
        add(Box.createVerticalGlue());
    }

    /**
     * Método auxiliar para estandarizar el tamaño y diseño visual de los botones.
     */
    private void formatButton(JButton btn, Color bc) {
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorder(new RoundedBorder(bc, 20, 1f));
        Dimension buttonSize = new Dimension(200, 40);
        btn.setPreferredSize(buttonSize);
        btn.setMinimumSize(buttonSize);
        btn.setMaximumSize(buttonSize);
    }

    public JButton getLogoutBtn() { return logoutBtn; }
    public JButton getDeleteAccountBtn() { return deleteAccountBtn; }
}