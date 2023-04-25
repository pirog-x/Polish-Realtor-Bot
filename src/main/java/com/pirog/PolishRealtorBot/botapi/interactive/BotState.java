package com.pirog.PolishRealtorBot.botapi.interactive;

public enum BotState {
    SET_LANGUAGE,
    SET_CITY,
    SET_MIN_PRICE,
    SET_MAX_PRICE,
    SET_AD_TYPE,
    SET_NUM_OF_ROOMS,

    CHANGE_LANGUAGE,
    CHANGE_CITY,
    CHANGE_MIN_PRICE,
    CHANGE_MAX_PRICE,
    CHANGE_AD_TYPE,
    CHANGE_NUM_OF_ROOMS,

    BOT_SETTINGS,
    WAIT_FOR_FLAT_SEARCH,
}
