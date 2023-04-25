package com.pirog.PolishRealtorBot.botapi.interactive.handlers.impl;

import com.pirog.PolishRealtorBot.botapi.interactive.BotState;
import com.pirog.PolishRealtorBot.botapi.interactive.handlers.InputMessageHandler;
import com.pirog.PolishRealtorBot.cache.UserDataCache;
import com.pirog.PolishRealtorBot.dao.repository.UserParserSettingsRepository;
import com.pirog.PolishRealtorBot.service.UserParserSettingsInMemoryService;
import com.vdurmont.emoji.EmojiParser;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FillParserHandler implements InputMessageHandler {

     UserDataCache userCache;
     UserParserSettingsRepository userParserSettingsRepository;
     UserParserSettingsInMemoryService memoryParserService;



    @Override
    public SendMessage handle(Message message) {
        long userId = message.getFrom().getId();
        if (userCache.getCurrentBotState(userId).equals(BotState.FILLING_PARSER_SETTINGS)) {
            userCache.setBotState(userId, BotState.SET_LANGUAGE);
        }
        return processUserInput(message);
    }

    private SendMessage processUserInput(Message message) {
        long userId = message.getFrom().getId();
        BotState currentState = userCache.getCurrentBotState(userId);
        SendMessage answer;

        switch (currentState) {
            case SET_LANGUAGE -> answer = handleSetLanguage(message);
            case SET_CITY -> answer = handleSetCity(message);
            default -> answer = getDefaultAnswer(message);
        }
        return answer;
    }

    private SendMessage getDefaultAnswer(Message message) {
        return SendMessage.builder()
                .text("Please, select again")
                .chatId(message.getChatId())
                .replyMarkup(message.getReplyMarkup())
                .build();
    }

    private SendMessage handleSetLanguage(Message message) {
        long userId = message.getFrom().getId();
        long chatId = message.getChatId();
        ReplyKeyboardMarkup keyboardMarkup = getLanguageKeyboard();

        userCache.setBotState(userId, BotState.SET_CITY);
        return SendMessage.builder()
                .text("Please, choose the language.")
                .chatId(chatId)
                .replyMarkup(keyboardMarkup)
                .build();
    }

    private ReplyKeyboardMarkup getLanguageKeyboard() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> rows = new ArrayList<>();

        KeyboardButton eng = new KeyboardButton(EmojiParser.parseToUnicode("English :gb:"));
        KeyboardButton ua = new KeyboardButton(EmojiParser.parseToUnicode("Українська :ua:"));
        KeyboardButton pl = new KeyboardButton(EmojiParser.parseToUnicode("Polski :pl:"));

        KeyboardRow row1 = new KeyboardRow(Arrays.asList(ua, pl));
        KeyboardRow row2 = new KeyboardRow(List.of(eng));

        rows.add(row1);
        rows.add(row2);
        replyKeyboardMarkup.setKeyboard(rows);

        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        replyKeyboardMarkup.setSelective(true);

        return replyKeyboardMarkup;
    }

    private SendMessage handleSetCity(Message message) {
        long userId = message.getFrom().getId();
        long chatId = message.getChatId();
        ReplyKeyboardMarkup keyboardMarkup = getCityKeyboard();

        List<String> existLanguages = Arrays.asList("Українська", "Polski", "English");
        String lang = EmojiParser.removeAllEmojis(message.getText()).trim();

        if (existLanguages.contains(lang)) {
            userCache.setBotState(userId, BotState.SET_MIN_PRICE);
            return SendMessage.builder()
                    .text("Select city.")
                    .chatId(chatId)
                    .replyMarkup(keyboardMarkup)
                    .build();
        } else {
            return getDefaultAnswer(message);
        }
    }

    private ReplyKeyboardMarkup getCityKeyboard() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setSelective(true);

        KeyboardButton warsaw = new KeyboardButton("Warsaw");
        KeyboardButton krakow = new KeyboardButton("Krakow");
        KeyboardRow row1 = new KeyboardRow(List.of(warsaw, krakow));

        KeyboardButton lodz = new KeyboardButton("Lodz");
        KeyboardButton wroclaw = new KeyboardButton("Wroclaw");
        KeyboardRow row2 = new KeyboardRow(List.of(lodz, wroclaw));

        KeyboardButton poznan = new KeyboardButton("Poznan");
        KeyboardButton gdansk = new KeyboardButton("Gdansk");
        KeyboardRow row3 = new KeyboardRow(List.of(poznan, gdansk));

        KeyboardButton szczecin = new KeyboardButton("Szczecin");
        KeyboardButton bydgoszcz = new KeyboardButton("Bydgoszcz");
        KeyboardRow row4 = new KeyboardRow(List.of(szczecin, bydgoszcz));

        replyKeyboardMarkup.setKeyboard(List.of(row1, row2, row3, row4));
        return replyKeyboardMarkup;
    }

    @Override
    public BotState getHandlerName() {
        return BotState.FILLING_PARSER_SETTINGS;
    }
}
