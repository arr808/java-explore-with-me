package ru.practicum.service.util.exceptions;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ForbiddenException extends RuntimeException {
    private LocalDateTime timestamp;

    public ForbiddenException(String message) {
        super(message);
        timestamp = LocalDateTime.now();
    }
}
