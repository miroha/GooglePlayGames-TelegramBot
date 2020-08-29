package ru.miroha.parser.googleplay.connection;

import org.apache.http.client.utils.URIBuilder;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Provides the connection to <a href="https://play.google.com/store/apps">Google Play</a>.
 */
@Component
public final class GooglePlayConnection {

    /**
     * Typical base part of URL for all apps.
     */
    private static final String BASE_URL = "https://play.google.com/store/apps/details?";

    /**
     * Tries to connect to provided URL.
     * Uses localized version of URL from {@link #getLocalized(Map, String, String)}.
     * Also checks if URL applies to APPS category to prevent from parsing books/music/movies.
     */
    public Connection connect(String URL, String language, String country) throws InvalidGooglePlayGameUrlException, MalformedURLException, URISyntaxException {
        final java.net.URL url = new URL(URL);
        if (GooglePlayCorrectURL.isUrlValid(url)) {
            if (!url.getPath().contains("apps")) {
                throw new InvalidGooglePlayGameUrlException("Wrong Google Play category");
            }
            Map<String, String> params = getParameters(URL);
            URL = getLocalized(params, language, country).toString();
            return Jsoup.connect(URL);
        }
        else {
            throw new InvalidGooglePlayGameUrlException("Not Google Play URL");
        }
    }

    /**
     * Gets localized version of provided URL.
     */
    private URL getLocalized(Map<String, String> params, String language, String country) throws MalformedURLException, URISyntaxException {
        URIBuilder uriBuilder = new URIBuilder(BASE_URL)
                .addParameter("id", params.get("id"))
                .addParameter("hl", language)
                .addParameter("gl", country);
        return uriBuilder.build().toURL();
    }

    /**
     * Returns query parameters.
     */
    private Map<String, String> getParameters(String url) throws MalformedURLException {
        return Arrays.stream(new URL(url).getQuery().split("&"))
                .map(s -> s.split("="))
                .collect(Collectors.toMap(k -> k[0], v -> v.length > 1 ? v[1] : ""));
    }

    /**
     * Checks if provided URL applies to Google Play by host, protocol and port.
     */
    private static class GooglePlayCorrectURL {

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

