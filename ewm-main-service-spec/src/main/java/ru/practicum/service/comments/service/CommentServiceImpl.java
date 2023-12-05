package ru.practicum.service.comments.service;

import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.service.comments.dto.CommentDto;
import ru.practicum.service.comments.dto.CommentFullDto;
import ru.practicum.service.comments.dto.NewCommentDto;
import ru.practicum.service.comments.dto.UpdateCommentDto;
import ru.practicum.service.comments.model.Comment;
import ru.practicum.service.comments.model.CommentStateAction;
import ru.practicum.service.comments.repository.CommentRepository;
import ru.practicum.service.events.model.Event;
import ru.practicum.service.events.repository.EventRepository;
import ru.practicum.service.requests.model.RequestStatus;
import ru.practicum.service.requests.repository.RequestRepository;
import ru.practicum.service.users.model.User;
import ru.practicum.service.users.repository.UserRepository;
import ru.practicum.service.util.Mapper;
import ru.practicum.service.util.exceptions.ForbiddenException;
import ru.practicum.service.util.exceptions.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository,
                              EventRepository eventRepository,
                              UserRepository userRepository,
                              RequestRepository requestRepository) {
        this.commentRepository = commentRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.requestRepository = requestRepository;
    }

    @Override
    public List<CommentFullDto> getAll(long eventId) {
        checkEvent(eventId);
        return commentRepository.findAllByEventId(eventId).stream()
                .map(Mapper::toFullDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentFullDto changeCommentStatus(long eventId, long commentId, CommentStateAction state) {
        return null;
    }

    @Override
    public void delete(long id) {
        commentRepository.findById(id).orElseThrow(() ->
                new NotFoundException(String.format("Comment with id=%d was not found", id)));
        commentRepository.deleteById(id);
    }

    @Override
    public List<CommentFullDto> getAll(long eventId, long userId) {
        Event event = checkEvent(eventId);
        checkUser(userId);

        if (event.getInitiator().getId() != userId) {
            throw new ForbiddenException("User is not initiator");
        }
        return commentRepository.findAllByEventId(eventId).stream()
                .map(Mapper::toFullDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto add(long eventId, long userId, NewCommentDto newCommentDto) {
        Event event = checkEvent(eventId);
        User user = checkUser(userId);
        checkParticipation(eventId, userId);
        Comment comment = Mapper.fromDto(event, user, newCommentDto);
        return Mapper.toDto(commentRepository.save(comment));
    }

    @Override
    public CommentDto update(long eventId, long userId, UpdateCommentDto updateCommentDto) {
        return null;
    }

    @Override
    public List<CommentDto> getAllByEventId(long eventId) {
        checkEvent(eventId);
        return commentRepository.findAllByEventId(eventId).stream()
                .map(Mapper::toDto)
                .collect(Collectors.toList());
    }

    private Event checkEvent (long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException(String.format("Event with id=%d was not found", eventId)));
    }

    private User checkUser(long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException(String.format("User with id=%d was not found", userId)));
    }

    private Comment checkComment(long commentId) {
        return commentRepository.findById(commentId).orElseThrow(() ->
                new NotFoundException(String.format("Comment with id=%d was not found", commentId)));
    }

    private void checkParticipation(long eventId, long userId) {
        requestRepository.findByEventIdRequesterIdAndStatus(eventId, userId, RequestStatus.CONFIRMED).orElseThrow(() ->
                new NotFoundException(String.format("User with id=%d did not participate in Event with id=%d", userId, eventId)));
    }
}
