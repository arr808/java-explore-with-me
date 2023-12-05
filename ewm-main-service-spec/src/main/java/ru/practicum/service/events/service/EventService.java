package ru.practicum.service.events.service;

import ru.practicum.service.events.dto.EventFullDto;
import ru.practicum.service.events.dto.EventShortDto;
import ru.practicum.service.events.dto.NewEventDto;
import ru.practicum.service.events.dto.UpdateEventDto;
import ru.practicum.service.events.model.EventStatus;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface EventService {

    //admin
    List<EventFullDto> getAll(List<Long> users, List<EventStatus> states, List<Long> categories,
                              LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size);

    //private
    List<EventShortDto> getAll(long userId, int from, int size);

    //public
    List<EventShortDto> getAll(String text, List<Long> categories, Boolean paid,
                               LocalDateTime rangeStart, LocalDateTime rangeEnd, boolean onlyAvailable,
                               String sort, int from, int size, HttpServletRequest request);

    //private
    EventFullDto get(long userId, long eventId);

    //public
    EventFullDto get(long eventId, HttpServletRequest request);

    //private
    EventFullDto add(long userId, NewEventDto newEventDto);

    //admin
    EventFullDto update(long eventId, UpdateEventDto updateEventDto);

    //private
    EventFullDto update(long userId, long eventId, UpdateEventDto updateEventDto);
}
