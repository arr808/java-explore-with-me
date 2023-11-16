package ru.practicum.server.service;

import ru.practicum.dto.ShortStatDto;
import ru.practicum.dto.StatDto;

import java.time.LocalDateTime;
import java.util.List;

public interface ServerService {

    List<ShortStatDto> get(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);

    StatDto add(StatDto statDto);
}
