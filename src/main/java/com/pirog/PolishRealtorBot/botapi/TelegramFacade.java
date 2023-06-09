package com.pirog.PolishRealtorBot.botapi;

import com.pirog.PolishRealtorBot.botapi.interactive.BotState;
import com.pirog.PolishRealtorBot.botapi.interactive.BotStateContext;
import com.pirog.PolishRealtorBot.cache.UserDataCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class TelegramFacade {
    @Autowired
    private BotStateContext botStateContext;
    @Autowired
    private UserDataCache userCache;

    public BotApiMethod<?> handleUpdate(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            return handleTextMessage(update.getMessage());
        }
        return null;
    }

    private SendMessage handleTextMessage(Message message) {
        String inputMsg = message.getText();
        long userId = message.getFrom().getId();
        BotState state;

        switch (inputMsg) {
            case "/start" -> state = BotState.FILLING_PARSER_SETTINGS;
            default -> state = userCache.getCurrentBotState(userId);
        }
        userCache.setBotState(userId, state);
        return botStateContext.processInputMessage(state, message);
    }
}
