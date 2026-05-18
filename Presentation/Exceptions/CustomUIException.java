package Presentation.Exceptions;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class CustomUIException extends Exception {
    private final String title;
    private final int messageType;

    private static final Color BG_COLOR = new Color(248, 245, 240);
    private static final Color BUTTON_COLOR = new Color(139, 69, 19);

    public CustomUIException(String message, String title, int messageType) {
        super(message);
        this.title = title;
        this.messageType = messageType;
    }

    public void showDialog(Component parent) {
        UIManager.put("OptionPane.background", BG_COLOR);
        UIManager.put("Panel.background", BG_COLOR);
        UIManager.put("Button.background", BUTTON_COLOR);
        UIManager.put("Button.foreground", Color.WHITE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BG_COLOR);
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JTextArea messageArea = new JTextArea(getMessage());
        messageArea.setFont(new Font("Segoe UI", Font.BOLD, 14));
        messageArea.setForeground(new Color(51, 51, 51)); // #333333
        messageArea.setBackground(BG_COLOR);

        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        messageArea.setEditable(false);
        messageArea.setFocusable(false);

        messageArea.setAlignmentX(Component.CENTER_ALIGNMENT);

        messageArea.setSize(new Dimension(260, 60));
        messageArea.setPreferredSize(new Dimension(260, 60));

        mainPanel.add(messageArea, BorderLayout.CENTER);

        JOptionPane.showMessageDialog(parent, mainPanel, this.title, this.messageType);
    }
}