package ru.miroha.service.bot;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.Serializable;

/**
 * Receives incoming updates using long polling technology.
 */
@Slf4j
@Component
public class GooglePlayGamesTelegramBot extends TelegramLongPollingBot {

    @Getter
    @Value("${bot.username}")
    String botUsername;

    @Getter
    @Value("${bot.token}")
    String botToken;

    private final UpdateReceiver updateReceiver;

    public GooglePlayGamesTelegramBot(UpdateReceiver updateReceiver) {
        this.updateReceiver = updateReceiver;
    }

    @Override
    public void onUpdateReceived(Update update) {
        PartialBotApiMethod<? extends Serializable> responseToUser = updateReceiver.handleUpdate(update);

        if (responseToUser instanceof SendDocument) {
            try {
                execute(
                        (SendDocument) responseToUser);
            }
            catch (TelegramApiException e) {
                log.error("Error occurred while sending message to user: {}", e.getMessage());
            }
        }

        if (responseToUser instanceof SendPhoto) {
            try {
                execute(
                        (SendPhoto) responseToUser);
            } catch (TelegramApiException e) {
                log.error("Error occurred while sending message to user: {}", e.getMessage());
            }
        }

        if (responseToUser instanceof BotApiMethod) {
            try {
                execute(
                        (BotApiMethod<? extends Serializable>) responseToUser);
            } catch (TelegramApiException e) {
                log.error("Error occurred while sending message to user: {}", e.getMessage());
            }
        }

    }

}
