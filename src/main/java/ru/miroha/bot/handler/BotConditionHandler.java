package ru.miroha.bot.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import ru.miroha.bot.BotCondition;
import ru.miroha.bot.handler.message.MessageHandler;
import ru.miroha.service.telegram.ReplyMessageService;

import java.util.List;

/**
 * Redirects incoming {@link Message} to specific handlers depending on current bot condition.
 * If suitable handler was not found, than thrown {@link NoHandlerFoundException}.
 */
@Slf4j
@Component
public class BotConditionHandler {

    private final List<MessageHandler> messageHandlers;

    private final ReplyMessageService replyMessageService;

    public BotConditionHandler(List<MessageHandler> messageHandlers, ReplyMessageService replyMessageService) {
        this.messageHandlers = messageHandlers;
        this.replyMessageService = replyMessageService;
    }

    public BotApiMethod<Message> handleTextMessageByCondition(Message message, BotCondition botCondition) {
        MessageHandler messageHandler;
        try {
             messageHandler = messageHandlers.stream()
                    .filter(m -> m.canHandle(botCondition))
                    .findAny()
                    .orElseThrow(NoHandlerFoundException::new);
        } catch (NoHandlerFoundException e) {
            log.error("No handler was found for current bot condition: {}", botCondition);
            return replyMessageService.getTextMessage(message.getChatId(), "Невозможно обработать запрос.");
        }
        return messageHandler.handle(message);
    }

}

