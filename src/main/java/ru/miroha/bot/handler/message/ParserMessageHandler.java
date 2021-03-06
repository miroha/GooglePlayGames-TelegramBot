package ru.miroha.bot.handler.message;

import lombok.extern.slf4j.Slf4j;

import org.jsoup.HttpStatusException;

import org.springframework.stereotype.Component;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import ru.miroha.bot.BotCondition;
import ru.miroha.model.GooglePlayGame;
import ru.miroha.parser.googleplay.connection.InvalidGooglePlayGameUrlException;
import ru.miroha.service.GooglePlayGameParserService;
import ru.miroha.service.GooglePlayGameService;
import ru.miroha.service.telegram.ReplyMessageService;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Stream;

/**
 * Handles {@link Message} when {@link BotCondition} is {@link BotCondition#REQUEST_BY_URL}.
 * Calls {@link GooglePlayGameParserService} to get information about the game from its URL.
 */
@Slf4j
@Component
public class ParserMessageHandler implements MessageHandler {

    private final GooglePlayGameParserService parserService;

    private final GooglePlayGameService googlePlayGameService;

    private final ReplyMessageService replyMessageService;

    public ParserMessageHandler(GooglePlayGameParserService parserService,
                                GooglePlayGameService googlePlayGameService,
                                ReplyMessageService replyMessageService) {
        this.parserService = parserService;
        this.googlePlayGameService = googlePlayGameService;
        this.replyMessageService = replyMessageService;
    }

    @Override
    public SendMessage handle(Message message) {
        Long chatId = message.getChatId();
        if (message.getText().equals("Поиск по ссылке")) {
            return replyMessageService.getTextMessage(chatId, "Введите ссылку на любую игру в странице магазина Google Play.");
        }
        String URL = message.getText();
        GooglePlayGame googlePlayGame;
        try {
            googlePlayGame = parserService.getGameByUrl(URL, "ru", "RU");
            if (!isGenreValid(googlePlayGame)) {
                throw new InvalidGooglePlayGameUrlException();
            }
        } catch (HttpStatusException e) {
            log.error("Google Play Store isn't available: {}", e.getStatusCode());
            return replyMessageService.getTextMessage(chatId, "Не удаётся получить доступ к магазину Google Play!");
        } catch (IOException | URISyntaxException e) {
            log.error("Couldn't parse message to URL: {}", URL);
            return replyMessageService.getTextMessage(chatId, "Не удалось распарсить страницу.");
        } catch (InvalidGooglePlayGameUrlException e) {
            log.error("Invalid URL: {}", URL);
            return replyMessageService.getTextMessage(chatId, "Некорректный URL-адрес, попробуйте снова.");
        }
        googlePlayGameService.saveToLibrary(googlePlayGame);
        log.info("Game {} saved to library", googlePlayGame.getTitle());
        return replyMessageService.getTextMessage(chatId, googlePlayGame.toString());
    }

    @Override
    public boolean canHandle(BotCondition botCondition) {
        return botCondition.equals(BotCondition.REQUEST_BY_URL);
    }

    /**
     * Doesn't let to parse information about applications.
     */
    private boolean isGenreValid (GooglePlayGame game) {
        if (game.getGenre().equals("Музыка и аудио")) {
            return false;
        }
        var validGenres = List.of(
                "Аркады", "Викторины", "Головоломки", "Гонки",
                "Казино", "Казуальные", "Карточные", "Музыка",
                "Настольные игры", "Настольные", "Обучающие", "Приключения",
                "Ролевые", "Симуляторы", "Словесные игры", "Словесные",
                "Спортивные игры", "Спортивные", "Стратегии", "Экшен"
        );
        return Stream.of(game.getGenre().split(","))
                .anyMatch(validGenres::contains);
    }

}
