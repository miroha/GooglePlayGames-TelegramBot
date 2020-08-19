package ru.miroha.scraper.googleplay.connection;

/**
 * Thrown to indicate that a URL doesn't apply to <a href="https://play.google.com/store/apps/category/GAME">Google Play Games</a>
 *
 * @author Pavel Mironov
 * @version 1.0
 */
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
