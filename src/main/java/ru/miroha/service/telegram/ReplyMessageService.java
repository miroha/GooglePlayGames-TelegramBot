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
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(false);
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        return sendMessage;
    }

    /**
     * Uses as answers to callback queries {@link org.telegram.telegrambots.meta.api.objects.CallbackQuery}.
     */
    public AnswerCallbackQuery getPopUpAnswer(String callbackId, String text) {
        AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
        answerCallbackQuery.setCallbackQueryId(callbackId);
        answerCallbackQuery.setText(text);
        answerCallbackQuery.setShowAlert(false);
        return answerCallbackQuery;
    }

    /**
     * Uses to hide keyboard {@link org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup}.
     */
    public EditMessageText getEditedTextMessage(Long chatId, Integer messageId, String text) {
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(chatId);
        editMessageText.setMessageId(messageId);
        editMessageText.setText(text);
        return editMessageText;
    }

    public SendPhoto getMessageWithImage(Long chatId, GooglePlayGame googlePlayGame) {
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(chatId);
        sendPhoto.setPhoto(googlePlayGame.getImage());
        sendPhoto.setCaption(googlePlayGame.toString());
        return sendPhoto;
    }

    /**
     * Uses to send information about {@link GooglePlayGame} in JSON file.
     */
    public SendDocument getMessageWithDocument(Long chatId, String document, InputStream inputStream) {
        SendDocument sendDocument = new SendDocument();
        sendDocument.setChatId(chatId);
        sendDocument.setDocument(document, inputStream);
        return sendDocument;
    }

}

