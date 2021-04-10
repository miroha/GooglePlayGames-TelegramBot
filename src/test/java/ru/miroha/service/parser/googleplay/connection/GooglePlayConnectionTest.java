package ru.miroha.service.parser.googleplay.connection;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.stream.Stream;

@DisplayNameGeneration(DisplayNameGenerator.Standard.class)
public class GooglePlayConnectionTest {

    private static GooglePlayConnection connection;

    private static Stream<Arguments> validUrls() {
        return Stream.of(
                arguments("https://play.google.com/store/apps/details?id=com.playdigious.deadcells.mobile"),
                arguments("https://play.google.com/store/apps/details?id=com.mobge.Oddmar"),
                arguments("https://play.google.com/store/apps/details?id=eu.bandainamcoent.verylittlenightmares")
        );
    }

    private static Stream<Arguments> invalidUrls() {
        return Stream.of(
                arguments("https://www.google.com/"),
                arguments("https://apps.apple.com/ru/app/dead-cells/id1389752090"),
                arguments("https://play.google.com/store/music/album/Kodaline_Follow_Your_Fire?id=Bzioayoshsavtbd4rw7n3zu5bsq")
        );
    }

    @BeforeAll
    static void createConnection() {
        connection = new GooglePlayConnection();
    }

    @ParameterizedTest
    @MethodSource("validUrls")
    void googlePlayUrlShouldBeLocalizedWithRequiredLanguage(String url) throws MalformedURLException, URISyntaxException {
        Map<String, String> params = connection.getParameters(url);
        assertTrue(connection.getLocalized(params, "ru", "RU").toString().endsWith("&hl=ru&gl=RU"));
        assertTrue(connection.getLocalized(params, "en", "US").toString().endsWith("&hl=en&gl=US"));
    }

    @ParameterizedTest
    @MethodSource("validUrls")
    void parameterIdShouldNotBeEmpty(String url) throws MalformedURLException {
        assertFalse(connection.getParameters(url).get("id").isEmpty());
    }

    @ParameterizedTest
    @MethodSource("invalidUrls")
    void invalidUrlShouldThrowException(String url) {
        InvalidGooglePlayGameUrlException thrown = assertThrows(InvalidGooglePlayGameUrlException.class,
                () -> connection.connect(url));
        assertTrue(thrown.getMessage().contains("Google Play"));
    }

}
