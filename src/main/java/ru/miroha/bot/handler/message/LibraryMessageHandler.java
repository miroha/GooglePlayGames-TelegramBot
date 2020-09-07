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
import ru.miroha.service.telegram.ReplyMessageService;
import ru.miroha.bot.handler.callbackquery.GooglePlayGameQueryHandler;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles {@link Message} when {@link BotCondition} is {@link BotCondition#IN_LIBRARY}.
 *
 * Searches requested {@link GooglePlayGame} by title in the database.
 * If the game was found, returns inline keyboard that contains information about it.
 */
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
            return replyMessageService.getTextMessage(chatId, defaultReply());
        }
        String title = message.getText();
        List<GooglePlayGame> googlePlayGames = googlePlayGameService.findListOfGamesByTitle(title);

        if (googlePlayGames.size() > 1) {   //<if more than one game with similar title was found
            return replyMessageService.getTextMessage(chatId, updateRequest(googlePlayGames));
        }
        if (googlePlayGames.isEmpty()){
            log.error("Game {} doesn't exist in library", title);
            return replyMessageService.getTextMessage(chatId, "Такой игры в библиотеке нет!");
        }
        return getGameInformationMenu(chatId, googlePlayGames.get(0));
    }

    /**
     * User must clarify his request.
     */
    private String updateRequest(List<GooglePlayGame> games) {
        return String.join("\n\n",
                "Найденные совпадения:",
                getSimilarTitles(games),
                "Уточните ваш запрос!");
    }

    /**
     * Found matches by title.
     */
    private String getSimilarTitles(List<GooglePlayGame> games) {
        return games.stream()
                .map(GooglePlayGame::getTitle)
                .collect(Collectors.joining("\n"));
    }

    /**
     * Returns inline keyboard with buttons that create callback queries to be processed by {@link GooglePlayGameQueryHandler}
     */
    private SendMessage getGameInformationMenu(Long chatId, GooglePlayGame googlePlayGame) {
        String gameTitle = googlePlayGame.getTitle();
        String URL = googlePlayGame.getUrl();
        return InlineKeyboardMarkupBuilder.create(chatId)
                .setText("Вы можете узнать следующую информацию об игре " + gameTitle)
                .row()
                .button("Стоимость " + Emoji.PRICE, "/price " + gameTitle)
                .button("Обновлено " + Emoji.CALENDAR, "/updated " + gameTitle)
                .button("Версия " + Emoji.TOOLS, "/version " + gameTitle)
                .endRow()
                .row()
                .button("Требования " + Emoji.REQUIREMENTS, "/requirements " + gameTitle)
                .button("Покупки " + Emoji.PURCHASES, "/iap " + gameTitle)
                .button("Размер " + Emoji.SIZE, "/size " + gameTitle)
                .endRow()
                .row()
                .button("Получить всю информацию об игре " + Emoji.GAME, "/all " + gameTitle)
                .endRow()
                .row()
                .button("В JSON формате " + Emoji.DOCUMENT, "/json " + gameTitle)
                .endRow()
                .row()
                .buttonWithURL("Перейти в Google Play " + Emoji.URL, URL)
                .endRow()
                .row()
                .button("Скрыть клавиатуру", "/close " + gameTitle)
                .endRow()
                .build();
    }

    private String defaultReply() {
        return String.join("\n\n",
                "Введите название игры, например: LIMBO",
                "Количество игр в библиотеке: " + googlePlayGameService.getLibrarySize(),
                "Случайные игры из библиотеки:",
                getRandomGameTitles());
    }

    private String getRandomGameTitles() {
        return googlePlayGameService.getRandomGames(10L)
                .stream()
                .map(GooglePlayGame::getTitle)
                .collect(Collectors.joining("\n"));
    }

}
