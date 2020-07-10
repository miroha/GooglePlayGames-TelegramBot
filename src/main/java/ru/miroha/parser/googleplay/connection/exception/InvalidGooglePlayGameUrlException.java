package ru.miroha.parser.googleplay.connection.exception;

public class InvalidGooglePlayGameUrlException extends Exception {

    public InvalidGooglePlayGameUrlException() {

    }

    public InvalidGooglePlayGameUrlException(String message) {
        super(message);
    }

    public InvalidGooglePlayGameUrlException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidGooglePlayGameUrlException(Throwable cause) {
        super(cause);
    }

}
