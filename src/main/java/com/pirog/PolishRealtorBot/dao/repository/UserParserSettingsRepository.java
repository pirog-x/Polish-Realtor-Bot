package com.pirog.PolishRealtorBot.dao.repository;

import com.pirog.PolishRealtorBot.dao.entity.UserParserSettings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserParserSettingsRepository extends JpaRepository<UserParserSettings, Long> {
}
