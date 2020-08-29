package ru.miroha.bot.handler;

import org.springframework.stereotype.Component;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import ru.miroha.bot.BotCondition;
import ru.miroha.bot.handler.message.MessageHandler;

import java.util.List;

/**
 * Redirects incoming {@link Message} to specific handlers depending on current bot condition.
 */
@Component
public class BotConditionHandler {

    private final List<MessageHandler> messageHandlers;

    public BotConditionHandler(List<MessageHandler> messageHandlers) {
        this.messageHandlers = messageHandlers;
    }

    public BotApiMethod<Message> handleTextMessageByCondition(Message message, BotCondition botCondition) {
        return messageHandlers.stream()
                .filter(h -> h.canHandle(botCondition))
                .findAny()
                .orElseThrow(()-> new IllegalArgumentException("Invalid bot condition: " + botCondition))
                .handle(message);
    }

}

