package ru.practicum.service.comments.service;

import ru.practicum.service.comments.dto.CommentDto;
import ru.practicum.service.comments.dto.CommentFullDto;
import ru.practicum.service.comments.dto.NewCommentDto;
import ru.practicum.service.comments.dto.UpdateCommentDto;
import ru.practicum.service.comments.model.CommentStateAction;

import java.util.List;

public interface CommentService {

    List<CommentFullDto> getAll(long eventId);

    CommentFullDto changeCommentStatus(long eventId, long commentId, CommentStateAction state);

    void delete(long commentId);

    List<CommentFullDto> getAll(long eventId, long userId);

    CommentDto add(long eventId, long userId, NewCommentDto newCommentDto);

    CommentDto update(long eventId, long userId, UpdateCommentDto updateCommentDto);

    List<CommentDto> getAllByEventId(long eventId);
}
