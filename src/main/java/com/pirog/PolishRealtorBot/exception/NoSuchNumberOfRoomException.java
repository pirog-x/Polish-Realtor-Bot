package com.pirog.PolishRealtorBot.exception;

import com.pirog.PolishRealtorBot.dao.entity.NumberOfRoomEnum;

public class NoSuchNumberOfRoomException extends RuntimeException {
    public NoSuchNumberOfRoomException(NumberOfRoomEnum id) {
        super("The number of room:'" + id + "' not found.");
    }
}
