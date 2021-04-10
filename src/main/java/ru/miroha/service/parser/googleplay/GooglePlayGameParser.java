package ru.miroha.service.parser.googleplay;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import ru.miroha.service.parser.GameParser;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * This simple parser can be used to find and extract data from <a href="https://play.google.com/store/apps/category/GAME">Google Play Games</a>
 * web pages using DOM traversal and CSS selectors.
 *
 * Requires HTML document that can be easily obtained via {@link ru.miroha.service.parser.googleplay.connection.GooglePlayConnection}.
 */
@Component
public class GooglePlayGameParser implements GameParser {

    private static final String DEFAULT_EMAIL_SUPPORT = "https://support.google.com/googleplay/";

    private static final String INFO_NOT_AVAILABLE = "Информация не доступна";

    /**
     * CSS-like element selectors, that find elements matching a query.
     */
    private static final String CURRENT_VERSION = "div:matchesOwn(^Текущая версия$)";

    private static final String LAST_UPDATED = "div:matchesOwn(^Обновлено$)";

    private static final String REQUIREMENTS = "div:matchesOwn(^Требуемая версия Android$)";

    private static final String CONTACTS = "div:matchesOwn(^Разработчик$)";

    private static final String IAP = "div:matchesOwn(^Платный контент$)";

    private static final String APK_SIZE = "div:matchesOwn(^Размер$)";

    private static final String DEVELOPER = "div:matchesOwn(^Продавец$)";

    private static final String INSTALLS = "div:matchesOwn(^Количество установок$)";

    @Override
    public String getTitle(Document htmlDocument) {
        return htmlDocument.getElementsByAttributeValue("itemprop", "name").text();
    }

    @Override
    public String getGenre(Document htmlDocument) {
        Set <String> genres = new HashSet<>();
        Elements elementsByAttributeValue = htmlDocument.getElementsByAttributeValue("itemprop", "genre");
        elementsByAttributeValue.forEach(element ->
            genres.add(element.text())
        );
        return String.join(", ", genres);
    }

    @Override
    public String getPrice(Document htmlDocument) {
        String price = parseByMetaTag("price", "itemprop", htmlDocument);
        return "0".equals(price)
                ? "Бесплатно"
                : price;
    }

    @Override
    public String getIAP(Document htmlDocument) {
        return isAttributePresent(IAP, htmlDocument)
                ? getByAttribute(IAP, htmlDocument)
                : "Отсутствуют";
    }

    @Override
    public String getCurrentVersion(Document htmlDocument) {
        return getIfAttributePresent(CURRENT_VERSION, htmlDocument);
    }

    @Override
    public String getLastUpdate(Document htmlDocument) {
        return getIfAttributePresent(LAST_UPDATED, htmlDocument);
    }

    @Override
    public String getRecentChanges(Document htmlDocument) {
        Elements elementsByAttributeValue = htmlDocument.getElementsByAttributeValue("itemprop", "description");
        Element container = elementsByAttributeValue.last().parent().select("span").first();
        container.select("br").append("\\n");
        container.select("p").prepend("\\n\\n");
        String recentChanges = container.html().replaceAll("\\\\n", "\n");
        return Jsoup.clean(recentChanges, "", Whitelist.none(), new Document.OutputSettings().prettyPrint(false));
    }

    @Override
    public String getInstallationFileSize(Document htmlDocument) {
        return getIfAttributePresent(APK_SIZE, htmlDocument);
    }

    @Override
    public String getDownloads(Document htmlDocument) {
        return getIfAttributePresent(INSTALLS, htmlDocument);
    }

    @Override
    public String getRequirements(Document htmlDocument) {
        return getIfAttributePresent(REQUIREMENTS, htmlDocument);
    }

    @Override
    public String getDeveloperName(Document htmlDocument) {
        return getIfAttributePresent(DEVELOPER, htmlDocument);
    }

    @Override
    public String getDeveloperContacts(Document htmlDocument) {
        String email = DEFAULT_EMAIL_SUPPORT;
        Matcher m = Pattern.compile(EMAIL_REGEX).matcher(getIfAttributePresent(CONTACTS, htmlDocument));
        while (m.find()) {
            email = m.group();
        }
        return email;
    }

    @Override
    public String getImagePreview(Document htmlDocument) {
        return parseByMetaTag("twitter:image", "name", htmlDocument);
    }

    @Override
    public String getOverview(Document htmlDocument) {
        return parseByMetaTag("description", "itemprop", htmlDocument);
    }

    @Override
    public String getAverageRating(Document htmlDocument) {
        return htmlDocument.getElementsByAttributeValueStarting("aria-label", "Средняя оценка").text().trim();

    }

    /**
     * Extracts screenshots in .webp format.
     */
    public List<String> getScreenshots(Document htmlDocument) {
        return htmlDocument.select("img[src]").stream()
                .filter(img -> img.attr("alt").equals("Скрнишот"))
                .map(element -> StringUtils.isNotEmpty(element.absUrl("srcset"))
                        ? StringUtils.substringBefore(element.absUrl("srcset"), " ")
                        : StringUtils.substringBefore(element.absUrl("data-srcset"), " "))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves the required element that is in meta tags.
     */
    private String parseByMetaTag(String pattern, String attribute, Document htmlDocument) {
        return htmlDocument.getElementsByTag("meta").stream()
                .filter(tag -> pattern.equals(tag.attr(attribute)))
                .findFirst()
                .map(tag -> tag.attr("content"))
                .orElse(INFO_NOT_AVAILABLE);
    }

    /**
     * Retrieves the element by attribute if exists.
     */
    private String getIfAttributePresent(String pattern, Document htmlDocument) {
        return isAttributePresent(pattern, htmlDocument)
                ? getByAttribute(pattern, htmlDocument)
                : INFO_NOT_AVAILABLE;
    }

    private String getByAttribute(String pattern, Document htmlDocument) {
        return htmlDocument.select(pattern)
                .first()
                .parent()
                .select("span")
                .first()
                .text();
    }

    /**
     * Checks if the required attribute exists (to prevent possible NPE) before extracting it.
     * Variant with {@link java.util.Optional} works in the wrong way.
     */
    private boolean isAttributePresent(String pattern, Document htmlDocument) {
        return !(htmlDocument.select(pattern).isEmpty());
    }

}
