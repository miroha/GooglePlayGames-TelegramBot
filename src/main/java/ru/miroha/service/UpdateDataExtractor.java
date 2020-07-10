package ru.miroha.service;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface UpdateDataExtractor {

    Long getChatId(Update update);

    String getUserName(Update update);

    Integer getUserId(Update update);

    String getInputUserData(Update update);

    Integer getMessageId(Update update);

}
