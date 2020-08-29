package ru.miroha.service.telegram;

import org.springframework.stereotype.Service;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.payments.ShippingQuery;

import java.util.function.Function;

/**
 * Provides various methods to shorten the code and avoid additional verifications every time when you want to know
 * what kind of {@link Update} came. It's just make it easier to work with {@link Update}.
 *
 * <b>NOTE!</b>
 * <p>Supports {@link Message} and {@link CallbackQuery}. Don't use it for {@link ShippingQuery} or another exotic types of {@link Update}.</p>
 */
@Service
public class TelegramUpdateService implements TelegramUpdateExtractor {

    public boolean hasTextMessage(Update update) {
        return (update.hasMessage() && update.getMessage().hasText());
    }

    public boolean hasCallbackQuery(Update update) {
        return (update.hasCallbackQuery());
    }

    private <T> T getUpdateAttribute(Update update,
                                     Function<Message, T> messageFunc,
                                     Function<CallbackQuery, T> callbackQueryFunc)  {
        if (hasTextMessage(update)) {
            return messageFunc.apply(update.getMessage());
        }
        else if (hasCallbackQuery(update)) {
            return callbackQueryFunc.apply(update.getCallbackQuery());
        }
        else {
            return messageFunc.apply(update.getMessage());
        }
    }

    @Override
    public Long getChatId(Update update) {
        return getUpdateAttribute(
                update,
                Message::getChatId,
                callbackQuery -> callbackQuery.getMessage().getChatId()
        );
    }

    @Override
    public String getUserName(Update update) {
        return getUpdateAttribute(
                update,
                message -> message.getFrom().getUserName(),
                callbackQuery -> callbackQuery.getFrom().getUserName()
        );
    }

    @Override
    public Integer getUserId(Update update) {
        return getUpdateAttribute(
                update,
                message -> message.getFrom().getId(),
                callbackQuery -> callbackQuery.getFrom().getId()
        );
    }

    @Override
    public String getInputUserData(Update update) {
        return getUpdateAttribute(
                update,
                Message::getText,
                CallbackQuery::getData
        );
    }

    @Override
    public Integer getMessageId(Update update) {
        return getUpdateAttribute(
                update,
                Message::getMessageId,
                callbackQuery -> callbackQuery.getMessage().getMessageId()
        );
    }

    public String getMessageType(Message message) {
        if (message.hasAudio()) {
            return "Audio";
        }
        else if (message.hasVoice()) {
            return "Voice";
        }
        else if (message.hasPhoto()) {
            return "Photo";
        }
        else if (message.hasLocation()) {
            return "Location";
        }
        else if (message.hasSticker()) {
            return "Sticker";
        }
        else if (message.hasVideo()) {
            return "Video";
        }
        else if (message.hasDocument()) {
            return "Document";
        }
        else if (message.hasAnimation()) {
            return "Animation";
        }
        else return "Unknown type";
    }

}
