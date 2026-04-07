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
        if(image != null) {
            g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
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