package com.pirog.PolishRealtorBot.cache;

import com.pirog.PolishRealtorBot.botapi.interactive.BotState;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserDataCache implements DataCache {

    private final Map<Long, BotState> cache = new HashMap<>();

    @Override
    public void setBotState(Long userId, BotState state) {
        cache.put(userId, state);
    }

    @Override
    public BotState getCurrentBotState(Long userId) {
        return cache.getOrDefault(userId, BotState.SET_LANGUAGE);
    }
}
