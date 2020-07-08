package ru.miroha.bot.handler.message;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.HttpStatusException;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.miroha.bot.BotCondition;
import ru.miroha.model.GooglePlayGame;
import ru.miroha.parser.googleplay.connection.exception.InvalidGooglePlayLinkException;
import ru.miroha.repository.GooglePlayGameRepository;
import ru.miroha.service.GooglePlayGameService;
import ru.miroha.service.ReplyMessageService;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

@Slf4j
@Component
public class ParserMessageHandler implements MessageHandler {

    private final GooglePlayGameService googlePlayGameService;
    private final GooglePlayGameRepository googlePlayGameRepository;
    private final ReplyMessageService replyMessageService;
    private GooglePlayGame googlePlayGame;

    public ParserMessageHandler(GooglePlayGameService googlePlayGameService,
                                GooglePlayGameRepository googlePlayGameRepository,
                                ReplyMessageService replyMessageService,
                                GooglePlayGame googlePlayGame) {
        this.googlePlayGameService = googlePlayGameService;
        this.googlePlayGameRepository = googlePlayGameRepository;
        this.replyMessageService = replyMessageService;
        this.googlePlayGame = googlePlayGame;
    }

    @Override
    public SendMessage handle(Message message) {
        if (message.getText().equals("Поиск по ссылке")) {
            return replyMessageService.getTextMessage(message.getChatId(), "Введите ссылку на любую игру в странице магазина Google Play.");
        }
        String URL = message.getText();
        Long chatId = message.getChatId();
        try {
            googlePlayGame = getGooglePlayGameByURL(URL);
            if (!isGenreValid(googlePlayGame)){
                throw new InvalidGooglePlayLinkException();
            }
        } catch (HttpStatusException e) {
            log.error("Google Play Store is not available: {}", e.getStatusCode());
            return replyMessageService.getTextMessage(chatId, "Не удаётся получить доступ к магазину Google Play!");
        } catch (IOException | URISyntaxException e) {
            log.error("Couldn't parse URL: {}", URL);
            return replyMessageService.getTextMessage(chatId, "Не удалось распарсить страницу.");
        } catch (InvalidGooglePlayLinkException e) {
            log.error("Invalid link: {}", URL);
            return replyMessageService.getTextMessage(chatId, "Некорректная ссылка, попробуйте снова.");
        }
        googlePlayGameRepository.save(googlePlayGame);
        log.info("Game {} added to library", googlePlayGame.getTitle());
        return replyMessageService.getTextMessage(chatId, googlePlayGame.toString());
    }

    @Override
    public boolean canHandle(BotCondition botCondition) {
        return botCondition.equals(BotCondition.REQUEST_BY_URL);
    }

    private GooglePlayGame getGooglePlayGameByURL(String URL) throws IOException, InvalidGooglePlayLinkException, URISyntaxException {
        return googlePlayGameService.getGooglePlayGame(URL);
    }

    private boolean isGenreValid (GooglePlayGame game) {
        Set<String> availableGenres = new HashSet<>(Arrays.asList("Аркады", "Викторины", "Головоломки", "Гонки", "Казино", "Казуальные", "Карточные", "Музыка", "Настольные игры",
                        "Настольные", "Обучающие", "Приключения", "Ролевые", "Симуляторы", "Словесные игры", "Словесные", "Спортивные игры", "Спортивные", "Стратегии", "Экшен"));
        return Stream.of(game.getGenre().split("\\s+"))
                .anyMatch(availableGenres::contains);
    }
}
