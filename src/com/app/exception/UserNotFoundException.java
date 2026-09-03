// Paquete: com.app.exception
package com.app.exception;

/**
 * Excepción de dominio. Se lanza cuando un usuario buscado no existe en el repositorio.
 */
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
