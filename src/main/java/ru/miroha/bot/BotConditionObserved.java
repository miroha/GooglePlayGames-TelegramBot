package ru.miroha.bot;

/**
 * Helps to find out what condition the bot is in for each user.
 * Also allows to set current condition for the bot.
 */
public interface BotConditionObserved {

    BotCondition getCurrentBotConditionForUserById(Integer userId);

    void setCurrentBotConditionForUserWithId(Integer userId, BotCondition botCondition);

}
