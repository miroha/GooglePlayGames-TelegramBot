package ru.miroha.bot.handler.message;

import org.springframework.stereotype.Component;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import ru.miroha.bot.BotCondition;
import ru.miroha.bot.keyboard.ReplyKeyboardMarkupBuilder;
import ru.miroha.util.Emoji;
import ru.miroha.service.ReplyMessageService;

@Component
public class StartMessageHandler implements MessageHandler {

    private final ReplyMessageService replyMessageService;

    public StartMessageHandler(ReplyMessageService replyMessageService) {
        this.replyMessageService = replyMessageService;
    }

    @Override
    public boolean canHandle(BotCondition botCondition) {
        return botCondition.equals(BotCondition.MAIN_MENU);
    }

    @Override
    public SendMessage handle(Message message) {
        if (message.getText().equals("/start")) {
            return getMainMenu(message.getChatId());
        }
        else {
            return replyMessageService.getTextMessage(message.getChatId(), "Такой команды я не знаю " + Emoji.EYES);
        }
    }

    private SendMessage getMainMenu(Long chatId) {
        return ReplyKeyboardMarkupBuilder.create(chatId)
                .setText("Добро пожаловать! "
                        + "\n\nЧтобы воспользоваться моим функционалом, нажмите нужную кнопку на появившейся клавиатуре. "
                        + Emoji.POINT_DOWN)
                .row()
                .button("Поиск по названию")
                .endRow()
                .row()
                .button("Поиск по ссылке")
                .endRow()
                .row()
                .button("Помощь")
                .endRow()
                .build();
    }

}