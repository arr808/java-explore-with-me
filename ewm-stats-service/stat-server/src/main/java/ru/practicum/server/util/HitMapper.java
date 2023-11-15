package ru.practicum.server.util;

import lombok.experimental.UtilityClass;

import ru.practicum.dto.StatDto;
import ru.practicum.server.model.EndpointHit;

@UtilityClass
public class HitMapper {

    public static StatDto toDto(EndpointHit hit) {
        return StatDto.builder()
                .app(hit.getApp())
                .uri(hit.getUri())
                .ip(hit.getIp())
                .timestamp(hit.getTimestamp())
                .build();
    }

    public static EndpointHit fromDto(StatDto statDto) {
        return EndpointHit.builder()
                .app(statDto.getApp())
                .uri(statDto.getUri())
                .ip(statDto.getIp())
                .timestamp(statDto.getTimestamp())
                .build();
    }
}
