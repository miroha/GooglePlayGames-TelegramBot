package ru.miroha.service.telegram;

import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Retrieves information from complex {@link org.telegram.telegrambots.meta.api.objects.Update} object by merging existing methods.
 */
public interface TelegramUpdateExtractor {

    Long getChatId(Update update);

    String getUserName(Update update);

    Integer getUserId(Update update);

    String getInputUserData(Update update);

    Integer getMessageId(Update update);

}
