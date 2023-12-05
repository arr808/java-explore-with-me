package ru.practicum.service.util.exceptions;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class NotFoundException extends RuntimeException {
    private LocalDateTime timestamp;

    public NotFoundException(String message) {
        super(message);
        timestamp = LocalDateTime.now();
    }
}
