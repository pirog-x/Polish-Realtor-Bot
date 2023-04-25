package com.pirog.PolishRealtorBot.service;

import com.pirog.PolishRealtorBot.dao.entity.UserParserSettings;
import com.pirog.PolishRealtorBot.dao.repository.UserParserSettingsRepository;
import com.pirog.PolishRealtorBot.exception.NoSuchUserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserParserSettingsService {

    @Autowired
    private UserParserSettingsRepository repository;

    static UserParserSettings unwrapUserParserSettings(Optional<UserParserSettings> settings, long userId) {
        if (settings.isEmpty()) throw new NoSuchUserException(userId);
        else return settings.get();
    }

    public void save(UserParserSettings userParserSettings) {
        repository.save(userParserSettings);
    }

    public void update(long userId, UserParserSettings another) {
        UserParserSettings userSettings = find(userId);
        if (!isValidSettings(another)) throw new IllegalArgumentException("Something is not correct in UserParserSettings entity.");

        userSettings.setLanguage(another.getLanguage());
        userSettings.setCity(another.getCity());
        userSettings.setMinPrice(another.getMinPrice());
        userSettings.setMaxPrice(another.getMaxPrice());
        userSettings.setAdType(another.getAdType());
        userSettings.setNumOfRooms(another.getNumOfRooms());

        repository.save(userSettings);
    }

    public UserParserSettings find(long userId) {
        return UserParserSettingsService.unwrapUserParserSettings(repository.findById(userId), userId);
    }

    public void delete(long userId) {
        repository.deleteById(userId);
    }

    private boolean isValidSettings(UserParserSettings settings) {
        return (settings.getUserId().describeConstable().isPresent() &&
                !settings.getLanguage().isBlank() &&
                !settings.getCity().isBlank() &&
                settings.getNumOfRooms().describeConstable().isPresent()
        ) && (
                settings.getMinPrice() <= settings.getMaxPrice() &&
                settings.getNumOfRooms() <= 4
        );
    }
}
