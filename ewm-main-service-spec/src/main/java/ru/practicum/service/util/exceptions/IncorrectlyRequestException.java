package ru.practicum.service.util.exceptions;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class IncorrectlyRequestException extends RuntimeException {
    private LocalDateTime timestamp;

    public IncorrectlyRequestException(String message) {
        super(message);
        timestamp = LocalDateTime.now();
    }
}
