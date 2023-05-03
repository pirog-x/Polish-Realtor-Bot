package com.pirog.PolishRealtorBot.exception;

import com.pirog.PolishRealtorBot.botapi.interactive.BotState;

public class NoSuchStateHandler extends RuntimeException {
    public NoSuchStateHandler(BotState state) {
        super("No such state handler with name: '" + state + "'.");
    }
}
