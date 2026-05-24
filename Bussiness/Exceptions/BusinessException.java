package Bussiness.Exceptions;

/**
 * Excepción de negocio pura que indica un fallo en el acceso o guardado de datos.
 */
public class BusinessException extends Exception {
    /**
     * Construye una nueva excepción de negocio con la causa original del fallo.
     *
     * @param cause El error o excepción original que provocó el fallo en
     *              el acceso a los datos.
     */
    public BusinessException(Throwable cause) {
        super(cause);
    }
}