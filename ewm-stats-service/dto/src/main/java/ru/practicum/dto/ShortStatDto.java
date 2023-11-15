package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ShortStatDto {
    private String app;
    private String uri;
    private long hits;
}
