package ru.practicum.service.comments.service;

import ru.practicum.service.comments.dto.CommentDto;
import ru.practicum.service.comments.dto.NewCommentDto;
import ru.practicum.service.comments.dto.UpdateCommentDto;
import ru.practicum.service.comments.model.CommentStateAction;

import java.util.List;

public interface CommentService {

    List<CommentDto> getAll(long eventId, int from, int size);

    CommentDto changeCommentStatus(long eventId, long commentId, CommentStateAction state);

    void delete(long commentId);

    List<CommentDto> getAll(long eventId, long userId, int from, int size);

    CommentDto getPrivate(long userId, long commentId);

    CommentDto add(long userId, NewCommentDto newCommentDto);

    CommentDto update(long userId, UpdateCommentDto updateCommentDto);

    void delete(long userId, long commentId);

    List<CommentDto> getAllByEventId(long eventId, int from, int size);

    CommentDto getPublic(long eventId, long commentId);
}
