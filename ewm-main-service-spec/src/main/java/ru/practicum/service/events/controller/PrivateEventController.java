package ru.practicum.service.events.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.service.events.dto.EventFullDto;
import ru.practicum.service.events.dto.EventShortDto;
import ru.practicum.service.events.dto.NewEventDto;
import ru.practicum.service.events.dto.UpdateEventDto;
import ru.practicum.service.events.service.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users/{userId}/events")
public class PrivateEventController {

    private final EventService eventService;

    @Autowired
    public PrivateEventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public List<EventShortDto> getAll(@PathVariable long userId,
                                      @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                      @Positive @RequestParam(defaultValue = "10") int size) {
        log.info("Получен запрос GET /users/{}/events/?from={}&size={}", userId, from, size);
        return eventService.getAll(userId, from, size);
    }

    @GetMapping("/{eventId}")
    public EventFullDto get(@PathVariable long userId,
                            @PathVariable long eventId) {
        log.info("Получен запрос GET /users/{}/events/{}", userId, eventId);
        return eventService.get(userId, eventId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto add(@PathVariable long userId,
                            @Valid @RequestBody NewEventDto newEventDto) {
        log.info("Получен запрос POST /users/{}/events", userId);
        return eventService.add(userId, newEventDto);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto update(@PathVariable long userId,
                               @PathVariable long eventId,
                               @Valid @RequestBody UpdateEventDto updateEventDto) {
        log.info("Получен запрос PATCH /users/{}/events/{}", userId, eventId);
        return eventService.update(userId, eventId, updateEventDto);
    }
}
