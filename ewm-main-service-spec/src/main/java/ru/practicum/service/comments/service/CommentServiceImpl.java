package ru.practicum.service.comments.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.service.comments.dto.CommentDto;
import ru.practicum.service.comments.dto.NewCommentDto;
import ru.practicum.service.comments.dto.UpdateCommentDto;
import ru.practicum.service.comments.model.Comment;
import ru.practicum.service.comments.model.CommentStateAction;
import ru.practicum.service.comments.model.CommentStatus;
import ru.practicum.service.comments.repository.CommentRepository;
import ru.practicum.service.events.model.Event;
import ru.practicum.service.events.repository.EventRepository;
import ru.practicum.service.requests.model.RequestStatus;
import ru.practicum.service.requests.repository.RequestRepository;
import ru.practicum.service.users.model.User;
import ru.practicum.service.users.repository.UserRepository;

import ru.practicum.service.util.Mapper;
import ru.practicum.service.util.PaginationAndSortParams;
import ru.practicum.service.util.exceptions.ForbiddenException;
import ru.practicum.service.util.exceptions.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
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
    @Transactional(readOnly = true)
    public List<CommentDto> getAll(long eventId, int from, int size) {
        checkEvent(eventId);
        Pageable pageRequest = PaginationAndSortParams.getPageable(from, size);
        return commentRepository.findAllByEventId(eventId, pageRequest).stream()
                .map(Mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto changeCommentStatus(long eventId, long commentId, CommentStateAction state) {
        checkEvent(eventId);
        Comment comment = checkComment(commentId);
        if (state.equals(CommentStateAction.PUBLISH)) {
            comment.setState(CommentStatus.PUBLISHED);
        } else comment.setState(CommentStatus.REJECTED);
        return Mapper.toDto(commentRepository.save(comment));
    }

    @Override
    public void delete(long id) {
        commentRepository.findById(id).orElseThrow(() ->
                new NotFoundException(String.format("Comment with id=%d was not found", id)));
        commentRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> getAll(long eventId, long userId, int from, int size) {
        Event event = checkEvent(eventId);
        checkUser(userId);
        Pageable pageRequest = PaginationAndSortParams.getPageable(from, size);
        if (event.getInitiator().getId() != userId) {
            throw new ForbiddenException("User is not initiator");
        }
        return commentRepository.findAllByEventId(eventId, pageRequest).stream()
                .map(Mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CommentDto getPrivate(long userId, long commentId) {
        checkUser(userId);
        Comment comment = checkComment(commentId);
        checkAuthor(userId, comment);
        return Mapper.toDto(comment);
    }

    @Override
    public CommentDto add(long userId, NewCommentDto newCommentDto) {
        long eventId = newCommentDto.getEventId();
        Event event = checkEvent(eventId);
        User user = checkUser(userId);
        checkParticipation(eventId, userId);
        Comment comment = Mapper.fromDto(event, user, newCommentDto);
        return Mapper.toDto(commentRepository.save(comment));
    }

    @Override
    public CommentDto update(long userId, UpdateCommentDto updateCommentDto) {
        long eventId = updateCommentDto.getEventId();
        checkEvent(eventId);
        checkUser(userId);
        Comment comment = checkComment(updateCommentDto.getId());
        checkAuthor(userId, comment);

        comment.setText(updateCommentDto.getText());
        comment.setUpdated(LocalDateTime.now());
        comment.setState(CommentStatus.PENDING);
        return Mapper.toDto(commentRepository.save(comment));
    }

    @Override
    public void delete(long userId, long commentId) {
        checkUser(userId);
        Comment comment = checkComment(commentId);
        checkAuthor(userId, comment);
        commentRepository.deleteById(commentId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> getAllByEventId(long eventId, int from, int size) {
        checkEvent(eventId);
        Pageable pageRequest = PaginationAndSortParams.getPageable(from, size);
        return commentRepository.findAllByEventId(eventId, pageRequest).stream()
                .map(Mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CommentDto getPublic(long eventId, long commentId) {
        checkEvent(eventId);
        Comment comment = checkComment(commentId);
        return Mapper.toDto(comment);
    }

    private Event checkEvent(long eventId) {
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
        requestRepository.findByEventIdAndRequesterIdAndStatus(eventId, userId, RequestStatus.CONFIRMED).orElseThrow(() ->
                new NotFoundException(String.format("User with id=%d did not participate in Event with id=%d", userId, eventId)));
    }

    private void checkAuthor(long userId, Comment comment) {
        if (userId != comment.getAuthor().getId()) {
            throw new ForbiddenException(String.format("User id=%d is not author", userId));
        }
    }
}
