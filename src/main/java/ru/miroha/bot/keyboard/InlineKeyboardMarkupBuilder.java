package ru.miroha.bot.keyboard;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.toIntExact;

public class InlineKeyboardMarkupBuilder {

    private Long chatId;
    private String text;

    private List<InlineKeyboardButton> row = null;
    private List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

    private InlineKeyboardMarkupBuilder() {

    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public InlineKeyboardMarkupBuilder setText(String text) {
        this.text = text;
        return this;
    }

    public static InlineKeyboardMarkupBuilder create() {
        return new InlineKeyboardMarkupBuilder();
    }

    public static InlineKeyboardMarkupBuilder create(Long chatId) {
        InlineKeyboardMarkupBuilder builder = new InlineKeyboardMarkupBuilder();
        builder.setChatId(chatId);
        return builder;
    }

    public InlineKeyboardMarkupBuilder row() {
        this.row = new ArrayList<>();
        return this;
    }

    public InlineKeyboardMarkupBuilder button(String text, String callbackData) {
        row.add(new InlineKeyboardButton()
                .setText(text)
                .setCallbackData(callbackData));
        return this;
    }

    public InlineKeyboardMarkupBuilder buttonWithURL(String text, String URL) {
        row.add(new InlineKeyboardButton()
                .setText(text)
                .setUrl(URL));
        return this;
    }

    public InlineKeyboardMarkupBuilder endRow() {
        this.keyboard.add(this.row);
        this.row = null;
        return this;
    }

    public SendMessage build() {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        keyboardMarkup.setKeyboard(keyboard);
        return new SendMessage()
                .setChatId(chatId)
                .setText(text)
                .setReplyMarkup(keyboardMarkup);
    }

    public EditMessageText rebuild(Long messageId) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        keyboardMarkup.setKeyboard(keyboard);
        return new EditMessageText()
                .setChatId(chatId)
                .setMessageId(toIntExact(messageId))
                .setReplyMarkup(keyboardMarkup);
    }
}
