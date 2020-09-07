package ru.miroha.service;

/**
 * Thrown to indicate that no {@link ru.miroha.model.GooglePlayGame} was found.
 */
public class NoSuchGooglePlayGameFoundException extends Exception {

    public NoSuchGooglePlayGameFoundException() {
        super();
    }

    public NoSuchGooglePlayGameFoundException(String message) {
        super(message);
    }

    public NoSuchGooglePlayGameFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuchGooglePlayGameFoundException(Throwable cause) {
        super(cause);
    }

}
