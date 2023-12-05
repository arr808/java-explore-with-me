package ru.practicum.service.events.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.service.events.dto.EventFullDto;
import ru.practicum.service.events.dto.UpdateEventDto;
import ru.practicum.service.events.model.EventStatus;
import ru.practicum.service.events.service.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin/events")
public class AdminEventController {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final EventService eventService;

    @Autowired
    public AdminEventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public List<EventFullDto> getAll(@RequestParam(required = false) List<Long> users,
                                     @RequestParam(required = false) List<EventStatus> states,
                                     @RequestParam(required = false) List<Long> categories,
                                     @RequestParam(required = false) String rangeStart,
                                     @RequestParam(required = false) String rangeEnd,
                                     @PositiveOrZero @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                     @Positive @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("Получен запрос GET /admin/events/?" +
                        "users={}&states={}&categories={}&rangeStart={}&rangeEnd={}&from={}&size={}",
                users, states, categories, rangeStart, rangeEnd, from, size);
        return eventService.getAll(users, states, categories, getDate(rangeStart), getDate(rangeEnd), from, size);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto update(@PathVariable long eventId,
                               @Valid @RequestBody UpdateEventDto updateEventDto) {
        log.info("Получен запрос PATCH /admin/events/{} {}", eventId, updateEventDto);
        return eventService.update(eventId, updateEventDto);
    }

    private LocalDateTime getDate(String dateStr) {
        if (dateStr != null) {
            return LocalDateTime.parse(dateStr, FORMATTER);
        }
        return null;
    }
}
