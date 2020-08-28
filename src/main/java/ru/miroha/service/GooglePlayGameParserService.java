package ru.miroha.service;

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
 *
 * @author Pavel Mironov
 * @version 1.0
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
                .price(parser.getPrice(htmlDocument))
                .lastUpdate(parser.getLastUpdate(htmlDocument))
                .recentChanges(parser.getRecentChanges(htmlDocument))
                .apkSize(parser.getInstallationFileSize(htmlDocument))
                .currentVersion(parser.getCurrentVersion(htmlDocument))
                .requirements(parser.getRequirements(htmlDocument))
                .iap(parser.getIAP(htmlDocument))
                .averageRating(parser.getAverageRating(htmlDocument))
                .developerName(parser.getDeveloperName(htmlDocument))
                .developerEmail(parser.getDeveloperContacts(htmlDocument))
                .addedToLibrary(LocalDate.now())
                .URL(URL)
                .image(parser.getImagePreview(htmlDocument))
                .build();
    }

    public Document getHtmlDocument(String URL) throws InvalidGooglePlayGameUrlException, IOException, URISyntaxException {
        return googlePlayConnection.connect(URL, "ru", "RU").get();
    }

    public String getGameId(String url) throws MalformedURLException {
        Map<String, String> params = Arrays.stream(new URL(url).getQuery().split("&"))
                .map(s -> s.split("="))
                .collect(Collectors.toMap(k -> k[0], v -> v.length > 1 ? v[1] : ""));
        return params.get("id");
    }

}
