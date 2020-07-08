package ru.miroha.bot.handler.message;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.miroha.bot.BotCondition;
import ru.miroha.bot.keyboard.InlineKeyboardMarkupBuilder;
import ru.miroha.model.GooglePlayGame;
import ru.miroha.repository.GooglePlayGameRepository;
import ru.miroha.service.EmojiService;
import ru.miroha.service.ReplyMessageService;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class LibraryMessageHandler implements  MessageHandler {

    private final ReplyMessageService replyMessageService;
    private final GooglePlayGameRepository googlePlayGameRepository;

    public LibraryMessageHandler(ReplyMessageService replyMessageService,
                                 GooglePlayGameRepository googlePlayGameRepository) {
        this.replyMessageService = replyMessageService;
        this.googlePlayGameRepository = googlePlayGameRepository;
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
        String gameTitle = message.getText();
        List<GooglePlayGame> googlePlayGames = googlePlayGameRepository.findByTitleContainsIgnoreCase(gameTitle);
        if (googlePlayGames.size() > 1) {
            log.info("Found matches for \"{}\": {} ", gameTitle, getSimilarGameTitles(googlePlayGames, ", "));
            return replyMessageService.getTextMessage(chatId, clarifyRequest(googlePlayGames));
        }
        else if (googlePlayGames.size() == 1) {
            return getInlineKeyboard(chatId, googlePlayGames.get(0));
        }
        else {
            log.error("Game {} doesn't exist in library", gameTitle);
            return replyMessageService.getTextMessage(chatId, "Такой игры в библиотеке нет!");
        }
    }

    private String clarifyRequest(List <GooglePlayGame> games) {
        return String.join("\n"
                , "Найденные совпадения:\n"
                , getSimilarGameTitles(games, "\n")
                , "\nУточните ваш запрос!");
    }

    private String getSimilarGameTitles(List <GooglePlayGame> games, String delimiter) {
        return games.stream()
                .map(GooglePlayGame::getTitle)
                .collect(Collectors.joining(delimiter));
    }

    private SendMessage getInlineKeyboard(Long chatId, GooglePlayGame googlePlayGame) {
        String gameTitle = googlePlayGame.getTitle();
        String URL = googlePlayGame.getURL();
        return InlineKeyboardMarkupBuilder.create(chatId)
                .setText("Вы может узнать следующую информацию об игре " + gameTitle)
                .row()
                .button("Стоимость " + EmojiService.MONEY, "/price " + gameTitle)
                .button("Обновлено " + EmojiService.UPDATED, "/updated " + gameTitle)
                .button("Версия " + EmojiService.VERSION, "/version " + gameTitle)
                .endRow()
                .row()
                .button("Требования " + EmojiService.REQUIREMENTS, "/requirements " + gameTitle)
                .button("Покупки " + EmojiService.IAP, "/iap " + gameTitle)
                .button("Размер " + EmojiService.SIZE, "/size " + gameTitle)
                .endRow()
                .row()
                .button("Получить всю информацию об игре " + EmojiService.ALL, "/all " + gameTitle)
                .endRow()
                .row()
                .buttonWithURL("Перейти в Google Play " + EmojiService.URL, URL)
                .endRow()
                .row()
                .button("Скрыть клавиатуру", "/close")
                .endRow()
                .build();
    }

    private String defaultReply() {
        return String.join("\n\n"
                , "Введите название игры, например: LIMBO"
                , "Количество игр в библиотеке: " + getNumberOfGamesInLibrary()
                , "Случайные игры из библиотеки:"
                , getRandomGames());
    }

    private Long getNumberOfGamesInLibrary() {
        return googlePlayGameRepository.count();
    }

    private String getRandomGames() {
        Long quantityOfGames = getNumberOfGamesInLibrary();
        if (quantityOfGames < 10) {
            return getNextNumberOfGames(quantityOfGames);
        }
        else {
            return getNextNumberOfGames(10L);
        }
    }

    private String getNextNumberOfGames(Long amount) {
        return googlePlayGameRepository.findRandomGames(amount)
                .stream()
                .map(GooglePlayGame::getTitle)
                .collect(Collectors.joining("\n"));
    }
}
