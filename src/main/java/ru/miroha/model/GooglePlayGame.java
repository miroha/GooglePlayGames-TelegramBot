package ru.miroha.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Document(collection = "googlePlayGames")
@Component
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderClassName = "GooglePlayGameBuilder", toBuilder = true)
@Getter
@Setter
public final class GooglePlayGame implements Serializable {

    @Id
    private String id;

    @Indexed(unique = true)
    private String title;

    private String genre;

    private String price;

    private String lastUpdate;

    private String apkSize;

    private String currentVersion;

    private String requirements;

    private String iap;

    private String devEmail;

    private String URL;

    private String pictureURL;

    private String averageRating;

    private LocalDate addedToLibrary;

    @Override
    public String toString(){
        return String.join("\n",
                "Название игры: " + title
                , "Жанр: " + genre
                , "Последнее обновление: " + lastUpdate
                , "Текущая версия: " + currentVersion
                , "Требуемая версия Android: " + requirements
                , "Размер установочного файла: " + apkSize
                , "Стоимость игры: " + price
                , "Внутриигровые покупки: " + iap
                , averageRating
                , "Связаться с разработчиком: " + devEmail
                , "Добавлена в библиотеку: " + addedToLibrary
        );
    }

}
