package ru.practicum.shareit.booking.exception;

public class IncorrectStatusException extends RuntimeException {
    public IncorrectStatusException(String message) {
        super(message);
    }
}
