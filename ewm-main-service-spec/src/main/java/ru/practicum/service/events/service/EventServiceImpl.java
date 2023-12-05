package ru.practicum.service.events.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.client.StatClient;
import ru.practicum.dto.ShortStatDto;
import ru.practicum.service.categories.model.Category;
import ru.practicum.service.categories.repository.CategoryRepository;
import ru.practicum.service.events.dto.EventFullDto;
import ru.practicum.service.events.dto.EventShortDto;
import ru.practicum.service.events.dto.NewEventDto;
import ru.practicum.service.events.dto.UpdateEventDto;
import ru.practicum.service.events.model.Event;
import ru.practicum.service.events.model.EventStatus;
import ru.practicum.service.events.repository.EventRepository;
import ru.practicum.service.events.repository.LocationRepository;
import ru.practicum.service.users.model.User;
import ru.practicum.service.users.repository.UserRepository;
import ru.practicum.service.util.Mapper;
import ru.practicum.service.util.PaginationAndSortParams;
import ru.practicum.service.util.exceptions.ForbiddenException;
import ru.practicum.service.util.exceptions.IncorrectlyRequestException;
import ru.practicum.service.util.exceptions.NotFoundException;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;
    private final StatClient statClient;

    @Autowired
    public EventServiceImpl(EventRepository eventRepository, UserRepository userRepository,
                            CategoryRepository categoryRepository, LocationRepository locationRepository,
                            StatClient statClient) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.locationRepository = locationRepository;
        this.statClient = statClient;
    }

    //admin
    @Override
    @Transactional(readOnly = true)
    public List<EventFullDto> getAll(List<Long> userIds, List<EventStatus> states, List<Long> categories,
                                     LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size) {
        Pageable pageRequest = PaginationAndSortParams.getPageable(from, size);

        if (userIds != null || states != null || categories != null || rangeStart != null || rangeEnd != null) {
            return eventRepository.getAll(userIds, states, categories, rangeStart, rangeEnd, pageRequest).stream()
                    .peek(event -> event.setViews(getViews(event)))
                    .map(Mapper::toDto)
                    .collect(Collectors.toList());
        }
        return eventRepository.findAll(pageRequest).stream()
                .peek(event -> event.setViews(getViews(event)))
                .map(Mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto update(long eventId, UpdateEventDto updateEventDto) {
        LocalDateTime timestamp = LocalDateTime.now();
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException(String.format("Event with id=%d was not found", eventId)));
        if (event.getState().equals(EventStatus.PUBLISHED)) {
            throw new IncorrectlyRequestException("Only pending or canceled events can be changed");
        } else if (!event.getState().equals(EventStatus.PENDING)) {
            throw new IncorrectlyRequestException("Cannot publish the event because it's not in the right state: PUBLISHED");
        }

        if (updateEventDto.getEventDate() != null && updateEventDto.getEventDate().isBefore(timestamp.minusHours(1))) {
            throw new ForbiddenException("The event does not satisfy the editing rules");
        }
        long categoryId;
        if (updateEventDto.getCategory() == 0) {
            categoryId = event.getCategory().getId();
        } else categoryId = updateEventDto.getCategory();
        Category category = categoryRepository.findById(categoryId).orElseThrow(() ->
                new NotFoundException(String.format("Category with id=%d was not found", categoryId)));
        if (updateEventDto.getLocation() != null) {
            locationRepository.save(updateEventDto.getLocation());
        }
        Event updateEvent = Mapper.fromUpdateDto(event, updateEventDto, category);
        updateEvent.setViews(getViews(updateEvent));
        return Mapper.toDto(eventRepository.save(updateEvent));
    }

    //private
    @Override
    @Transactional(readOnly = true)
    public List<EventShortDto> getAll(long userId, int from, int size) {
        Pageable pageRequest = PaginationAndSortParams.getPageable(from, size);
        return eventRepository.findAllByInitiatorId(userId, pageRequest).stream()
                .peek(event -> event.setViews(getViews(event)))
                .map(Mapper::toShortDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public EventFullDto get(long userId, long eventId) {
        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId).orElseThrow(() ->
                new NotFoundException(String.format("Event with id=%d was not found", eventId)));
        event.setViews(getViews(event));
        return Mapper.toDto(event);
    }

    @Override
    public EventFullDto add(long userId, NewEventDto newEventDto) {
        LocalDateTime timestamp = LocalDateTime.now();
        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException(String.format("User with id=%d was not found", userId)));

        long categoryId = newEventDto.getCategory();
        Category category = categoryRepository.findById(categoryId).orElseThrow(() ->
                new NotFoundException(String.format("Category with id=%d was not found", categoryId)));
        locationRepository.save(newEventDto.getLocation());
        Event event = Mapper.fromDto(newEventDto, user, category, timestamp);
        if (newEventDto.getRequestModeration() == null) {
            event.setRequestModeration(true);
        } else event.setRequestModeration(newEventDto.getRequestModeration());
        return Mapper.toDto(eventRepository.save(event));
    }

    @Override
    public EventFullDto update(long userId, long eventId, UpdateEventDto updateEventDto) {
        LocalDateTime timestamp = LocalDateTime.now();
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException(String.format("Event with id=%d was not found", eventId)));

        if (event.getState().equals(EventStatus.PUBLISHED)) {
            throw new IncorrectlyRequestException("Only pending or canceled events can be changed");
        }

        long categoryId;
        if (updateEventDto.getCategory() == 0) {
            categoryId = event.getCategory().getId();
        } else categoryId = updateEventDto.getCategory();
        Category category = categoryRepository.findById(categoryId).orElseThrow(() ->
                new NotFoundException(String.format("Category with id=%d was not found", categoryId)));
        if (updateEventDto.getLocation() != null) {
            locationRepository.save(updateEventDto.getLocation());
        }
        Event updateEvent = Mapper.fromUpdateDto(event, updateEventDto, category);
        updateEvent.setViews(getViews(updateEvent));
        return Mapper.toDto(eventRepository.save(updateEvent));
    }

    //public
    @Override
    @Transactional(readOnly = true)
    public List<EventShortDto> getAll(String text, List<Long> categories, Boolean paid,
                               LocalDateTime rangeStart, LocalDateTime rangeEnd, boolean onlyAvailable,
                               String sort, int from, int size, HttpServletRequest request) {
        if (rangeEnd.isBefore(rangeStart)) throw new ForbiddenException("");

        statClient.sendPost("ewm-main-service", request.getRequestURI(), request.getRemoteAddr(), LocalDateTime.now());

        Pageable pageRequest = PaginationAndSortParams.getPageableAsc(from, size, sort);
        return eventRepository.getAll(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, pageRequest).stream()
                .peek(event -> event.setViews(getViews(event)))
                .map(Mapper::toShortDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public EventFullDto get(long eventId, HttpServletRequest request) {
        Event event = eventRepository.findByIdAndState(eventId, EventStatus.PUBLISHED).orElseThrow(() ->
                new NotFoundException(String.format("Event with id=%d was not found", eventId)));

        statClient.sendPost("ewm-main-service", request.getRequestURI(), request.getRemoteAddr(), LocalDateTime.now());
        event.setViews(getViews(event));
        return Mapper.toDto(event);
    }

    private long getViews(Event event) {
        String uri = "/events/" + event.getId();

        List<ShortStatDto> stats = statClient.getStats(event.getPublishedOn().minusMinutes(1), LocalDateTime.now(), new String[]{uri}, true).getBody();
        return stats.size();
    }
}
