package ru.miroha.service;

import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import ru.miroha.model.GooglePlayGame;
import ru.miroha.scraper.googleplay.GooglePlayGameScraper;
import ru.miroha.scraper.googleplay.connection.GooglePlayConnection;
import ru.miroha.scraper.googleplay.connection.InvalidGooglePlayGameUrlException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Additional service for {@link GooglePlayGameScraper}.
 *
 * Provides methods that allow to fill all parsed information about requested game into one {@link GooglePlayGame} object.
 *
 * @author Pavel Mironov
 * @version 1.0
 */
@Service
public class GooglePlayGameScraperService {

    private final GooglePlayGameScraper scraper;

    public GooglePlayGameScraperService(GooglePlayGameScraper scraper) {
        this.scraper = scraper;
    }

    public GooglePlayGame getGameByUrl(String URL) throws InvalidGooglePlayGameUrlException, IOException {
        Document htmlDocument = getHtmlDocument(URL);
        return GooglePlayGame.builder()
                .id(getGameId(URL))
                .title(scraper.getTitle(htmlDocument))
                .genre(scraper.getGenre(htmlDocument))
                .price(scraper.getPrice(htmlDocument))
                .lastUpdate(scraper.getLastUpdate(htmlDocument))
                .recentChanges(scraper.getRecentChanges(htmlDocument))
                .apkSize(scraper.getInstallationFileSize(htmlDocument))
                .currentVersion(scraper.getCurrentVersion(htmlDocument))
                .requirements(scraper.getRequirements(htmlDocument))
                .iap(scraper.getIAP(htmlDocument))
                .averageRating(scraper.getAverageRating(htmlDocument))
                .devEmail(scraper.getDeveloperContacts(htmlDocument))
                .addedToLibrary(LocalDate.now())
                .URL(URL)
                .image(scraper.getImagePreview(htmlDocument))
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

}
