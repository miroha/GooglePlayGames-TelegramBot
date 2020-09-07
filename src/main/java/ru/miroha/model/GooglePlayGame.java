package ru.miroha.model;

import com.fasterxml.jackson.annotation.JsonProperty;

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
 */
@Document(collection = "googlePlayGames")
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderClassName = "GooglePlayGameBuilder", toBuilder = true)
@Getter
@Setter
public final class GooglePlayGame implements Serializable {

    @Id
    @JsonProperty("Имя пакета")
    private String id;

    @JsonProperty("Название")
    @Indexed(unique = true)
    private String title;

    @JsonProperty("Жанр")
    private String genre;

    @JsonProperty("Стоимость")
    private String price;

    @JsonProperty("Последнее обновление")
    private String lastUpdate;

    @JsonProperty("Последние изменения")
    private String recentChanges;

    @JsonProperty("Размер установочного файла")
    private String apkSize;

    @JsonProperty("Текущая версия")
    private String currentVersion;

    @JsonProperty("Требования")
    private String requirements;

    @JsonProperty("Внутриигровые покупки")
    private String iap;

    @JsonProperty("Разработчик")
    private String developerName;

    @JsonProperty("Почта разработчика")
    private String developerEmail;

    @JsonProperty("Ссылка на страницу с игрой")
    private String url;

    @JsonProperty("Заглавное изображение")
    private String image;

    @JsonProperty("Средний рейтинг")
    private String averageRating;

    @JsonProperty("Количество установок")
    private String downloads;

    @JsonProperty("Описание")
    private String overview;

    @JsonProperty("Актуальность информации")
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
                , "Средняя оценка: " + averageRating
                , "Количество установок: " + downloads
                , "Разработчик: " + developerName
                , "Связаться с разработчиком: " + developerEmail
                , "Добавлена в библиотеку: " + addedToLibrary
                , "\nЧто нового в последнем обновлении?\n" + recentChanges
        );
    }

}
