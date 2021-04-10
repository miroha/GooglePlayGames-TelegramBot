package ru.miroha.service.bot.handler.callbackquery;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import ru.miroha.model.GooglePlayGame;
import ru.miroha.service.GooglePlayGameService;
import ru.miroha.service.JsonService;
import ru.miroha.service.NoSuchGooglePlayGameFoundException;
import ru.miroha.util.Emoji;
import ru.miroha.service.telegram.ReplyMessageService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;


/**
 * Returns information about {@link GooglePlayGame} in different forms: pop-up notification, text message, JSON.
 */
@Slf4j
@Component
public class GooglePlayGameQueryHandler implements CallbackQueryHandler {

    private final GooglePlayGameService googlePlayGameService;

    private final ReplyMessageService replyMessageService;

    private final JsonService jsonService;

    public GooglePlayGameQueryHandler(GooglePlayGameService googlePlayGameService,
                                      ReplyMessageService replyMessageService,
                                      JsonService jsonService) {
        this.googlePlayGameService = googlePlayGameService;
        this.replyMessageService = replyMessageService;
        this.jsonService = jsonService;
    }

    public PartialBotApiMethod<? extends Serializable> handleCallbackQuery(CallbackQuery callbackQuery) {
        String callBackData = callbackQuery.getData();
        String callBackId = callbackQuery.getId();
        Integer messageId = callbackQuery.getMessage().getMessageId();
        Long chatId = callbackQuery.getMessage().getChatId();

        String title = callBackData.substring(callBackData.indexOf(' ') + 1); //<skip callback data name and extract game title
        GooglePlayGame game;

        try {
            game = googlePlayGameService.getGameByTitle(title);
        } catch (NoSuchGooglePlayGameFoundException e) {
            log.error(e.getMessage());
            return replyMessageService.getTextMessage(chatId, "Такой игры нет в библиотеке!");
        }
        switch (callBackData.split("\\s+")[0]) {
            case "/price":
                return replyMessageService.getPopUpAnswer(callBackId, game.getPrice());
            case "/updated":
                return replyMessageService.getPopUpAnswer(callBackId, game.getLastUpdate());
            case "/version":
                return replyMessageService.getPopUpAnswer(callBackId, game.getCurrentVersion());
            case "/requirements":
                return replyMessageService.getPopUpAnswer(callBackId, game.getRequirements());
            case "/iap":
                return replyMessageService.getPopUpAnswer(callBackId, game.getIap());
            case "/size":
                return replyMessageService.getPopUpAnswer(callBackId, game.getApkSize());
            case "/close":
                return replyMessageService.getEditedTextMessage(chatId, messageId, Emoji.CLOSE.toString());
            case "/all":
                return replyMessageService.getMessageWithImage(chatId, game);
            case "/json":
                try {
                    InputStream inputStream = new ByteArrayInputStream(jsonService.toJson(game)); //<don't save to disk
                    String filename = jsonService.getFileName(game);
                    return replyMessageService.getMessageWithDocument(chatId, filename, inputStream);
                } catch (IOException e) {
                    log.error("Failed attempt to get JSON file: {}", e.toString());
                    return replyMessageService.getTextMessage(chatId, "Не удалось получить JSON файл.");
                }
            default:
                return replyMessageService.getTextMessage(chatId, "Не удалось получить информацию об игре.");
        }

    }
}
