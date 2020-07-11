package ru.miroha.parser.googleplay;

import org.jsoup.nodes.Document;

import org.springframework.stereotype.Component;

import ru.miroha.parser.GameParser;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class GooglePlayGameParser implements GameParser {

    private static final String CURRENT_VERSION = "div:matchesOwn(^Текущая версия$)";

    private static final String LAST_UPDATED = "div:matchesOwn(^Обновлено$)";

    private static final String REQUIREMENTS = "div:matchesOwn(^Требуемая версия Android$)";

    private static final String CONTACTS = "div:matchesOwn(^Разработчик$)";

    private static final String IAP = "div:matchesOwn(^Платный контент$)";

    private static final String APK_SIZE = "div:matchesOwn(^Размер$)";

    private static final String DEVELOPER = "div:matchesOwn(^Продавец$)";

    private static final String DEFAULT_EMAIL_SUPPORT = "https://support.google.com/googleplay/";

    private static final String INFO_NOT_AVAILABLE = "Информация не доступна";

    @Override
    public String parseGenre(Document htmlDocument) {
        return removeGenreDuplicates(htmlDocument.getElementsByAttributeValue("itemprop", "genre").text());
    }

    @Override
    public String parseInstallationFileSize(Document htmlDocument) {
        return parseIfAttributePresent(APK_SIZE, htmlDocument);
    }

    @Override
    public String parseTitle(Document htmlDocument) {
        return htmlDocument.getElementsByAttributeValue("itemprop", "name").text();
    }

    @Override
    public String parseIAP(Document htmlDocument) {
        return isAttributePresent(IAP, htmlDocument)
                ? parseByAttribute(IAP, htmlDocument)
                : "Отсутствуют";
    }

    @Override
    public String parseDeveloperName(Document htmlDocument) {
        return parseIfAttributePresent(DEVELOPER, htmlDocument);
    }

    @Override
    public String parseContacts(Document htmlDocument) {
        String email = DEFAULT_EMAIL_SUPPORT;
        Matcher m = Pattern.compile(EMAIL_REGEX).matcher(parseIfAttributePresent(CONTACTS, htmlDocument));
        while (m.find()) {
            email = m.group();
        }
        return email;
    }

    @Override
    public String parsePrice(Document htmlDocument) {
        String price = parseByMetaTagWithStream("price", "itemprop", htmlDocument);
        return "0".equals(price)
                ? "Бесплатно"
                : price;
    }

    @Override
    public String parseVersion(Document htmlDocument) {
        return parseIfAttributePresent(CURRENT_VERSION, htmlDocument);
    }

    @Override
    public String parseRequirements(Document htmlDocument) {
        return parseIfAttributePresent(REQUIREMENTS, htmlDocument);
    }

    @Override
    public String parseDateOfLastUpdate(Document htmlDocument) {
        return parseIfAttributePresent(LAST_UPDATED, htmlDocument);
    }

    @Override
    public String parseGamePicture(Document htmlDocument) {
        return parseByMetaTagWithStream("twitter:image", "name", htmlDocument);
    }

    @Override
    public String parseDescription(Document htmlDocument) {
        return parseByMetaTagWithStream("description", "itemprop", htmlDocument);
    }

    @Override
    public String parseRating(Document htmlDocument) {
        return htmlDocument.getElementsByAttributeValueStarting("aria-label", "Средняя оценка").attr("aria-label");

    }

    private String parseByMetaTagWithStream(String pattern, String attribute, Document htmlDocument) {
        return htmlDocument.getElementsByTag("meta").stream()
                .filter(tag -> pattern.equals(tag.attr(attribute)))
                .findFirst()
                .map(tag -> tag.attr("content"))
                .orElse(INFO_NOT_AVAILABLE);
    }

    private String parseByAttribute(String pattern, Document htmlDocument) {
        return htmlDocument.select(pattern)
                .first()
                .parent()
                .select("span")
                .first()
                .text();
    }

    private boolean isAttributePresent(String pattern, Document htmlDocument) {
        return !(htmlDocument.select(pattern).text().isEmpty());
    }

    private String parseIfAttributePresent(String pattern, Document htmlDocument) {
        return isAttributePresent(pattern, htmlDocument)
                ? parseByAttribute(pattern, htmlDocument)
                : INFO_NOT_AVAILABLE;
    }

    private String removeGenreDuplicates(String genre) {
        return Arrays.stream(genre.split("\\s+"))
                .distinct()
                .collect(Collectors.joining(" "));
    }

}
