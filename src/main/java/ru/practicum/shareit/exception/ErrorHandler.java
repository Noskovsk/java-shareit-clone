package ru.practicum.shareit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.request.ItemRequestController;

import javax.validation.ConstraintViolationException;
import java.util.Map;

@RestControllerAdvice(assignableTypes = {BookingController.class, ItemRequestController.class})
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handle(final IncorrectStatusException e) {
        return Map.of("error", "Unknown state: " + e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handle(final ConstraintViolationException e) {
        return Map.of("error", "Validate error: " + e.getMessage());
    }
}
