package ru.miroha.service;

import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import ru.miroha.model.GooglePlayGame;
import ru.miroha.parser.GameParser;
import ru.miroha.parser.googleplay.connection.GooglePlayConnection;
import ru.miroha.parser.googleplay.connection.exception.InvalidGooglePlayLinkException;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GooglePlayGameService {

    private GameParser gameParser;

    public GooglePlayGameService(GameParser gameParser) {
        this.gameParser = gameParser;
    }

    public GooglePlayGame getGooglePlayGame(String URL) throws InvalidGooglePlayLinkException, IOException, URISyntaxException {
        Document htmlDocument = getWebDocument(URL);
        return GooglePlayGame.builder()
                .id(getAppId(URL))
                .title(gameParser.parseTitle(htmlDocument))
                .genre(gameParser.parseGenre(htmlDocument))
                .price(gameParser.parsePrice(htmlDocument))
                .dateOfLastUpdate(gameParser.parseDateOfLastUpdate(htmlDocument))
                .apkSize(gameParser.parseInstallationFileSize(htmlDocument))
                .currentVersion(gameParser.parseVersion(htmlDocument))
                .requirements(gameParser.parseRequirements(htmlDocument))
                .iap(gameParser.parseIAP(htmlDocument))
                .averageRating(gameParser.parseRating(htmlDocument))
                .devEmail(gameParser.parseContacts(htmlDocument))
                .addedToLibrary(LocalDate.now())
                .URL(URL)
                .pictureURL(gameParser.parseGamePicture(htmlDocument))
                .build();
    }

    private Document getWebDocument(String URL) throws InvalidGooglePlayLinkException, IOException, URISyntaxException {
        return GooglePlayConnection.connectToGooglePlay(URL).get();
    }

    private String getAppId(String URL) throws URISyntaxException {
        URI uri = new URI(URL);
        Map<String, String> params = Arrays.stream(uri.getQuery().split("&"))
                .map(s -> s.split("="))
                .collect(Collectors.toMap(k -> k[0], v -> v.length > 1 ? v[1] : ""));
        return params.get("id");
    }
}