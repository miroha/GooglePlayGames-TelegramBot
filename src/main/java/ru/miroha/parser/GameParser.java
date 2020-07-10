package ru.miroha.parser;

import org.jsoup.nodes.Document;

public interface GameParser {

    String EMAIL_REGEX = "[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+";

    String parseTitle(Document htmlDocument);

    String parseGenre(Document htmlDocument);

    String parsePrice(Document htmlDocument);

    String parseContacts(Document htmlDocument);

    String parseVersion(Document htmlDocument);

    String parseRequirements(Document htmlDocument);

    String parseDateOfLastUpdate(Document htmlDocument);

    String parseIAP(Document htmlDocument);

    String parseInstallationFileSize(Document htmlDocument);

    String parseGamePicture(Document htmlDocument);

    String parseDescription(Document htmlDocument);

    String parseRating(Document htmlDocument);

    String parseDeveloperName(Document htmlDocument);

}
