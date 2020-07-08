package ru.miroha.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.LocalDate;

@Document(collection = "googlePlayGames")
@Component
@Builder(builderClassName = "GooglePlayGameBuilder", toBuilder = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class GooglePlayGame implements Serializable {
    @Id
    String id;
    @Indexed(unique = true)
    String title;
    String genre;
    String price;
    LocalDate dateOfLastUpdate;
    String apkSize;
    String currentVersion;
    String requirements;
    String iap;
    String devEmail;
    String URL;
    String pictureURL;
    String averageRating;
    LocalDate addedToLibrary;

    @Override
    public String toString(){
        return String.join("\n",
                "Название игры: " + title
                , "Жанр: " + genre
                , "Последнее обновление: " + dateOfLastUpdate
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
