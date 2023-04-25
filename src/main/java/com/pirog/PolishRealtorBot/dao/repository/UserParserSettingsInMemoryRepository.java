package com.pirog.PolishRealtorBot.dao.repository;

import com.pirog.PolishRealtorBot.dao.entity.UserParserSettings;
import com.pirog.PolishRealtorBot.exception.NoSuchUserException;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class UserParserSettingsInMemoryRepository {
    private final Map<Long, UserParserSettings> database = new HashMap<>();

    public void save(long userId, UserParserSettings userSettings) {
        database.put(userId, userSettings);
    }

    public UserParserSettings find(long userId) {
        return database.get(userId);
    }

    public void update(long userId, UserParserSettings userParserSettings) {
        UserParserSettings existSettings = find(userId);
        if (existSettings == null) {
            throw new NoSuchUserException(userId);
        }
        save(userId, userParserSettings);
    }

    public void delete(long userId) {
        database.remove(userId);
    }
}
