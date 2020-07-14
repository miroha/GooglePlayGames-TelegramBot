package ru.miroha.bot.handler.message;

import lombok.extern.slf4j.Slf4j;

import org.jsoup.HttpStatusException;

import org.springframework.stereotype.Component;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import ru.miroha.bot.BotCondition;
import ru.miroha.model.GooglePlayGame;
import ru.miroha.parser.googleplay.connection.exception.InvalidGooglePlayGameUrlException;
import ru.miroha.service.GooglePlayGameService;
import ru.miroha.service.ReplyMessageService;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
@Component
public class ParserMessageHandler implements MessageHandler {

    private final GooglePlayGameService googlePlayGameService;

    private final ReplyMessageService replyMessageService;

    public ParserMessageHandler(GooglePlayGameService googlePlayGameService,
                                ReplyMessageService replyMessageService) {
        this.googlePlayGameService = googlePlayGameService;
        this.replyMessageService = replyMessageService;
    }

    @Override
    public SendMessage handle(Message message) {
        Long chatId = message.getChatId();
        GooglePlayGame googlePlayGame;
        if (message.getText().equals("Поиск по ссылке")) {
            return replyMessageService.getTextMessage(chatId, "Введите ссылку на любую игру в странице магазина Google Play.");
        }
        String URL = message.getText();
        try {
            googlePlayGame = googlePlayGameService.getGameByUrl(URL);
            if (!isGenreValid(googlePlayGame)){
                throw new InvalidGooglePlayGameUrlException();
            }
        } catch (HttpStatusException e) {
            log.error("Google Play Store isn't available: {}", e.getStatusCode());
            return replyMessageService.getTextMessage(chatId, "Не удаётся получить доступ к магазину Google Play!");
        } catch (IOException e) {
            log.error("Couldn't parse message to URL: {}", URL);
            return replyMessageService.getTextMessage(chatId, "Не удалось распарсить страницу.");
        } catch (InvalidGooglePlayGameUrlException e) {
            log.error("Invalid URL: {}", URL);
            return replyMessageService.getTextMessage(chatId, "Некорректный URL-адрес, попробуйте снова.");
        }
        googlePlayGameService.saveGame(googlePlayGame);
        log.info("Game {} saved to library", googlePlayGame.getTitle());
        return replyMessageService.getTextMessage(chatId, googlePlayGame.toString());
    }

    @Override
    public boolean canHandle(BotCondition botCondition) {
        return botCondition.equals(BotCondition.REQUEST_BY_URL);
    }

    /*
    Don't let to parse information about applications
     */
    private boolean isGenreValid (GooglePlayGame game) {
        if (game.getGenre().equals("Музыка и аудио")) {
            return false;
        }
        var correctGenres = List.of(
                "Аркады", "Викторины", "Головоломки", "Гонки",
                "Казино", "Казуальные", "Карточные", "Музыка",
                "Настольные игры", "Настольные", "Обучающие", "Приключения",
                "Ролевые", "Симуляторы", "Словесные игры", "Словесные",
                "Спортивные игры", "Спортивные", "Стратегии", "Экшен"
        );
        return Stream.of(game.getGenre().split("\\s+"))
                .anyMatch(correctGenres::contains);
    }

}
