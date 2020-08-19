package ru.miroha.util;

import com.vdurmont.emoji.EmojiParser;

/**
 * Emoji library for interaction with user.
 *
 * @author Pavel Mironov
 * @version 1.0.
 */
public enum Emoji {

    PRICE(EmojiParser.parseToUnicode(":dollar:")),
    PURCHASES(EmojiParser.parseToUnicode(":shopping_bags:")),
    CALENDAR(EmojiParser.parseToUnicode(":spiral_calendar_pad:")),
    TOOLS(EmojiParser.parseToUnicode(":hammer_and_wrench:")),
    SIZE(EmojiParser.parseToUnicode(":mag:")),
    REQUIREMENTS(EmojiParser.parseToUnicode(":no_mobile_phones:")),
    GAME(EmojiParser.parseToUnicode(":video_game:")),
    CLOSE(EmojiParser.parseToUnicode(":see_no_evil:")),
    MENU(EmojiParser.parseToUnicode(":point_down:")),
    EYES(EmojiParser.parseToUnicode(":eyes:")),
    URL(EmojiParser.parseToUnicode(":earth_asia:"));

    private final String emojiName;

    Emoji(String emojiName) {
        this.emojiName = emojiName;
    }

    @Override
    public String toString() {
        return emojiName;
    }

}
