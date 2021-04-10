package ru.miroha.service.bot.handler;

/**
 * Thrown to indicate that no handler was found for current bot condition.
 */
public class NoHandlerFoundException extends Exception {

    public NoHandlerFoundException() {
    }

    public NoHandlerFoundException(String message) {
        super(message);
    }

    public NoHandlerFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoHandlerFoundException(Throwable cause) {
        super(cause);
    }

}
