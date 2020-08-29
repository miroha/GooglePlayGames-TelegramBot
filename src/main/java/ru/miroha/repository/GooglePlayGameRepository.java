package ru.miroha.repository;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import ru.miroha.model.GooglePlayGame;

import java.util.List;

/**
 * Additional level of abstraction over data access.
 *
 * Stores {@link GooglePlayGame} objects to database and manages them.
 */
@Repository
public interface GooglePlayGameRepository extends MongoRepository<GooglePlayGame, String> {

    GooglePlayGame findByTitle(String title);

    List<GooglePlayGame> findByTitleContainsIgnoreCase(String title);

    @Aggregation("{$sample: {size: ?0} }")
    List<GooglePlayGame> findRandomGames(Long number);

}
