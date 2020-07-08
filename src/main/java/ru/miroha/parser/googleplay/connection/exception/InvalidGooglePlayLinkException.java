package ru.miroha.parser.googleplay.connection.exception;

public class InvalidGooglePlayLinkException extends Exception {
    public InvalidGooglePlayLinkException() {
    }

    public InvalidGooglePlayLinkException(String message) {
        super(message);
    }

    public InvalidGooglePlayLinkException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidGooglePlayLinkException(Throwable cause) {
        super(cause);
    }
}
