package ru.miroha.parser.googleplay;

import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;
import ru.miroha.parser.GameParser;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Locale;
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
    public String parseGenre(Document document) {
        return removeGenreDuplicates(document.getElementsByAttributeValue("itemprop", "genre").text());
    }

    @Override
    public String parseInstallationFileSize(Document document) {
        return parseIfPresent(APK_SIZE, document);
    }

    @Override
    public String parseTitle(Document document) {
        return document.getElementsByAttributeValue("itemprop", "name").text();
    }

    @Override
    public String parseIAP(Document document) {
        return isPresent(IAP, document) ?
                parseByAttribute(IAP, document) : "Отсутствуют";
    }

    @Override
    public String parseDeveloperName(Document document) {
        return parseIfPresent(DEVELOPER, document);
    }

    @Override
    public String parseContacts(Document document) {
        String email = DEFAULT_EMAIL_SUPPORT;
        Matcher m = Pattern.compile(EMAIL_REGEX).matcher(parseIfPresent(CONTACTS, document));
        while (m.find()) {
            email = m.group();
        }
        return email;
    }

    @Override
    public String parsePrice(Document document) {
        String price = parseMetaTagsWithStream("price", "itemprop", document);
        return "0".equals(price) ? "Бесплатно" : price;
    }

    @Override
    public String parseVersion(Document document) {
        return parseIfPresent(CURRENT_VERSION, document);
    }

    @Override
    public String parseRequirements(Document document) {
        return parseIfPresent(REQUIREMENTS, document);
    }

    @Override
    public LocalDate parseDateOfLastUpdate(Document document) {
        String dateOfLastUpdate = parseIfPresent(LAST_UPDATED, document).replace("г.", "").trim();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("d MMMM yyyy").withLocale(Locale.forLanguageTag("ru-RU"));
        return LocalDate.parse(dateOfLastUpdate, format);
    }

    @Override
    public String parseGamePicture(Document document) {
        return parseMetaTagsWithStream("twitter:image", "name", document);
    }

    @Override
    public String parseDescription(Document document) {
        return parseMetaTagsWithStream("description", "itemprop", document);
    }

    @Override
    public String parseRating(Document document) {
        return document.getElementsByAttributeValueStarting("aria-label", "Средняя оценка").attr("aria-label");

    }

    private String parseMetaTagsWithStream(String pattern, String attribute, Document document) {
        return document.getElementsByTag("meta").stream()
                .filter(tag -> pattern.equals(tag.attr(attribute)))
                .findFirst()
                .map(tag -> tag.attr("content"))
                .orElse(INFO_NOT_AVAILABLE);
    }

    private String parseByAttribute(String pattern, Document document) {
        return document.select(pattern)
                .first()
                .parent()
                .select("span")
                .first()
                .text();
    }

    private boolean isPresent(String pattern, Document document) {
        return !(document.select(pattern)
                .text()
                .isEmpty());
    }

    private String parseIfPresent(String pattern, Document document) {
        return isPresent(pattern, document) ?
                parseByAttribute(pattern, document) : INFO_NOT_AVAILABLE;
    }

    private String removeGenreDuplicates(String genre) {
        return Arrays.stream(genre.split("\\s+"))
                .distinct()
                .collect(Collectors.joining(" "));
    }
}
