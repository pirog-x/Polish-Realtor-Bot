package com.pirog.PolishRealtorBot.service;

import com.pirog.PolishRealtorBot.dao.entity.UserParserSettings;
import com.pirog.PolishRealtorBot.dao.repository.UserParserSettingsInMemoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserParserSettingsInMemoryService {

    @Autowired
    private UserParserSettingsInMemoryRepository memoryRepository;

    public void save(long userId, UserParserSettings userSettings) {
        memoryRepository.save(userId, userSettings);
    }

    public UserParserSettings get(long userId) {
        return memoryRepository.find(userId);
    }

    public void update(long userId, UserParserSettings userParserSettings) {
        memoryRepository.update(userId, userParserSettings);
    }

    public void delete(long userId) {
        memoryRepository.delete(userId);
    }
}
