package ru.miroha.bot;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of {@link UserBotCondition} that saves and extracts bot conditions to/from {@link HashMap}.
 *
 * @author Pavel Mironov
 * @version 1.0
 */
@Component
public class BotConditionContext implements UserBotCondition {

    private final Map<Integer, BotCondition> usersBotCondition = new HashMap<>();

    public Map<Integer, BotCondition> getUsersBotCondition() {
        return usersBotCondition;
    }

    @Override
    public BotCondition getCurrentBotConditionForUserById(Integer userId) {
        return usersBotCondition.getOrDefault(userId, BotCondition.MAIN_MENU);
    }

    @Override
    public void setCurrentBotConditionForUserWithId(Integer userId, BotCondition botCondition) {
        usersBotCondition.put(userId, botCondition);
    }

}

