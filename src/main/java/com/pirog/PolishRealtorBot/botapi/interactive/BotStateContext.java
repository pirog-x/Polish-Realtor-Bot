package com.pirog.PolishRealtorBot.botapi.interactive;

import com.pirog.PolishRealtorBot.botapi.interactive.handlers.InputMessageHandler;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

@Component
public class BotStateContext {
    private final Map<BotState, InputMessageHandler> messageHandlers = new HashMap<>();

    public BotStateContext(List<InputMessageHandler> messageHandlers) {
        messageHandlers.forEach(handler -> this.messageHandlers.put(handler.getHandlerName(), handler));
    }

    public SendMessage processInputMessage(BotState state, Message message) {
        InputMessageHandler handler = findInputMessageHandler(state);
        return handler.handle(message);
    }

    private InputMessageHandler findInputMessageHandler(BotState state) {
        if (isFillingParser(state)) {
            return messageHandlers.get(BotState.FILLING_PARSER_SETTINGS);
        }
        return messageHandlers.get(state);
    }

    private boolean isFillingParser(BotState curr) {
        return switch (curr) {
            case FILLING_PARSER_SETTINGS, SET_LANGUAGE, SET_CITY, SET_MIN_PRICE, SET_MAX_PRICE, SET_AD_TYPE, SET_NUM_OF_ROOMS -> true;
            default -> false;
        };
    }
}
