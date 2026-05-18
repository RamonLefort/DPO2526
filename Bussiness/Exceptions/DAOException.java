package Bussiness.Exceptions;

/**
 * Excepción de negocio pura que indica un fallo en el acceso o guardado de datos.
 */
public class DAOException extends Exception {
    public DAOException(Throwable cause) {
        super(cause);
    }
}