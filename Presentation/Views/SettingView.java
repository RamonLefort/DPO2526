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

    public SettingView() {
        setBackground(BACKGROUND_COLOR);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        logoutBtn = new JButton("Log Out");
        logoutBtn.setActionCommand(BTN_LOGOUT);
        formatButton(logoutBtn, BUTTON_COLOR, Color.WHITE);

        deleteAccountBtn = new JButton("Delete Account");
        deleteAccountBtn.setActionCommand(BTN_DELETE_ACCOUNT);
        formatButton(deleteAccountBtn, DANGER_COLOR, Color.WHITE);

        add(Box.createVerticalGlue());

        JLabel titleLabel = new JLabel("Settings");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(titleLabel);

        add(Box.createVerticalStrut(40));
        add(logoutBtn);
        add(Box.createVerticalStrut(20));
        add(deleteAccountBtn);

        add(Box.createVerticalGlue());
    }

    private void formatButton(JButton btn, Color bg, Color fg) {
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);

        Dimension buttonSize = new Dimension(200, 40);
        btn.setPreferredSize(buttonSize);
        btn.setMinimumSize(buttonSize);
        btn.setMaximumSize(buttonSize);
    }

    public JButton getLogoutBtn() { return logoutBtn; }
    public JButton getDeleteAccountBtn() { return deleteAccountBtn; }
}