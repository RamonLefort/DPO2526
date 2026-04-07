import Presentation.Views.LoginCardView;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Coffee Clicker");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1100, 700);
            frame.setLocationRelativeTo(null);

            try {
                URL iconURL = Main.class.getResource("/assets/icono.png");

                if (iconURL != null) {
                    Image icon = ImageIO.read(iconURL);
                    frame.setIconImage(icon);
                } else {
                    System.err.println("WARNING: No se encontró la imagen del icono.");
                }
            } catch (IOException e) {
                System.err.println("Error al leer el archivo de imagen: " + e.getMessage());
            }

            frame.setContentPane(new LoginCardView());
            frame.setVisible(true);
        });
    }
}