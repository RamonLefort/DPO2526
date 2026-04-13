package Presentation.Views;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * The type J image panel.
 */
// Custom JPanel that renders an image in the background
public class JImagePanel extends JPanel {

    // The image to render
    private BufferedImage image;
    private Timer animationTimer;
    private ArrayList<BufferedImage> loadedFrames = new ArrayList<>();

    /**
     * Instantiates a new J image panel.
     *
     * @param path the path
     */
// Constructor with parameters
    public JImagePanel(String path) {
        try {
            image = ImageIO.read(new File(path));
        } catch (IOException e) {
            // Not properly managed, sorry!
            e.printStackTrace();
        }
    }

    // IMPORTANT: WE override this to scale the image in layouts that stretch it horizontally while respecting its preferred vertical size
    // THIS WILL NOT WORK IF YOU HAVE OTHER GOALS, DON'T REUSE WITHOUT THINKING
    @Override
    public Dimension getPreferredSize() {
        // Prevención de NullPointerException si la imagen falla al cargar
        if (image == null) {
            // Un tamaño de seguridad por defecto para que no desaparezca la interfaz
            return new Dimension(100, 100);
        }

        // Devolvemos el tamaño real de la imagen.
        // GridBagLayout lo usará como base para centrar el componente.
        return new Dimension(image.getWidth(), image.getHeight());
    }

    // Paint the image in the background, with the size the layout assigns to the panel
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Si la imagen está lista y tiene dimensiones válidas
        if (image != null && image.getWidth(this) > 0) {
            Graphics2D g2d = (Graphics2D) g.create();

            // Anti-aliasing y suavizado de píxeles al estirar
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int panelW = getWidth();
            int panelH = getHeight();
            int imgW = image.getWidth(this);
            int imgH = image.getHeight(this);

            // 1. Cálculo del Ratio de Contención Matemático
            double ratio = Math.min((double) panelW / imgW, (double) panelH / imgH);

            // 2. Nuevas dimensiones manteniendo la proporción estricta
            int drawW = (int) (imgW * ratio);
            int drawH = (int) (imgH * ratio);

            // 3. Coordenadas para centrar la imagen en el panel (eje X e Y)
            int x = (panelW - drawW) / 2;
            int y = (panelH - drawH) / 2;

            // Se dibuja con el tamaño calculado, no con el tamaño del panel
            g2d.drawImage(image, x, y, drawW, drawH, this);

            g2d.dispose();
        }
    }

    /**
     * Sets image.
     *
     * @param path the path
     */
    public void setImage(String path) {
        try {
            this.image = ImageIO.read(new File(path));
            repaint();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Configure animation timer.
     *
     * @param frameTimer the frame timer
     * @param framesList the frames list
     * @return the timer
     */
    public Timer configureAnimation(int frameTimer, ArrayList<String> framesList) {
        if (animationTimer != null && animationTimer.isRunning()) {
            animationTimer.stop();
        }

        animationTimer = null;

        loadedFrames.clear();

        for (String path : framesList) {
            try {
                loadedFrames.add(ImageIO.read(new File(path)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (loadedFrames.isEmpty()) return null;

        animationTimer = new Timer(frameTimer, new ActionListener() {
            int index = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                image = loadedFrames.get(index);
                repaint(); // ¡Redibujar!
                index = (index + 1) % loadedFrames.size();
            }
        });

        animationTimer.start();
        return animationTimer;
    }

    /**
     * Start animation.
     */
    public void startAnimation() {
        animationTimer.start();
    }

    /**
     * Stop animation.
     */
    public void stopAnimation() {
        if (animationTimer != null && animationTimer.isRunning()) {
            animationTimer.stop();
        }
    }
}