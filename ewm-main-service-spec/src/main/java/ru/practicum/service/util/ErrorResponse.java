package ru.practicum.service.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class ErrorResponse {

    private String status;
    private String reason;
    private String message;
    private LocalDateTime timestamp;
}
