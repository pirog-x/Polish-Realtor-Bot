package com.pirog.PolishRealtorBot.botapi;

import lombok.Setter;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Setter
public class Bot extends TelegramWebhookBot {

    private String username;
    private String token;
    private String webhookPath;

    public Bot(DefaultBotOptions options, String token) {
        super(options, token);
        this.token = token;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(update.getMessage().getChatId());
            sendMessage.setReplyToMessageId(update.getMessage().getMessageId());
            sendMessage.setText(update.getMessage().getText());
            return sendMessage;
        }
        return null;
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
