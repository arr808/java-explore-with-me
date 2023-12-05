package ru.practicum.service.events.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.service.events.dto.EventFullDto;
import ru.practicum.service.events.dto.EventShortDto;
import ru.practicum.service.events.service.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/events")
public class PublicEventController {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final EventService eventService;

    @Autowired
    public PublicEventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public List<EventShortDto> getAll(@RequestParam(required = false) String text,
                                      @RequestParam(required = false) List<Long> categories,
                                      @RequestParam(required = false) Boolean paid,
                                      @RequestParam(required = false) String rangeStart,
                                      @RequestParam(required = false) String rangeEnd,
                                      @RequestParam(required = false, defaultValue = "false") boolean onlyAvailable,
                                      @RequestParam(required = false) String sort,
                                      @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                      @Positive @RequestParam(defaultValue = "10") int size,
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

    @GetMapping("/{id}")
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
