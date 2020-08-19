package ru.miroha.service;

import org.springframework.stereotype.Service;

import ru.miroha.model.GooglePlayGame;
import ru.miroha.repository.GooglePlayGameRepository;

import java.util.List;

/**
 * This is a layer to interact with repository {@link GooglePlayGameRepository} layer.
 *
 * @author Pavel Mironov
 * @version 1.0
 */
@Service
public class GooglePlayGameService {

    private final GooglePlayGameRepository googlePlayGameRepository;

    public GooglePlayGameService(GooglePlayGameRepository googlePlayGameRepository) {
        this.googlePlayGameRepository = googlePlayGameRepository;
    }

    public void save(GooglePlayGame googlePlayGame) {
        googlePlayGameRepository.save(googlePlayGame);
    }

    public GooglePlayGame getGameByTitle(String title) {
        return googlePlayGameRepository.findByTitle(title);
    }

    public List<GooglePlayGame> findByTitle(String title) {
        return googlePlayGameRepository.findByTitleContainsIgnoreCase(title);
    }

    public Long getLibrarySize() {
        return googlePlayGameRepository.count();
    }

    public List<GooglePlayGame> getRandomGames(Long number) {
        return googlePlayGameRepository.findRandomGames(number);
    }

}
