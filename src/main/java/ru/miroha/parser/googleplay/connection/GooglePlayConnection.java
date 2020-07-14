package ru.miroha.parser.googleplay.connection;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import ru.miroha.parser.googleplay.connection.exception.InvalidGooglePlayGameUrlException;

import java.net.MalformedURLException;
import java.net.URL;

public final class GooglePlayConnection {

    public static Connection connectToGooglePlay(String URL) throws InvalidGooglePlayGameUrlException, MalformedURLException {
        final java.net.URL url= new URL(URL);
        if (GooglePlayCorrectURL.isUrlValid(url)) {
            if (!url.getPath().contains("apps")) {
                throw new InvalidGooglePlayGameUrlException("Wrong Google Play category");
            }
            URL = forceToRusLocalization(URL);
            return Jsoup.connect(URL);
        }
        else {
            throw new InvalidGooglePlayGameUrlException("Not Google Play URL");
        }
    }

    private static String forceToRusLocalization(String URL) {
        if (URL.endsWith("&hl=ru")) {
            return URL;
        }
        else {
            if (URL.contains("&hl=")) {
                URL = URL.replace(
                        URL.substring(
                                URL.length()-"&hl=ru".length()), "&hl=ru");
            }
            else {
                URL += "&hl=ru";
            }
        }
        return URL;
    }

    static class GooglePlayCorrectURL {

        private static final String VALID_HOST = "play.google.com";

        private static final String VALID_PROTOCOL = "https";

        private static final int VALID_PORT = -1;

        private static boolean isUrlValid(URL url) {
            return (isHostValid(url) && isProtocolValid(url) && isPortValid(url));
        }

        private static boolean isProtocolValid(URL url) {
            if (url.getProtocol() != null) {
                return url.getProtocol().equals(VALID_PROTOCOL);
            }
            else {
                return false;
            }
        }

        private static boolean isHostValid(URL url) {
            if (url.getHost() != null) {
                return url.getHost().equals(VALID_HOST);
            }
            else {
                return false;
            }
        }

        private static boolean isPortValid(URL url) {
            return url.getPort() == VALID_PORT;
        }

    }

}

