package com.pirog.PolishRealtorBot.botapi.interactive.handlers.impl;

import com.pirog.PolishRealtorBot.botapi.interactive.BotState;
import com.pirog.PolishRealtorBot.botapi.interactive.handlers.InputMessageHandler;
import com.pirog.PolishRealtorBot.cache.UserDataCache;
import com.pirog.PolishRealtorBot.dao.entity.NumberOfRoom;
import com.pirog.PolishRealtorBot.dao.entity.NumberOfRoomEnum;
import com.pirog.PolishRealtorBot.dao.entity.UserParserSettings;
import com.pirog.PolishRealtorBot.service.NumberOfRoomService;
import com.pirog.PolishRealtorBot.service.UserParserSettingsInMemoryService;
import com.pirog.PolishRealtorBot.service.UserParserSettingsService;
import com.vdurmont.emoji.EmojiParser;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.*;

@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FillParserHandler implements InputMessageHandler {

    UserDataCache userCache;
    UserParserSettingsInMemoryService userParserSettingsInMemoryService;
    UserParserSettingsService userParserSettingsService;
    NumberOfRoomService numberOfRoomService;

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
            case SET_MIN_PRICE -> answer = handleSetMinPrice(message);
            case SET_MAX_PRICE -> answer = handleSetMaxPrice(message);
            case SET_AD_TYPE -> answer = handleSetAdType(message);
            case SET_NUM_OF_ROOMS -> answer = handleSetNumOfRooms(message);
            case PARSER_SETTINGS_FILLED -> answer = handleParserSettingsFilled(message);
            default -> answer = getDefaultAnswer(message);
        }
        return answer;
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
            UserParserSettings settings = new UserParserSettings();
            settings.setLanguage(lang);
            settings.setUserId(userId);
            userParserSettingsInMemoryService.save(userId, settings);
            userCache.setBotState(userId, BotState.SET_MIN_PRICE);
            return SendMessage.builder()
                    .text(EmojiParser.parseToUnicode("Select city :city_sunset:"))
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

    private SendMessage handleSetMinPrice(Message message) {
        long chatId = message.getChatId();
        long userId = message.getFrom().getId();
        String city = message.getText();

        List<String> cities = List.of("warsaw", "krakow", "lodz", "wroclaw", "poznan", "gdansk", "szczecin", "bydgoszcz");
        if (cities.contains(city.toLowerCase())) {
            UserParserSettings old = userParserSettingsInMemoryService.get(userId);
            old.setCity(city);
            userParserSettingsInMemoryService.update(userId, old);
            userCache.setBotState(userId, BotState.SET_MAX_PRICE);
            ReplyKeyboardRemove keyboardRemove = new ReplyKeyboardRemove();
            keyboardRemove.setRemoveKeyboard(true);
            return SendMessage.builder()
                    .text(EmojiParser.parseToUnicode("Enter the minimum price in PLN :dollar:"))
                    .chatId(chatId)
                    .replyMarkup(removeKeyboard())
                    .build();
        } else {
            return getDefaultAnswer(message);
        }
    }

    private SendMessage handleSetMaxPrice(Message message) {
        long chatId = message.getChatId();
        long userId = message.getFrom().getId();
        int minPrice;
        try {
            minPrice = (int) Float.parseFloat(message.getText());
            if (minPrice < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            return SendMessage.builder()
                    .chatId(chatId)
                    .text(EmojiParser.parseToUnicode("Enter the minimum price in PLN :dollar:"))
                    .build();
        }
        UserParserSettings old = userParserSettingsInMemoryService.get(userId);
        old.setMinPrice(minPrice);
        userParserSettingsInMemoryService.update(userId, old);

        userCache.setBotState(userId, BotState.SET_AD_TYPE);

        return SendMessage.builder()
                .chatId(chatId)
                .text(EmojiParser.parseToUnicode("Enter the maximum price in PLN :dollar:"))
                .build();
    }

    private SendMessage handleSetAdType(Message message) {
        long chatId = message.getChatId();
        long userId = message.getFrom().getId();
        int maxPrice;
        UserParserSettings old = userParserSettingsInMemoryService.get(userId);
        try {
            maxPrice = (int) Float.parseFloat(message.getText());
            if (maxPrice < 0) throw new NumberFormatException();
            if (maxPrice <= old.getMinPrice()) {
                return SendMessage.builder()
                        .chatId(chatId)
                        .text("Maximum price must be bigger than minimum.")
                        .build();
            }
        } catch (NumberFormatException e) {
            return SendMessage.builder()
                    .chatId(chatId)
                    .text(EmojiParser.parseToUnicode("Enter the maximum price in PLN :dollar:"))
                    .build();
        }
        old.setMaxPrice(maxPrice);
        userParserSettingsInMemoryService.update(userId, old);

        userCache.setBotState(userId, BotState.SET_NUM_OF_ROOMS);

        return SendMessage.builder()
                .chatId(chatId)
                .text(EmojiParser.parseToUnicode("Select ad type :memo:"))
                .replyMarkup(adTypeKeyboard())
                .build();
    }

    private ReplyKeyboardMarkup adTypeKeyboard() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);

        KeyboardButton owner = new KeyboardButton("Only from the owners");
        KeyboardButton agency = new KeyboardButton("Agencies only");
        KeyboardButton ownerAndAgency = new KeyboardButton("Owners + Agencies");

        List<KeyboardRow> keyboard = List.of(new KeyboardRow(List.of(owner, agency)),
                new KeyboardRow(List.of(ownerAndAgency)));
        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

    private SendMessage handleSetNumOfRooms(Message message) {
        long chatId = message.getChatId();
        long userId = message.getFrom().getId();
        String adType = message.getText();
        List<String> adTypes = List.of("Only from the owners", "Agencies only", "Owners + Agencies");

        if (adTypes.contains(adType)) {
            userCache.setBotState(userId, BotState.PARSER_SETTINGS_FILLED);

            UserParserSettings old = userParserSettingsInMemoryService.get(userId);
            if (adType.equals(adTypes.get(0))) {
                old.setAdType("private");
            } else if (adType.equals(adTypes.get(1))) {
                old.setAdType("business");
            }
            userParserSettingsInMemoryService.update(userId, old);
            return SendMessage.builder()
                    .chatId(chatId)
                    .text(EmojiParser.parseToUnicode("Select number of rooms :door:"))
                    .replyMarkup(numOfRoomsKeyboard())
                    .build();
        } else {
            return getDefaultAnswer(message);
        }
    }

    private ReplyKeyboardMarkup numOfRoomsKeyboard() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);

        KeyboardButton one = new KeyboardButton("1 room");
        KeyboardButton two = new KeyboardButton("2 rooms");
        KeyboardRow row1 = new KeyboardRow(List.of(one, two));

        KeyboardButton three = new KeyboardButton("3 rooms");
        KeyboardButton four = new KeyboardButton("4 rooms");
        KeyboardRow row2 = new KeyboardRow(List.of(three, four));

        KeyboardButton oneTwo = new KeyboardButton("1 - 2 rooms");
        KeyboardButton twoThree = new KeyboardButton("2 - 3 rooms");
        KeyboardRow row3 = new KeyboardRow(List.of(oneTwo, twoThree));

        KeyboardButton threeFour = new KeyboardButton("3 - 4 rooms");
        KeyboardButton moreThanFour = new KeyboardButton("4 and more rooms");
        KeyboardRow row4 = new KeyboardRow(List.of(threeFour, moreThanFour));

        List<KeyboardRow> keyboard = List.of(row1, row2, row3, row4);
        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

    private SendMessage handleParserSettingsFilled(Message message) {
        long chatId = message.getChatId();
        long userId = message.getFrom().getId();
        String numberOfRooms = message.getText();
        List<String> correctNumbersOfRooms = List.of("1 room", "2 rooms", "3 rooms", "4 rooms", "1 - 2 rooms", "2 - 3 rooms", "3 - 4 rooms", "4 and more rooms");
        if (correctNumbersOfRooms.contains(numberOfRooms)) {
            NumberOfRoomEnum[] userNumOfRooms = getRoomsFromMessage(numberOfRooms, correctNumbersOfRooms);
            UserParserSettings userParserSettings = userParserSettingsInMemoryService.get(userId);
            userParserSettingsService.save(setRoomsToUserSettings(userParserSettings, userNumOfRooms));
            userCache.setBotState(userId, BotState.WAIT_FOR_FLAT_SEARCH);

            return SendMessage.builder()
                    .chatId(chatId)
                    .text(EmojiParser.parseToUnicode("Complete!\nWaiting for new ads :wink:"))
                    .parseMode(ParseMode.HTML)
                    .replyMarkup(getMainKeyboard())
                    .build();
        } else {
            return getDefaultAnswer(message);
        }
    }

    private ReplyKeyboardMarkup getMainKeyboard() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setIsPersistent(false);

        KeyboardButton botSettings = new KeyboardButton(EmojiParser.parseToUnicode("Bot settings :gear:"));
        KeyboardButton donate = new KeyboardButton(EmojiParser.parseToUnicode("Donate to author \uD83E\uDEF6"));

        KeyboardRow row1 = new KeyboardRow(List.of(botSettings, donate));
        replyKeyboardMarkup.setKeyboard(List.of(row1));
        return replyKeyboardMarkup;
    }

    private UserParserSettings setRoomsToUserSettings(UserParserSettings userParserSettings, NumberOfRoomEnum[] numberOfRooms) {
        Set<NumberOfRoom> rooms = numberOfRoomService.getAllById(numberOfRooms);
        userParserSettings.setNumberOfRooms(rooms);
        return userParserSettings;
    }

    private NumberOfRoomEnum[] getRoomsFromMessage(String rooms, List<String> correctNumbersOfRooms) {
        Map<String, NumberOfRoomEnum[]> mapForParsing = getMapForParsing(correctNumbersOfRooms);
        return mapForParsing.get(rooms);
    }

    private Map<String, NumberOfRoomEnum[]> getMapForParsing(List<String> correctNumbersOfRooms) {
        Map<String, NumberOfRoomEnum[]> mapForParsing = new HashMap<>();
        // "1 room", "2 rooms", "3 rooms", "4 rooms", "1 - 2 rooms", "2 - 3 rooms", "3 - 4 rooms", "4 and more rooms"
        int i = 0;
        mapForParsing.put(correctNumbersOfRooms.get(i++), new NumberOfRoomEnum[]{NumberOfRoomEnum.one});
        mapForParsing.put(correctNumbersOfRooms.get(i++), new NumberOfRoomEnum[]{NumberOfRoomEnum.two});
        mapForParsing.put(correctNumbersOfRooms.get(i++), new NumberOfRoomEnum[]{NumberOfRoomEnum.three});
        mapForParsing.put(correctNumbersOfRooms.get(i++), new NumberOfRoomEnum[]{NumberOfRoomEnum.four});

        mapForParsing.put(correctNumbersOfRooms.get(i++), new NumberOfRoomEnum[]{NumberOfRoomEnum.one, NumberOfRoomEnum.two});
        mapForParsing.put(correctNumbersOfRooms.get(i++), new NumberOfRoomEnum[]{NumberOfRoomEnum.two, NumberOfRoomEnum.three});
        mapForParsing.put(correctNumbersOfRooms.get(i++), new NumberOfRoomEnum[]{NumberOfRoomEnum.three, NumberOfRoomEnum.four});
        mapForParsing.put(correctNumbersOfRooms.get(i), new NumberOfRoomEnum[]{NumberOfRoomEnum.four});
        return mapForParsing;
    }

    private ReplyKeyboardRemove removeKeyboard() {
        return ReplyKeyboardRemove.builder()
                .removeKeyboard(true)
                .selective(true)
                .build();
    }

    private SendMessage getDefaultAnswer(Message message) {
        return SendMessage.builder()
                .text("Please, select again.")
                .chatId(message.getChatId())
                .replyMarkup(message.getReplyMarkup())
                .build();
    }

    @Override
    public BotState getHandlerName() {
        return BotState.FILLING_PARSER_SETTINGS;
    }
}
