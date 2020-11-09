package ru.miroha.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.miroha.model.GooglePlayGame;
import ru.miroha.parser.googleplay.connection.InvalidGooglePlayGameUrlException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Allows to update {@link GooglePlayGame} library everyday on a schedule to keep actual information.
 */
@Service
@Slf4j
public class ScheduledGooglePlayGamesUpdateService {

    private final GooglePlayGameService googlePlayGameService;
    private final GooglePlayGameParserService googlePlayGameParserService;

    public ScheduledGooglePlayGamesUpdateService(GooglePlayGameService googlePlayGameService, GooglePlayGameParserService googlePlayGameParserService) {
        this.googlePlayGameService = googlePlayGameService;
        this.googlePlayGameParserService = googlePlayGameParserService;
    }

    @Scheduled(cron = "${bot.schedule.cron.update}")
    public void updateLibraryOnSchedule() {
        log.info("Scheduled update started.");
        List<GooglePlayGame> library = googlePlayGameService.getAllLibrary();
        library.stream()
                .map(GooglePlayGame::getUrl)
                .forEach(url -> {
                    log.info("Updating information for {}:", url);
                    GooglePlayGame googlePlayGame = null;
                    try {
                        googlePlayGame = googlePlayGameParserService.getGameByUrl(url, "ru", "RU");
                    } catch (InvalidGooglePlayGameUrlException | IOException | URISyntaxException e) {
                        log.error("Error occurred while update information. {}", e.toString());
                    }
                    googlePlayGameService.saveToLibrary(googlePlayGame);
                });
        log.info("Scheduled update finished.");
    }


}
