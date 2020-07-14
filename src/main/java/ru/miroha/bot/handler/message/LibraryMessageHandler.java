package ru.miroha.bot.handler.message;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import ru.miroha.bot.BotCondition;
import ru.miroha.bot.keyboard.InlineKeyboardMarkupBuilder;
import ru.miroha.model.GooglePlayGame;
import ru.miroha.service.GooglePlayGameService;
import ru.miroha.util.Emoji;
import ru.miroha.service.ReplyMessageService;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class LibraryMessageHandler implements MessageHandler {

    private final ReplyMessageService replyMessageService;

    private final GooglePlayGameService googlePlayGameService;

    public LibraryMessageHandler(ReplyMessageService replyMessageService,
                                 GooglePlayGameService googlePlayGameService) {
        this.replyMessageService = replyMessageService;
        this.googlePlayGameService = googlePlayGameService;
    }

    @Override
    public boolean canHandle(BotCondition botCondition) {
        return botCondition.equals(BotCondition.IN_LIBRARY);
    }

    @Override
    public SendMessage handle(Message message) {
        Long chatId = message.getChatId();
        if (message.getText().equals("Поиск по названию")) {
            return replyMessageService.getTextMessage(chatId, replyByDefault());
        }
        String title = message.getText();
        List<GooglePlayGame> googlePlayGames = googlePlayGameService.getGamesWithSimilarTitle(title);
        if (googlePlayGames.size() > 1) {
            return replyMessageService.getTextMessage(chatId, specifyRequest(googlePlayGames));
        }
        else if (googlePlayGames.size() == 1) {
            return getInlineKeyboard(chatId, googlePlayGames.get(0));
        }
        else {
            log.error("Game {} doesn't exist in library", title);
            return replyMessageService.getTextMessage(chatId, "Такой игры в библиотеке нет!");
        }
    }

    private String specifyRequest(List<GooglePlayGame> games) {
        return String.join("\n\n"
                , "Найденные совпадения:"
                , getSimilarGameTitles(games)
                , "Уточните ваш запрос!");
    }

    private String getSimilarGameTitles(List<GooglePlayGame> games) {
        return games.stream()
                .map(GooglePlayGame::getTitle)
                .collect(Collectors.joining("\n"));
    }

    private SendMessage getInlineKeyboard(Long chatId, GooglePlayGame googlePlayGame) {
        String gameTitle = googlePlayGame.getTitle();
        String URL = googlePlayGame.getURL();
        return InlineKeyboardMarkupBuilder.create(chatId)
                .setText("Вы можете узнать следующую информацию об игре " + gameTitle)
                .row()
                .button("Стоимость " + Emoji.MONEY, "/price " + gameTitle)
                .button("Обновлено " + Emoji.UPDATED, "/updated " + gameTitle)
                .button("Версия " + Emoji.VERSION, "/version " + gameTitle)
                .endRow()
                .row()
                .button("Требования " + Emoji.REQUIREMENTS, "/requirements " + gameTitle)
                .button("Покупки " + Emoji.IAP, "/iap " + gameTitle)
                .button("Размер " + Emoji.SIZE, "/size " + gameTitle)
                .endRow()
                .row()
                .button("Получить всю информацию об игре " + Emoji.ALL, "/all " + gameTitle)
                .endRow()
                .row()
                .buttonWithURL("Перейти в Google Play " + Emoji.URL, URL)
                .endRow()
                .row()
                .button("Скрыть клавиатуру", "/close")
                .endRow()
                .build();
    }

    private String replyByDefault() {
        return String.join("\n\n"
                , "Введите название игры, например: LIMBO"
                , "Количество игр в библиотеке: " + googlePlayGameService.getNumberOfGamesInLibrary()
                , "Случайные игры из библиотеки:"
                , getRandomGameTitles());
    }

    private String getRandomGameTitles() {
        Long numberOfGames = googlePlayGameService.getNumberOfGamesInLibrary();
        if (numberOfGames < 10) {
            return getNextNumberOfGameTitles(numberOfGames);
        }
        else {
            return getNextNumberOfGameTitles(10L);
        }
    }

    private String getNextNumberOfGameTitles(Long quantity) {
        return googlePlayGameService.getRandomGames(quantity)
                .stream()
                .map(GooglePlayGame::getTitle)
                .collect(Collectors.joining("\n"));
    }

}
