package ru.miroha.service;

import org.jsoup.nodes.Document;

import org.springframework.stereotype.Service;

import ru.miroha.model.GooglePlayGame;
import ru.miroha.parser.GameParser;
import ru.miroha.parser.googleplay.connection.GooglePlayConnection;
import ru.miroha.parser.googleplay.connection.exception.InvalidGooglePlayGameUrlException;
import ru.miroha.repository.GooglePlayGameRepository;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GooglePlayGameService {

    private final GameParser gameParser;

    private final GooglePlayGameRepository googlePlayGameRepository;

    public GooglePlayGameService(GameParser gameParser, GooglePlayGameRepository googlePlayGameRepository) {
        this.gameParser = gameParser;
        this.googlePlayGameRepository = googlePlayGameRepository;
    }

    public GooglePlayGame getGameByUrl(String URL) throws InvalidGooglePlayGameUrlException, IOException {
        Document htmlDocument = getHtmlDocument(URL);
        return GooglePlayGame.builder()
                .id(getGameId(URL))
                .title(gameParser.parseTitle(htmlDocument))
                .genre(gameParser.parseGenre(htmlDocument))
                .price(gameParser.parsePrice(htmlDocument))
                .lastUpdate(gameParser.parseDateOfLastUpdate(htmlDocument))
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

    private Document getHtmlDocument(String URL) throws InvalidGooglePlayGameUrlException, IOException {
        return GooglePlayConnection.connectToGooglePlay(URL).get();
    }

    private String getGameId(String URL) throws MalformedURLException {
        var url = new URL(URL);
        Map<String, String> params = Arrays.stream(url.getQuery().split("&"))
                .map(s -> s.split("="))
                .collect(Collectors.toMap(k -> k[0], v -> v.length > 1 ? v[1] : ""));
        return params.get("id");
    }

    public void saveGame(GooglePlayGame googlePlayGame) {
        googlePlayGameRepository.save(googlePlayGame);
    }

    public GooglePlayGame getGameByTitle(String title) {
        return googlePlayGameRepository.findByTitle(title);
    }

    public Long getNumberOfGamesInLibrary() {
        return googlePlayGameRepository.count();
    }

    public List<GooglePlayGame> getRandomGames(Long quantity) {
        return googlePlayGameRepository.findRandomGames(quantity);
    }

    public List<GooglePlayGame> getGamesByTitle(String title) {
        return googlePlayGameRepository.findByTitleContainsIgnoreCase(title);
    }

}
