package ru.miroha.parser;

import org.jsoup.nodes.Document;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.miroha.parser.googleplay.GooglePlayGameParser;
import ru.miroha.parser.googleplay.connection.GooglePlayConnection;
import ru.miroha.parser.googleplay.connection.InvalidGooglePlayGameUrlException;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.stream.Stream;

@DisplayNameGeneration(DisplayNameGenerator.Standard.class)
public class GooglePlayGameParserTest {

    private static GooglePlayGameParser parser;
    private static GooglePlayConnection connection;

    private Document document;

    @BeforeAll
    static void initParser() {
        parser = new GooglePlayGameParser();
    }

    @BeforeAll
    static void initConnection() {
        connection = new GooglePlayConnection();
    }

    private static Stream<Arguments> freeGames() {
        return Stream.of(
                arguments("https://play.google.com/store/apps/details?id=com.mobge.Oddmar"),
                arguments("https://play.google.com/store/apps/details?id=com.hitcents.forgottonanne"),
                arguments("https://play.google.com/store/apps/details?id=com.noodlecake.altosadventure")
        );
    }

    private static Stream<Arguments> paidGames() {
        return Stream.of(
                arguments("https://play.google.com/store/apps/details?id=com.rockstargames.gtasa"),
                arguments("https://play.google.com/store/apps/details?id=nz.co.codepoint.minimetro"),
                arguments("https://play.google.com/store/apps/details?id=com.robtopx.geometryjump")
        );
    }

    private static Stream<Arguments> gamesWithIap() {
        return Stream.of(
                arguments("https://play.google.com/store/apps/details?id=com.ustwo.monumentvalley"),
                arguments("https://play.google.com/store/apps/details?id=com.elevenbitstudios.twommobile"),
                arguments("https://play.google.com/store/apps/details?id=com.imangi.templerun")
        );
    }

    @ParameterizedTest
    @MethodSource("freeGames")
    void freeGamesShouldReturnFreeAsPrice(String url) throws InvalidGooglePlayGameUrlException, URISyntaxException, IOException {
        document = getDocument(url);
        assertEquals(parser.getPrice(document), "Бесплатно");
    }

    @ParameterizedTest
    @MethodSource({"freeGames", "paidGames", "gamesWithIap"})
    void titleShouldNotBeNull(String url) throws InvalidGooglePlayGameUrlException, URISyntaxException, IOException {
        document = getDocument(url);
        assertNotNull(parser.getTitle(document));
    }

    @ParameterizedTest
    @MethodSource("paidGames")
    void paidGamesShouldReturnPrice(String url) throws InvalidGooglePlayGameUrlException, URISyntaxException, IOException {
        document = getDocument(url);
        assertTrue(parser.getPrice(document).contains("₽"));
    }

    @ParameterizedTest
    @MethodSource("gamesWithIap")
    void gamesWithIapShouldContainPriceOfPurchases(String url) throws InvalidGooglePlayGameUrlException, URISyntaxException, IOException {
        document = getDocument(url);
        assertTrue(parser.getIAP(document).contains("₽"));
    }

    @ParameterizedTest
    @MethodSource("paidGames")
    void gamesWithoutIapShouldReturnLackOfPurchases(String url) throws InvalidGooglePlayGameUrlException, URISyntaxException, IOException {
        document = getDocument(url);
        assertEquals("Отсутствуют", parser.getIAP(document));
    }

    Document getDocument(String url) throws InvalidGooglePlayGameUrlException, URISyntaxException, IOException {
        return connection.connect(url).get();
    }

}
