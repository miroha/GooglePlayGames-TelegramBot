package ru.miroha.service;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.stereotype.Service;
import ru.miroha.model.GooglePlayGame;

import java.io.IOException;

@Service
public class JsonService {

    public static ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
            .enable(SerializationFeature.INDENT_OUTPUT);

    public byte [] toJson(GooglePlayGame googlePlayGame) throws IOException {
        return objectMapper.writeValueAsBytes(googlePlayGame);
    }

    public String getFileName(GooglePlayGame googlePlayGame) {
        return googlePlayGame.getId() + ".json";
    }

}
