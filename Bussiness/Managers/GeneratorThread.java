package Bussiness.Managers;

import Bussiness.Entities.Generator;
import Bussiness.Entities.Game;
import Presentation.Controllers.GameController;
import Presentation.Views.GameView;
import javax.swing.SwingUtilities;

/**
 * Hilo especializado encargado de gestionar la producción automática de un generador específico.
 * Esta clase extiende {@link Thread} para ejecutar un ciclo de producción en segundo plano.
 * Implementa la lógica de espera basada en el periodo del generador y asegura la sincronización
 * del estado del juego al actualizar los recursos.
 */
public class GeneratorThread extends Thread {
    private final Generator generator;
    private final Game game;
    private final GameView gameView;
    private final GameController gameController;
    private boolean running = true;

    /**
     * Constructor que inicializa el hilo con las dependencias necesarias para la producción y actualización visual.
     *
     * @param generator El modelo del generador que dicta el periodo y la ganancia.
     * @param game El estado global de la partida que será actualizado.
     * @param gameView La interfaz gráfica que recibirá las actualizaciones de recursos.
     * @param gameController El controlador encargado de registrar la telemetría de autogeneración.
     */
    public GeneratorThread(Generator generator, Game game, GameView gameView, GameController gameController) {
        this.generator = generator;
        this.game = game;
        this.gameView = gameView;
        this.gameController = gameController;
    }

    /**
     * Ciclo principal de ejecución del hilo.
     * El hilo permanece en espera durante el tiempo definido por {@code generator.getPeriod()}.
     * Tras la espera, calcula la producción total (ganancia base por cantidad de unidades)
     * y actualiza el estado del juego de forma segura mediante un bloque sincronizado.
     */
    @Override
    public void run() {
        while (running) {
            try {
                // Pausa el hilo según la frecuencia de producción del generador
                Thread.sleep(generator.getPeriod());

                // Cálculo de la producción en función de la eficiencia y cantidad del generador
                double production = generator.getEarning() * generator.getQuantity();

                // Sincronización sobre el juego para evitar inconsistencias de datos
                synchronized (game) {
                    game.addMoney(production);
                    gameController.addAutogen(production);
                    double currentMoney = game.getMoney();

                    // Actualización de la UI
                    SwingUtilities.invokeLater(() -> {
                        gameView.updateCoffeeCount((int) currentMoney);
                    });
                }
            } catch (InterruptedException e) {
                // Interrupción segura del hilo para finalizar el ciclo de producción
                running = false;
            }
        }
    }

    /**
     * Detiene la ejecución del generador de forma controlada.
     */
    public void stopGenerator() {
        this.running = false;
        this.interrupt();
    }
}