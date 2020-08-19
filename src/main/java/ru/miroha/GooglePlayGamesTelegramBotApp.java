package ru.miroha;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.telegram.telegrambots.ApiContextInitializer;

/**
 * @author Pavel Mironov
 * @version 1.0
 */
@SpringBootApplication
public class GooglePlayGamesTelegramBotApp {

    public static void main(String[] args) {
        ApiContextInitializer.init();
        SpringApplication.run(GooglePlayGamesTelegramBotApp.class, args);
    }

}
