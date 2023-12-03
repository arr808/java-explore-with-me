package ru.practicum.service.events.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.service.events.dto.EventFullDto;
import ru.practicum.service.events.dto.EventShortDto;
import ru.practicum.service.events.dto.NewEventDto;
import ru.practicum.service.events.dto.UpdateEventDto;
import ru.practicum.service.events.model.EventStatus;
import ru.practicum.service.events.service.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@RestController
public class EventController {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/admin/events")
    public List<EventFullDto> getAll(@RequestParam(required = false) List<Long> users,
                                     @RequestParam(required = false) List<EventStatus> states,
                                     @RequestParam(required = false) List<Long> categories,
                                     @RequestParam(required = false) String rangeStart,
                                     @RequestParam(required = false) String rangeEnd,
                                     @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                     @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("Получен запрос GET /admin/events/?" +
                        "users={}&states={}&categories={}&rangeStart={}&rangeEnd={}&from={}&size={}",
                users, states, categories, rangeStart, rangeEnd, from, size);
        return eventService.getAll(users, states, categories, getDate(rangeStart), getDate(rangeEnd), from, size);
    }

    @PatchMapping("/admin/events/{eventId}")
    public EventFullDto update(@PathVariable long eventId,
                               @Valid @RequestBody UpdateEventDto updateEventDto) {
        log.info("Получен запрос PATCH /admin/events/{} {}", eventId, updateEventDto);
        return eventService.update(eventId, updateEventDto);
    }

    @GetMapping("/users/{userId}/events")
    public List<EventShortDto> getAll(@PathVariable long userId,
                                      @RequestParam(defaultValue = "0") int from,
                                      @RequestParam(defaultValue = "10") int size) {
        log.info("Получен запрос GET /users/{}/events/?from={}&size={}", userId, from, size);
        return eventService.getAll(userId, from, size);
    }

    @GetMapping("/users/{userId}/events/{eventId}")
    public EventFullDto get(@PathVariable long userId,
                            @PathVariable long eventId) {
        log.info("Получен запрос GET /users/{}/events/{}", userId, eventId);
        return eventService.get(userId, eventId);
    }

    @PostMapping("/users/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto add(@PathVariable long userId,
                            @Valid @RequestBody NewEventDto newEventDto) {
        log.info("Получен запрос POST /users/{}/events", userId);
        return eventService.add(userId, newEventDto);
    }

    @PatchMapping("/users/{userId}/events/{eventId}")
    public EventFullDto update(@PathVariable long userId,
                               @PathVariable long eventId,
                               @Valid @RequestBody UpdateEventDto updateEventDto) {
        log.info("Получен запрос PATCH /users/{}/events/{}", userId, eventId);
        return eventService.update(userId, eventId, updateEventDto);
    }

    @GetMapping("/events")
    public List<EventShortDto> getAll(@RequestParam(required = false) String text,
                                      @RequestParam(required = false) List<Long> categories,
                                      @RequestParam(required = false) Boolean paid,
                                      @RequestParam(required = false) String rangeStart,
                                      @RequestParam(required = false) String rangeEnd,
                                      @RequestParam(required = false, defaultValue = "false") boolean onlyAvailable,
                                      @RequestParam(required = false) String sort,
                                      @RequestParam(defaultValue = "0") int from,
                                      @RequestParam(defaultValue = "10") int size,
                                      HttpServletRequest request) {
        log.info("Получен запрос GET /events/?" +
                "text={}&categories={}&paid={}&rangeStart={}&rangeEnd={}&onlyAvailable={}&sort={}&from={}&size={}",
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
        if (rangeStart != null && rangeEnd != null) {
            return eventService.getAll(text, categories, paid, getDate(rangeStart), getDate(rangeEnd),
                    onlyAvailable, sort, from, size, request);
        } else {
            LocalDateTime timestamp = LocalDateTime.now();
            return eventService.getAll(text, categories, paid, timestamp, timestamp.plusYears(100),
                    onlyAvailable, sort, from, size, request);
        }

    }

    @GetMapping("/events/{id}")
    public EventFullDto get(@PathVariable(name = "id") long eventId, HttpServletRequest request) {
        log.info("Получен запрос GET /events/{}", eventId);
        return eventService.get(eventId, request);
    }

    private LocalDateTime getDate(String dateStr) {
        if (dateStr != null) {
            return LocalDateTime.parse(dateStr, FORMATTER);
        }
        return null;
    }
}
