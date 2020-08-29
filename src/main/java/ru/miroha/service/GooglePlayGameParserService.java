package ru.miroha.service;

import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import ru.miroha.model.GooglePlayGame;
import ru.miroha.parser.googleplay.GooglePlayGameParser;
import ru.miroha.parser.googleplay.connection.GooglePlayConnection;
import ru.miroha.parser.googleplay.connection.InvalidGooglePlayGameUrlException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Additional service for {@link GooglePlayGameParser}.
 *
 * Provides methods that allow to fill all parsed information about requested game into one {@link GooglePlayGame} object.
 */
@Service
public class GooglePlayGameParserService {

    private final GooglePlayGameParser parser;

    private final GooglePlayConnection googlePlayConnection;

    public GooglePlayGameParserService(GooglePlayGameParser parser, GooglePlayConnection googlePlayConnection) {
        this.parser = parser;
        this.googlePlayConnection = googlePlayConnection;
    }

    public GooglePlayGame getGameByUrl(String URL) throws InvalidGooglePlayGameUrlException, IOException, URISyntaxException {
        Document htmlDocument = getHtmlDocument(URL);
        return GooglePlayGame.builder()
                .id(getGameId(URL))
                .title(parser.getTitle(htmlDocument))
                .genre(parser.getGenre(htmlDocument))
                .overview(parser.getOverview(htmlDocument))
                .price(parser.getPrice(htmlDocument))
                .lastUpdate(parser.getLastUpdate(htmlDocument))
                .recentChanges(parser.getRecentChanges(htmlDocument))
                .apkSize(parser.getInstallationFileSize(htmlDocument))
                .currentVersion(parser.getCurrentVersion(htmlDocument))
                .requirements(parser.getRequirements(htmlDocument))
                .iap(parser.getIAP(htmlDocument))
                .averageRating(parser.getAverageRating(htmlDocument))
                .downloads(parser.getDownloads(htmlDocument))
                .developerName(parser.getDeveloperName(htmlDocument))
                .developerEmail(parser.getDeveloperContacts(htmlDocument))
                .addedToLibrary(LocalDate.now())
                .url(URL)
                .image(parser.getImagePreview(htmlDocument))
                .build();
    }

    public Document getHtmlDocument(String URL) throws InvalidGooglePlayGameUrlException, IOException, URISyntaxException {
        Connection connection = googlePlayConnection.connect(URL, "ru", "RU");
        return connection.get();
    }

    /**
     * Returns {@link GooglePlayGame} package name (unique id) from {@link URL} query part.
     */
    public String getGameId(String url) throws MalformedURLException {
        Map<String, String> params = Arrays.stream(new URL(url).getQuery().split("&"))
                .map(s -> s.split("="))
                .collect(Collectors.toMap(k -> k[0], v -> v.length > 1 ? v[1] : ""));
        return params.get("id");
    }

}
