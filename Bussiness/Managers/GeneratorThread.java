package Bussiness.Managers;

import Bussiness.Entities.Generator;
import Bussiness.Entities.Game;

public class GeneratorThread extends Thread {
    private final Generator generator;
    private final Game game;
    private final GameViewUpdateListener listener;
    private boolean running = true;

    // Interfaz para notificar a la vista sin acoplamiento fuerte
    public interface GameViewUpdateListener {
        void onProductionUpdate(double newTotal);
    }

    public GeneratorThread(Generator generator, Game game, GameViewUpdateListener listener) {
        this.generator = generator;
        this.game = game;
        this.listener = listener;
    }

    @Override
    public void run() {
        while (running) {
            try {
                // El periodo suele estar en milisegundos en la DB
                Thread.sleep(generator.getPeriod());

                synchronized (game) {
                    double production = generator.getTotalProduction();
                    game.addMoney(production);
                    if (listener != null) {
                        listener.onProductionUpdate(game.getMoney());
                    }
                }
            } catch (InterruptedException e) {
                running = false; // Detener si el hilo es interrumpido
            }
        }
    }

    public void stopGenerator() {
        this.running = false;
        this.interrupt();
    }
}