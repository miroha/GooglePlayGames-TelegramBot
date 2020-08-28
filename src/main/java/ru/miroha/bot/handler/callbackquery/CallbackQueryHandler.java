package ru.miroha.bot.handler.callbackquery;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import ru.miroha.model.GooglePlayGame;
import ru.miroha.service.GooglePlayGameService;
import ru.miroha.service.JsonService;
import ru.miroha.util.Emoji;
import ru.miroha.service.telegram.ReplyMessageService;

import java.io.*;

/**
 * Handles callback queries from a callback buttons in an inline keyboard.
 *
 * @author Pavel Mironov
 * @version 1.0
 */
@Slf4j
@Component
public class CallbackQueryHandler {

    private final GooglePlayGameService googlePlayGameService;

    private final ReplyMessageService replyMessageService;

    private final JsonService jsonService;

    public CallbackQueryHandler(GooglePlayGameService googlePlayGameService,
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
        GooglePlayGame game = googlePlayGameService.getGameByTitle(title);

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
                    InputStream inputStream = new ByteArrayInputStream(jsonService.toJson(game));
                    String filename = jsonService.getFileName(game);
                    return replyMessageService.getMessageWithDocument(chatId, filename, inputStream);
                } catch (IOException e) {
                    log.error("Failed attempt to get JSON file: {}", e.toString());
                }
            default:
                return replyMessageService.getTextMessage(chatId, "Не удалось получить информацию об игре.");
        }

    }
}
