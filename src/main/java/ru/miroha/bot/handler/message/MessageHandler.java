package ru.miroha.bot.handler.message;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.miroha.bot.BotCondition;

/**
 * Handles incoming {@link Message} by current bot condition.
 */
public interface MessageHandler {

    boolean canHandle(BotCondition botCondition);

    BotApiMethod<Message> handle(Message message);

}
