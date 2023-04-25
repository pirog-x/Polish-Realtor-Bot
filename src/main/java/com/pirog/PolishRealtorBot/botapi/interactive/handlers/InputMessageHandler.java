package com.pirog.PolishRealtorBot.botapi.interactive.handlers;

import com.pirog.PolishRealtorBot.botapi.interactive.BotState;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface InputMessageHandler {
    SendMessage handle(Message message);
    BotState getHandlerName();
}
