package ru.miroha.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import ru.miroha.model.GooglePlayGame;

import java.io.IOException;

/**
 * Allows to serialize {@link GooglePlayGame} as a byte array.
 * This byte array will be used to send JSON document to user without saving it to disk.
 */
@Service
public class JsonService {

    private final ObjectMapper objectMapper;

    public JsonService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public byte [] toJson(GooglePlayGame googlePlayGame) throws IOException {
        return objectMapper.writeValueAsBytes(googlePlayGame);
    }

    /**
     * Generates unique JSON filename from {@link GooglePlayGame} package name.
     * Example: com.happyvolcano.games.thealmostgone.json
     */
    public String getFileName(GooglePlayGame googlePlayGame) {
        return googlePlayGame.getId() + ".json";
    }

}
