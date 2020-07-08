package ru.miroha.bot.handler.message;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.miroha.bot.BotCondition;

import java.util.List;

@Component
public class TextMessageHandler {

    private final List<MessageHandler> messageHandlers;

    public TextMessageHandler(List<MessageHandler> messageHandlers) {
        this.messageHandlers = messageHandlers;
    }

    public BotApiMethod<Message> handleTextMessage(Message message, BotCondition botCondition) {
        return messageHandlers.stream()
                .filter(h -> h.canHandle(botCondition))
                .findAny()
                .orElseThrow(IllegalArgumentException::new)
                .handle(message);
    }
}

