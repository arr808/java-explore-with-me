package ru.practicum.service.util;

import lombok.experimental.UtilityClass;
import ru.practicum.service.categories.dto.CategoryDto;
import ru.practicum.service.categories.dto.NewCategoryDto;
import ru.practicum.service.categories.model.Category;
import ru.practicum.service.comments.dto.CommentDto;
import ru.practicum.service.comments.dto.NewCommentDto;
import ru.practicum.service.comments.model.Comment;
import ru.practicum.service.comments.model.CommentStatus;
import ru.practicum.service.compilations.dto.CompilationDto;
import ru.practicum.service.compilations.dto.NewCompilationDto;
import ru.practicum.service.compilations.model.Compilation;
import ru.practicum.service.events.dto.EventFullDto;
import ru.practicum.service.events.dto.EventShortDto;
import ru.practicum.service.events.dto.LocationDto;
import ru.practicum.service.events.dto.NewEventDto;
import ru.practicum.service.events.dto.UpdateEventDto;
import ru.practicum.service.events.model.Event;
import ru.practicum.service.events.model.EventStateAction;
import ru.practicum.service.events.model.EventStatus;
import ru.practicum.service.events.model.Location;
import ru.practicum.service.requests.dto.ParticipationRequestDto;
import ru.practicum.service.requests.model.ParticipationRequest;
import ru.practicum.service.users.dto.NewUserDto;
import ru.practicum.service.users.dto.UserDto;
import ru.practicum.service.users.dto.UserShortDto;
import ru.practicum.service.users.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class Mapper {

    //User
    public static UserDto toDto(User user) {
        return UserDto.builder()
                .email(user.getEmail())
                .id(user.getId())
                .name(user.getName())
                .build();
    }

    public static UserShortDto toShortDto(User user) {
        return UserShortDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }

    public static User fromDto(NewUserDto newUserDto) {
        return User.builder()
                .email(newUserDto.getEmail())
                .name(newUserDto.getName())
                .build();
    }

    //Category
    public static CategoryDto toDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public static Category fromDto(NewCategoryDto updateDto) {
        return Category.builder()
                .name(updateDto.getName())
                .build();
    }

    public static Category fromDto(CategoryDto categoryDto) {
        return Category.builder()
                .id(categoryDto.getId())
                .name(categoryDto.getName())
                .build();
    }

    //Event
    public static EventFullDto toDto(Event event) {
        return EventFullDto.builder()
                .annotation(event.getAnnotation())
                .category(toDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .id(event.getId())
                .initiator(toDto(event.getInitiator()))
                .location(toDto(event.getLocation()))
                .paid(event.isPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.isRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }

    public static EventShortDto toShortDto(Event event) {
        return EventShortDto.builder()
                .annotation(event.getAnnotation())
                .category(toDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .eventDate(event.getEventDate())
                .id(event.getId())
                .initiator(toDto(event.getInitiator()))
                .paid(event.isPaid())
                .title(event.getTitle())
                .build();
    }

    public static Event fromDto(NewEventDto newEventDto, User user, Category category, LocalDateTime timestamp) {
        return Event.builder()
                .annotation(newEventDto.getAnnotation())
                .category(category)
                .description(newEventDto.getDescription())
                .eventDate(newEventDto.getEventDate())
                .location(newEventDto.getLocation())
                .paid(newEventDto.isPaid())
                .participantLimit(newEventDto.getParticipantLimit())
                .title(newEventDto.getTitle())
                .initiator(user)
                .createdOn(timestamp)
                .publishedOn(timestamp)
                .state(EventStatus.PENDING)
                .build();
    }

    public static Event fromUpdateDto(Event event, UpdateEventDto updateEventDto, Category category) {
        if (updateEventDto.getAnnotation() != null) {
            event.setAnnotation(updateEventDto.getAnnotation());
        }

        if (category != null) {
            event.setCategory(category);
        }

        if (updateEventDto.getDescription() != null) {
            event.setDescription(updateEventDto.getDescription());
        }

        if (updateEventDto.getEventDate() != null) {
            event.setEventDate(updateEventDto.getEventDate());
        }

        if (updateEventDto.getLocation() != null) {
            event.setLocation(updateEventDto.getLocation());
        }

        if (updateEventDto.getPaid() != null) {
            event.setPaid(updateEventDto.getPaid());
        }

        if (updateEventDto.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventDto.getParticipantLimit());
        }

        if (updateEventDto.getRequestModeration() != null) {
            event.setRequestModeration(updateEventDto.getRequestModeration());
        }

        EventStateAction updateState = updateEventDto.getStateAction();
        if (updateState != null) {
            if (updateState.equals(EventStateAction.PUBLISH_EVENT)) event.setState(EventStatus.PUBLISHED);
            else if (updateState.equals(EventStateAction.REJECT_EVENT)) event.setState(EventStatus.CANCELED);
            else if (updateState.equals(EventStateAction.CANCEL_REVIEW)) event.setState(EventStatus.CANCELED);
            else event.setState(EventStatus.PENDING);
        }

        if (updateEventDto.getTitle() != null) {
            event.setTitle(updateEventDto.getTitle());
        }
        return event;
    }

    //Location
    public static LocationDto toDto(Location location) {
        return LocationDto.builder()
                .lon(location.getLon())
                .lat(location.getLat())
                .build();
    }

    //Request
    public static ParticipationRequestDto toDto(ParticipationRequest request) {
        return ParticipationRequestDto.builder()
                .created(request.getCreated())
                .event(request.getEvent().getId())
                .id(request.getId())
                .requester(request.getRequester().getId())
                .status(request.getStatus())
                .build();
    }

    //Compilation
    public static Compilation fromDto(NewCompilationDto newCompilationDto) {
        return Compilation.builder()
                .pinned(newCompilationDto.isPinned())
                .title(newCompilationDto.getTitle())
                .build();
    }

    public static CompilationDto toDto(Compilation compilation) {
        List<Event> events = compilation.getEvents();
        if (events != null) {
            return CompilationDto.builder()
                    .id(compilation.getId())
                    .events(events.stream().map(Mapper::toShortDto).collect(Collectors.toList()))
                    .pinned(compilation.getPinned())
                    .title(compilation.getTitle())
                    .build();
        } else
        return CompilationDto.builder()
                .id(compilation.getId())
                .events(new ArrayList<>())
                .pinned(compilation.getPinned())
                .title(compilation.getTitle())
                .build();
    }

    //Comment
    public static CommentDto toDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .author(toShortDto(comment.getAuthor()))
                .text(comment.getText())
                .created(comment.getCreated())
                .updated(comment.getUpdated())
                .status(comment.getState())
                .build();
    }

    public static Comment fromDto(Event event, User author, NewCommentDto newCommentDto) {
        return Comment.builder()
                .event(event)
                .author(author)
                .text(newCommentDto.getText())
                .created(LocalDateTime.now())
                .state(CommentStatus.PENDING)
                .build();
    }
}
