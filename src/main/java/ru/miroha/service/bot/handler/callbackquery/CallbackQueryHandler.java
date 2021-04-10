package ru.miroha.service.bot.handler.callbackquery;

import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.io.Serializable;

/**
 * Handles callback queries from a callback buttons in an inline keyboard.
 */
public interface CallbackQueryHandler {

    PartialBotApiMethod<? extends Serializable> handleCallbackQuery(CallbackQuery callbackQuery);

}
