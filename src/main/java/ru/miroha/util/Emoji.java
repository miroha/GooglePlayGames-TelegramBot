package ru.miroha.util;

import com.vdurmont.emoji.EmojiParser;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Emoji {

    MONEY(EmojiParser.parseToUnicode(":dollar:")),
    IAP(EmojiParser.parseToUnicode(":shopping_bags:")),
    UPDATED(EmojiParser.parseToUnicode(":spiral_calendar_pad:")),
    VERSION(EmojiParser.parseToUnicode(":hammer_and_wrench:")),
    SIZE(EmojiParser.parseToUnicode(":mag:")),
    REQUIREMENTS(EmojiParser.parseToUnicode(":no_mobile_phones:")),
    ALL(EmojiParser.parseToUnicode(":video_game:")),
    HIDE(EmojiParser.parseToUnicode(":see_no_evil:")),
    POINT_DOWN(EmojiParser.parseToUnicode(":point_down:")),
    EYES(EmojiParser.parseToUnicode(":eyes:")),
    URL(EmojiParser.parseToUnicode(":earth_asia:"));

    private final String emojiName;

    @Override
    public String toString() {
        return emojiName;
    }

}
