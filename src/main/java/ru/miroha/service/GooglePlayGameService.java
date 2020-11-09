package ru.miroha.service;

import org.springframework.stereotype.Service;

import ru.miroha.model.GooglePlayGame;
import ru.miroha.repository.GooglePlayGameRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * This is a layer to interact with repository {@link GooglePlayGameRepository} layer.
 */
@Service
public class GooglePlayGameService {

    private final GooglePlayGameRepository googlePlayGameRepository;

    public GooglePlayGameService(GooglePlayGameRepository googlePlayGameRepository) {
        this.googlePlayGameRepository = googlePlayGameRepository;
    }

    public void saveToLibrary(GooglePlayGame googlePlayGame) {
        googlePlayGameRepository.save(googlePlayGame);
    }

    public GooglePlayGame getGameByTitle(String title) throws NoSuchGooglePlayGameFoundException {
        Optional<GooglePlayGame> googlePlayGame = googlePlayGameRepository.findByTitle(title);
        return googlePlayGame.orElseThrow(
                () -> new NoSuchGooglePlayGameFoundException("Game was not found: " + title)
        );
    }

    public List<GooglePlayGame> getAllLibrary() {
        return googlePlayGameRepository.findAll();
    }

    public List<GooglePlayGame> findListOfGamesByTitle(String title) {
        List<GooglePlayGame> googlePlayGames = googlePlayGameRepository.findByTitleContainsIgnoreCase(title);
        return googlePlayGames.isEmpty()
                ? Collections.emptyList()
                : googlePlayGames;
    }

    public Long getLibrarySize() {
        return googlePlayGameRepository.count();
    }

    public List<GooglePlayGame> getRandomGames(Long number) {
        return googlePlayGameRepository.findRandomGames(number);
    }

}
