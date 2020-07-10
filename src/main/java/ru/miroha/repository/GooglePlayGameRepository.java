package ru.miroha.repository;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import ru.miroha.model.GooglePlayGame;

import java.util.List;

@Repository
public interface GooglePlayGameRepository extends MongoRepository<GooglePlayGame, String> {

    GooglePlayGame findByTitleIgnoreCase(String gameTitle);

    List<GooglePlayGame> findByTitleContainsIgnoreCase(String gameTitle);

    @Aggregation("{$sample: {size: ?0} }")
    List<GooglePlayGame> findRandomGames(Long quantity);

}
