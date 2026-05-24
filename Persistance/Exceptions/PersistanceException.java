package Persistance.Exceptions;

/**
 * Excepción no verificada que indica un fallo crítico en la capa de persistencia.
 */
public class PersistanceException extends RuntimeException {

    /**
     * Construye una nueva excepción de persistencia con la causa original.
     *
     * @param cause La causa raíz del fallo técnico
     */
    public PersistanceException(Throwable cause) {
        super(cause);
    }
}