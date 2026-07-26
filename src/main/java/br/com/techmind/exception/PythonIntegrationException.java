package br.com.techmind.exception;

/**
 * Exceção utilizada quando ocorre algum problema
 * durante a comunicação entre a API Java
 * e o serviço Python de Inteligência Artificial.
 */
public class PythonIntegrationException extends RuntimeException {

    public PythonIntegrationException(String message) {
        super(message);
    }

    public PythonIntegrationException(String message, Throwable cause) {
        super(message, cause);
    }
}
