package es.hostess.app.exceptions;

/**
 * Custom exception for the application.
 */
public class HostessGeneticException extends RuntimeException {

    /**
     * Exception to pass the message.
     * @param message The error message
     */
    public HostessGeneticException(String message) {
        super(message);
    }

    /**
     * Exception to pass the message and the {@link Throwable}.
     * @param message The error message
     * @param throwable The {@link Throwable thrown}.
     */
    public HostessGeneticException(String message, Throwable throwable) {
        super(message, throwable);
        throwable.printStackTrace();
    }
}
