package com.pirog.PolishRealtorBot.exception;

public class NoSuchUserException extends RuntimeException {
    public NoSuchUserException(long userId) {
        super("The user with id '" + userId + "' does not exist.");
    }
}
