package com.pirog.PolishRealtorBot.dao.repository;

import com.pirog.PolishRealtorBot.dao.entity.NumberOfRoom;
import com.pirog.PolishRealtorBot.dao.entity.NumberOfRoomEnum;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NumberOfRoomRepository extends JpaRepository<NumberOfRoom, NumberOfRoomEnum> {
}
