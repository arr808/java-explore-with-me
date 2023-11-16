package ru.practicum.server.util;

import lombok.experimental.UtilityClass;

import ru.practicum.dto.StatDto;
import ru.practicum.server.model.Stat;

@UtilityClass
public class HitMapper {

    public static StatDto toDto(Stat hit) {
        return StatDto.builder()
                .app(hit.getApp())
                .uri(hit.getUri())
                .ip(hit.getIp())
                .timestamp(hit.getTimestamp())
                .build();
    }

    public static Stat fromDto(StatDto statDto) {
        return Stat.builder()
                .app(statDto.getApp())
                .uri(statDto.getUri())
                .ip(statDto.getIp())
                .timestamp(statDto.getTimestamp())
                .build();
    }
}
