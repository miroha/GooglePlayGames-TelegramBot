package ru.miroha.parser;

import org.jsoup.nodes.Document;

import java.time.LocalDate;

public interface GameParser {

    String EMAIL_REGEX = "[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+";

    String parseTitle(Document document);

    String parseGenre(Document document);

    String parsePrice(Document document);

    String parseContacts(Document document);

    String parseVersion(Document document);

    String parseRequirements(Document document);

    LocalDate parseDateOfLastUpdate(Document document);

    String parseIAP(Document document);

    String parseInstallationFileSize(Document document);

    String parseGamePicture(Document document);

    String parseDescription(Document document);

    String parseRating(Document document);

    String parseDeveloperName(Document document);
}
