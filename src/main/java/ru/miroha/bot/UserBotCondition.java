package ru.miroha.bot;

public interface UserBotCondition {

    BotCondition getCurrentBotConditionForUserById(Integer userId);

    void setCurrentBotConditionForUserWithId(Integer userId, BotCondition botCondition);

}
