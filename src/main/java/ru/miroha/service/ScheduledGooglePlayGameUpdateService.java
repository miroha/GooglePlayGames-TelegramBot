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
public class ScheduledGooglePlayGameUpdateService {

    private final GooglePlayGameService googlePlayGameService;
    private final GooglePlayGameParserService googlePlayGameParserService;

    public ScheduledGooglePlayGameUpdateService(GooglePlayGameService googlePlayGameService, GooglePlayGameParserService googlePlayGameParserService) {
        this.googlePlayGameService = googlePlayGameService;
        this.googlePlayGameParserService = googlePlayGameParserService;
    }

    @Scheduled(cron = "${bot.schedule.cron.update}")
    public void updateLibraryOnSchedule() {
        List<GooglePlayGame> library = googlePlayGameService.getAllLibrary();
        log.info("Scheduled update started. Number of games in library: {}", library.size());
        library.stream()
                .map(GooglePlayGame::getUrl)
                .forEach(this::update);
        log.info("Scheduled update finished.");
    }

    private void update(String url) {
        log.info("Updating information for {}:", url);
        GooglePlayGame googlePlayGame;
        try {
            googlePlayGame = googlePlayGameParserService.getGameByUrl(url, "ru", "RU");
        } catch (InvalidGooglePlayGameUrlException | IOException | URISyntaxException e) {
            log.error("Error occurred while update information. {}", e.toString());
            return;
        }
        googlePlayGameService.saveToLibrary(googlePlayGame);
    }


}
