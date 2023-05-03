package com.pirog.PolishRealtorBot.service;

import com.pirog.PolishRealtorBot.dao.entity.NumberOfRoom;
import com.pirog.PolishRealtorBot.dao.entity.NumberOfRoomEnum;
import com.pirog.PolishRealtorBot.dao.repository.NumberOfRoomRepository;
import com.pirog.PolishRealtorBot.exception.NoSuchNumberOfRoomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class NumberOfRoomService {

    @Autowired
    private NumberOfRoomRepository numberOfRoomRepository;

    public NumberOfRoom get(NumberOfRoomEnum id) {
        return unwrapEntity(numberOfRoomRepository.findById(id), id);
    }

    public Set<NumberOfRoom> getAllById(NumberOfRoomEnum[] id) {
        Set<NumberOfRoom> result = new HashSet<>();
        for (NumberOfRoomEnum element : id) {
            result.add(unwrapEntity(numberOfRoomRepository.findById(element), element));
        }
        return result;
    }

    private NumberOfRoom unwrapEntity(Optional<NumberOfRoom> entity, NumberOfRoomEnum id) {
        if (entity.isPresent())
            return entity.get();
        else
            throw new NoSuchNumberOfRoomException(id);
    }
}
