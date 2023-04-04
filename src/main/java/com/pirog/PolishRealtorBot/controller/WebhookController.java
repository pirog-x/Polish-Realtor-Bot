package com.pirog.PolishRealtorBot.controller;

import com.pirog.PolishRealtorBot.botapi.Bot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@RestController
public class WebhookController {

    @Autowired
    Bot bot;

    @PostMapping
    public BotApiMethod<?> handleWebhook(@RequestBody Update update) {
        return bot.onWebhookUpdateReceived(update);
    }
}
