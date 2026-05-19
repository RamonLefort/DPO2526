package Presentation.Views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Vista encargada de mostrar el panel de ajustes y configuración del usuario.
 * Esta clase extiende {@link JPanel} y ofrece una interfaz simplificada para realizar
 * acciones críticas de la cuenta, como el cierre de sesión o la eliminación permanente
 * del perfil.
 */
public class SettingView extends JPanel {

    public static final String BTN_LOGOUT = "BTN_LOGOUT";
    public static final String BTN_DELETE_ACCOUNT = "BTN_DELETE_ACCOUNT";

    private JButton logoutBtn;
    private JButton deleteAccountBtn;

    private final Color BACKGROUND_COLOR = new Color(248, 245, 240);
    private final Color DANGER_COLOR = new Color(220, 53, 69);
    private final Color BUTTON_COLOR = new Color(74, 44, 23);
    private final Color CARD_COLOR = Color.WHITE;

    /**
     * Constructor de la vista de ajustes.
     * Configura un layout de tipo {@link BoxLayout} en el eje Y y establece
     * el fondo de la ventana. Inicializa los botones de acción aplicando
     * formatos específicos según su relevancia y peligro.
     */
    public SettingView() {
        setBackground(BACKGROUND_COLOR);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        logoutBtn = new RoundedButton("Log Out", 20, BUTTON_COLOR, CARD_COLOR, CARD_COLOR, BUTTON_COLOR);
        deleteAccountBtn = new RoundedButton("Delete Account", 20, DANGER_COLOR, CARD_COLOR, CARD_COLOR, DANGER_COLOR);

        formatButton(logoutBtn, BUTTON_COLOR, BTN_LOGOUT);
        formatButton(deleteAccountBtn, DANGER_COLOR, BTN_DELETE_ACCOUNT);

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

    /**
     * Da formato a los botones de la vista.
     *
     * @param btn     El botón al que aplicar el formato.
     * @param bc      El color del borde asociado a la acción.
     * @param command El String que identifica la acción para el controlador.
     */
    private void formatButton(JButton btn, Color bc, String command) {
        btn.setActionCommand(command);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorder(new RoundedBorder(bc, 20, 1f));

        Dimension buttonSize = new Dimension(200, 40);
        btn.setPreferredSize(buttonSize);
        btn.setMinimumSize(buttonSize);
        btn.setMaximumSize(buttonSize);
    }

    /**
     * Vincula el controlador de eventos a los botones de la vista de ajustes.
     *
     * @param e El {@link ActionListener} que procesará las acciones.
     */
    public void setActionListener(ActionListener e) {
        logoutBtn.addActionListener(e);
        deleteAccountBtn.addActionListener(e);
    }
}
