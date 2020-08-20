package ru.miroha.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Represents game from Google Play Store.
 *
 * @author Pavel Mironov
 * @version 1.0
 */
@Document(collection = "googlePlayGames")
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

    private String recentChanges;

    private String apkSize;

    private String currentVersion;

    private String requirements;

    private String iap;

    private String developerEmail;

    private String developerName;

    private String URL;

    private String image;

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
                , "Разработчик: " + developerName
                , "Связаться с разработчиком: " + developerEmail
                , "Добавлена в библиотеку: " + addedToLibrary
                , "\nЧто нового в последнем обновлении?\n" + recentChanges
        );
    }

}
