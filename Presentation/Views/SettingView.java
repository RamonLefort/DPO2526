package Presentation.Views;

import javax.swing.*;
import java.awt.*;

public class SettingView extends JPanel {

    private JButton logoutBtn;
    private JButton deleteAccountBtn;

    // Paleta de colores básica
    private final Color BACKGROUND_COLOR = new Color(248, 245, 240);
    private final Color DANGER_COLOR = new Color(220, 53, 69); // Rojo estándar para acciones destructivas
    private final Color BUTTON_COLOR = new Color(139, 69, 19);

    public SettingView() {
        // 1. Configuración base del Panel
        setBackground(BACKGROUND_COLOR);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // 2. Inicialización de componentes
        logoutBtn = new JButton("Log Out");
        formatButton(logoutBtn, BUTTON_COLOR, Color.WHITE);

        deleteAccountBtn = new JButton("Delete Account");
        formatButton(deleteAccountBtn, DANGER_COLOR, Color.WHITE);

        // 3. Construcción del Layout (El "sándwich" de pegamento)

        // Empuja todo hacia abajo
        add(Box.createVerticalGlue());

        // Título opcional para dar contexto
        JLabel titleLabel = new JLabel("Settings");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(titleLabel);

        add(Box.createVerticalStrut(40)); // Espacio fijo entre título y botones

        // Añadimos los botones centrados
        add(logoutBtn);
        add(Box.createVerticalStrut(20)); // Espacio fijo entre botones
        add(deleteAccountBtn);

        // Empuja todo hacia arriba, colisionando en el centro
        add(Box.createVerticalGlue());
    }

    /**
     * Método auxiliar para estandarizar el tamaño y diseño visual de los botones.
     */
    private void formatButton(JButton btn, Color bg, Color fg) {
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false); // Quita el recuadro feo al hacer clic

        // Forzamos el tamaño para que ambos botones midan lo mismo
        Dimension buttonSize = new Dimension(200, 40);
        btn.setPreferredSize(buttonSize);
        btn.setMinimumSize(buttonSize);
        btn.setMaximumSize(buttonSize);
    }

    // Getters para el Controlador
    public JButton getLogoutBtn() { return logoutBtn; }
    public JButton getDeleteAccountBtn() { return deleteAccountBtn; }
}