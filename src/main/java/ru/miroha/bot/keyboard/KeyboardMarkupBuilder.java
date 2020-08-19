package ru.miroha.bot.keyboard;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

/**
 * Special telegram keyboards builder.
 *
 * @author Pavel Mironov
 * @version 1.0
 */
public interface KeyboardMarkupBuilder {

    void setChatId(Long chatId);

    KeyboardMarkupBuilder setText(String text);

    KeyboardMarkupBuilder row();

    KeyboardMarkupBuilder endRow();

    SendMessage build();

}
