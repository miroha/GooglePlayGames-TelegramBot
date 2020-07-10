package ru.miroha.bot;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import ru.miroha.bot.handler.BotConditionHandler;
import ru.miroha.bot.handler.callbackquery.CallbackQueryHandler;
import ru.miroha.service.ReplyMessageService;
import ru.miroha.service.TelegramUpdateService;

import java.io.Serializable;

@Slf4j
@Component
public class UpdateReceiver {

    private final BotConditionHandler botConditionHandler;

    private final CallbackQueryHandler callbackQueryHandler;

    private final BotConditionContext botConditionContext;

    private final ReplyMessageService replyMessageService;

    private final TelegramUpdateService updateService;

    public UpdateReceiver(BotConditionHandler botConditionHandler,
                          CallbackQueryHandler callbackQueryHandler,
                          BotConditionContext botConditionContext,
                          ReplyMessageService replyMessageService,
                          TelegramUpdateService updateService) {
        this.botConditionHandler = botConditionHandler;
        this.callbackQueryHandler = callbackQueryHandler;
        this.botConditionContext = botConditionContext;
        this.replyMessageService = replyMessageService;
        this.updateService = updateService;
    }

    public PartialBotApiMethod<? extends Serializable> handleUpdate(Update update) {
        if (updateService.hasTextMessage(update)) {
            BotCondition botCondition = defineBotCondition(update);
            log.info("Message from: {}; " +
                            "chat id: {};  " +
                            "text: {}; " +
                            "bot condition: {}",
                    updateService.getUserName(update),
                    updateService.getChatId(update),
                    updateService.getInputUserData(update),
                    botCondition);

            return botConditionHandler.handleTextMessageByCondition(update.getMessage(), botCondition);
        }
        else if (updateService.hasCallbackQuery(update)) {
            log.info("CallbackQuery from: {}; " +
                            "data: {}; " +
                            "message id: {}",
                    updateService.getUserName(update),
                    updateService.getInputUserData(update),
                    updateService.getMessageId(update));

            return callbackQueryHandler.handleCallbackQuery(update.getCallbackQuery());
        }
        else {
            log.error("Unsupported request from: {}; " +
                            "Message type: {}",
                    updateService.getUserName(update),
                    updateService.getMessageType(update.getMessage()));

            return replyMessageService.getTextMessage(updateService.getChatId(update), "Я могу принимать только текстовые сообщения!");
        }
    }

    private BotCondition defineBotCondition(Update update) {
        Integer userId = updateService.getUserId(update);
        String userTextMessage = updateService.getInputUserData(update);
        BotCondition botCondition;

        switch (userTextMessage) {
            case "/start":
                botCondition = BotCondition.MAIN_MENU;
                break;
            case "Помощь":
                botCondition = BotCondition.HELP;
                break;
            case "Поиск по ссылке":
                botCondition = BotCondition.REQUEST_BY_URL;
                break;
            case "Поиск по названию":
                botCondition = BotCondition.IN_LIBRARY;
                break;
            default:
                botCondition = botConditionContext.getCurrentBotConditionForUserById(userId);
        }
        botConditionContext.setCurrentBotConditionForUserWithId(userId, botCondition);
        return botCondition;
    }

}