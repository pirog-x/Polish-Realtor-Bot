package com.pirog.PolishRealtorBot.botapi;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@Setter
public class Bot extends TelegramWebhookBot {

    @Autowired
    TelegramFacade telegramFacade;

    private String username;
    private String token;
    private String webhookPath;

    public Bot(DefaultBotOptions options, String token) {
        super(options, token);
        this.token = token;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        return telegramFacade.handleUpdate(update);
    }

    @Override
    public String getBotPath() {
        return this.webhookPath;
    }

    @Override
    public String getBotUsername() {
        return this.username;
    }
}
