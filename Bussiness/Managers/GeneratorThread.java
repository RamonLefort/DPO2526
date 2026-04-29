package Bussiness.Managers;

import Bussiness.Entities.Generator;
import Bussiness.Entities.Game;
import Presentation.Views.GameView;
import javax.swing.SwingUtilities;

public class GeneratorThread extends Thread {
    private final Generator generator;
    private final Game game;
    private final GameView gameView;
    private boolean running = true;

    public GeneratorThread(Generator generator, Game game, GameView gameView) {
        this.generator = generator;
        this.game = game;
        this.gameView = gameView;
    }

    @Override
    public void run() {
        while (running) {
            try {
                Thread.sleep(generator.getPeriod());

                double production = generator.getEarning() * generator.getQuantity();

                synchronized (game) {
                    game.addMoney(production);
                    double currentMoney = game.getMoney();

                    SwingUtilities.invokeLater(() -> {
                        gameView.updateCoffeeCount((int) currentMoney);
                    });
                }
            } catch (InterruptedException e) {
                running = false;
            }
        }
    }

    public void stopGenerator() {
        this.running = false;
        this.interrupt();
    }
}