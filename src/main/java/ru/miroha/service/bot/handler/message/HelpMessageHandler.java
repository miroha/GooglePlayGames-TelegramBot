package ru.miroha.service.bot.handler.message;

import org.springframework.stereotype.Component;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import ru.miroha.service.bot.BotCondition;
import ru.miroha.service.telegram.ReplyMessageService;

/**
 * Handles {@link Message} when {@link BotCondition} is {@link BotCondition#HELP}.
 *
 * Informs how the bot works.
 */
@Component
public class HelpMessageHandler implements MessageHandler {

    private final ReplyMessageService replyMessageService;

    public HelpMessageHandler(ReplyMessageService replyMessageService) {
        this.replyMessageService = replyMessageService;
    }

    @Override
    public SendMessage handle(Message message) {
        Long chatId = message.getChatId();
        return replyMessageService.getTextMessage(chatId,
                String.join("\n\n",
                        "Получить информацию об игре в магазине Google Play можно двумя способами:",
                        "1) Отправляете боту ссылку на игру в магазине Google Play (выберите соответствующий пункт меню).",
                        "2) По названию игры (выберите соответствующий пункт меню).",
                        "Второй вариант срабатывает, если указанная игра уже есть в библиотеке бота.",
                        "Приятного пользования!"
                ));
    }

    @Override
    public boolean canHandle(BotCondition botCondition) {
        return botCondition.equals(BotCondition.HELP);
    }

}