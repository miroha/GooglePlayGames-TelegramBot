package ru.miroha.bot.handler;

/**
 * Thrown to indicate that no handler was found for current bot condition.
 */
public class NoHandlerFoundException extends Exception {

    public NoHandlerFoundException() {
    }

    public NoHandlerFoundException(String message) {
        super(message);
    }
}
