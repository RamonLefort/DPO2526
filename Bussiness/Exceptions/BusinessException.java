package Bussiness.Exceptions;

/**
 * Excepción de negocio pura que indica un fallo en el acceso o guardado de datos.
 */
public class BusinessException extends Exception {
    public BusinessException(Throwable cause) {
        super(cause);
    }
}