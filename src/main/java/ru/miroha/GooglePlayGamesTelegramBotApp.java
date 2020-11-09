package ru.miroha;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.telegram.telegrambots.ApiContextInitializer;

/**
 * Allows to register bot in spring context automatically and also use it as standard spring bean.
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableScheduling
public class GooglePlayGamesTelegramBotApp {

    public static void main(String[] args) {
        ApiContextInitializer.init();
        SpringApplication.run(GooglePlayGamesTelegramBotApp.class, args);
    }

}
