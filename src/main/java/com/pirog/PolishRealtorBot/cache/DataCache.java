package com.pirog.PolishRealtorBot.cache;

import com.pirog.PolishRealtorBot.botapi.interactive.BotState;

public interface DataCache {
    void setBotState(Long userId, BotState state);
    BotState getCurrentBotState(Long userId);
}
