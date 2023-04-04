package com.pirog.PolishRealtorBot.appconfig;

import com.pirog.PolishRealtorBot.botapi.Bot;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.bots.DefaultBotOptions;

@Configuration
public class BotConfig {
    @Value("${bot.username}")
    private String username;
    @Value("${bot.token}")
    private String token;
    @Value("${bot.webhookPath}")
    private String webhookPath;

    @Bean
    public Bot bot() {
        DefaultBotOptions botOptions = new DefaultBotOptions();

        Bot bot = new Bot(botOptions, this.token);
        bot.setUsername(this.username);
        bot.setWebhookPath(this.webhookPath);

        return bot;
    }
}
