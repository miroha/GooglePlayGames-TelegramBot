package ru.miroha.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import ru.miroha.model.GooglePlayGame;

@Service
public class ReplyMessageService {

    public SendMessage getTextMessage(Long chatId, String text) {
        return new SendMessage()
                .enableMarkdown(false)
                .setChatId(chatId)
                .setText(text);
    }

    public AnswerCallbackQuery getPopUpAnswer(String callbackId, String text) {
        return new AnswerCallbackQuery()
                .setCallbackQueryId(callbackId)
                .setText(text)
                .setShowAlert(false);
    }

    public EditMessageText getEditedMessage(Long chatId, Integer messageId, String text) {
        return new EditMessageText()
                .setChatId(chatId)
                .setMessageId(messageId)
                .setText(text);
    }

    public SendPhoto getMessageWithPicture(Long chatId, GooglePlayGame googlePlayGame) {
        return new SendPhoto().setChatId(chatId)
                .setPhoto(googlePlayGame.getPictureURL())
                .setCaption(googlePlayGame.toString());
    }
}

