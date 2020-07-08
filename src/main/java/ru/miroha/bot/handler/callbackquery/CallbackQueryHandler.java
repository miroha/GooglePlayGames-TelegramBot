package ru.miroha.bot.handler.callbackquery;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import ru.miroha.model.GooglePlayGame;
import ru.miroha.repository.GooglePlayGameRepository;
import ru.miroha.service.EmojiService;
import ru.miroha.service.ReplyMessageService;

import java.io.Serializable;

@Component
public class CallbackQueryHandler {

    private final GooglePlayGameRepository googlePlayGameRepository;
    private final ReplyMessageService replyMessageService;

    public CallbackQueryHandler(GooglePlayGameRepository googlePlayGameRepository,
                                ReplyMessageService replyMessageService) {
        this.googlePlayGameRepository = googlePlayGameRepository;
        this.replyMessageService = replyMessageService;
    }

    public PartialBotApiMethod<? extends Serializable> handleCallbackQuery(CallbackQuery callbackQuery){
        String callBackData = callbackQuery.getData();
        String callBackId = callbackQuery.getId();
        Integer messageId = callbackQuery.getMessage().getMessageId();
        Long chatId = callbackQuery.getMessage().getChatId();
        String gameTitle = callBackData.substring(callBackData.indexOf(' ') + 1);
        GooglePlayGame game = googlePlayGameRepository.findByTitleIgnoreCase(gameTitle);

        switch (callBackData.split("\\s+")[0]) {
            case "/price":
                return replyMessageService.getPopUpAnswer(callBackId, game.getPrice());
            case "/updated":
                return replyMessageService.getPopUpAnswer(callBackId, game.getDateOfLastUpdate().toString());
            case "/version":
                return replyMessageService.getPopUpAnswer(callBackId, game.getCurrentVersion());
            case "/requirements":
                return replyMessageService.getPopUpAnswer(callBackId, game.getRequirements());
            case "/iap":
                return replyMessageService.getPopUpAnswer(callBackId, game.getIap());
            case "/size":
                return replyMessageService.getPopUpAnswer(callBackId, game.getApkSize());
            case "/close":
                return replyMessageService.getEditedMessage(chatId, messageId, EmojiService.HIDE.toString());
            case "/all":
                return replyMessageService.getMessageWithPicture(chatId, game);
            default:
                return replyMessageService.getTextMessage(chatId, "Не удалось получить информацию об игре.");
        }
    }
}
