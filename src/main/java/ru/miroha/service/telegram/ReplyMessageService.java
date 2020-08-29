package ru.miroha.service.telegram;

import org.springframework.stereotype.Service;

import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

import ru.miroha.model.GooglePlayGame;

import java.io.InputStream;

/**
 * Contains various methods for building different type of telegram messages, such as:
 * text message, pop-up notification, edited text message, text message with image, document.
 *
 * Markdown feature should be disabled to prevent unexpected errors: information about requested game may contains
 * symbols that probably cannot be parsed. This case can throws unexpected {@link org.telegram.telegrambots.meta.exceptions.TelegramApiException}.
 */
@Service
public class ReplyMessageService {

    public SendMessage getTextMessage(Long chatId, String text) {
        return new SendMessage()
                .enableMarkdown(false)
                .setChatId(chatId)
                .setText(text);
    }

    /**
     * Uses as answers to callback queries {@link org.telegram.telegrambots.meta.api.objects.CallbackQuery}.
     */
    public AnswerCallbackQuery getPopUpAnswer(String callbackId, String text) {
        return new AnswerCallbackQuery()
                .setCallbackQueryId(callbackId)
                .setText(text)
                .setShowAlert(false);
    }

    /**
     * Uses to hide keyboard {@link org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup}.
     */
    public EditMessageText getEditedTextMessage(Long chatId, Integer messageId, String text) {
        return new EditMessageText()
                .setChatId(chatId)
                .setMessageId(messageId)
                .setText(text);
    }

    public SendPhoto getMessageWithImage(Long chatId, GooglePlayGame googlePlayGame) {
        return new SendPhoto()
                .setChatId(chatId)
                .setPhoto(googlePlayGame.getImage())
                .setCaption(googlePlayGame.toString());
    }

    /**
     * Uses to send information about {@link GooglePlayGame} in JSON file.
     */
    public SendDocument getMessageWithDocument(Long chatId, String document, InputStream inputStream) {
        return new SendDocument()
                .setChatId(chatId)
                .setDocument(document, inputStream);
    }

}

