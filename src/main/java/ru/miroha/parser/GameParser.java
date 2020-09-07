package ru.miroha.parser;

import org.jsoup.nodes.Document;

/**
 * Collects game data.
 */
public interface GameParser {

    String EMAIL_REGEX = "[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+";

    String getTitle(Document htmlDocument);

    String getGenre(Document htmlDocument);

    String getPrice(Document htmlDocument);

    String getDeveloperContacts(Document htmlDocument);

    String getCurrentVersion(Document htmlDocument);

    String getRequirements(Document htmlDocument);

    String getLastUpdate(Document htmlDocument);

    String getIAP(Document htmlDocument);

    String getInstallationFileSize(Document htmlDocument);

    String getImagePreview(Document htmlDocument);

    String getOverview(Document htmlDocument);

    String getAverageRating(Document htmlDocument);

    String getDeveloperName(Document htmlDocument);

    String getRecentChanges(Document htmlDocument);

    String getDownloads(Document htmlDocument);

}
