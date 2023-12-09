package ru.practicum.service.comments.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.service.comments.dto.CommentDto;
import ru.practicum.service.comments.dto.NewCommentDto;
import ru.practicum.service.comments.dto.UpdateCommentDto;
import ru.practicum.service.comments.service.CommentService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users/{userId}/events/{eventId}/comments")
public class PrivateCommentController {

    private final CommentService commentService;

    @Autowired
    public PrivateCommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping
    public List<CommentDto> getAll(@PathVariable long userId,
                                @PathVariable long eventId,
                                @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                @Positive @RequestParam(defaultValue = "10") int size) {
        log.info("Получен запрос GET /users/{}/events/{}/comments?from={}&size={}", userId, eventId, from, size);
        return commentService.getAll(eventId, userId, from, size);
    }

    @GetMapping("/{commentId}")
    public CommentDto get(@PathVariable long userId,
                          @PathVariable long eventId,
                          @PathVariable long commentId) {
        log.info("Получен запрос GET /users/{}/events/{}/comments/{}", userId, eventId, commentId);
        return commentService.getPrivate(userId, commentId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto add(@PathVariable long userId,
                          @PathVariable long eventId,
                          @Valid @RequestBody NewCommentDto newCommentDto) {
        log.info("Получен запрос POST /users/{}/events/{}/comments", userId, eventId);
        newCommentDto.setEventId(eventId);
        return commentService.add(userId, newCommentDto);
    }

    @PatchMapping("/{commentId}")
    public CommentDto update(@PathVariable long userId,
                             @PathVariable long eventId,
                             @PathVariable long commentId,
                             @Valid @RequestBody UpdateCommentDto updateCommentDto) {
        log.info("Получен запрос PATCH /users/{}/events/{}/comments/{}", userId, eventId, commentId);
        updateCommentDto.setId(commentId);
        updateCommentDto.setEventId(eventId);
        return commentService.update(userId, updateCommentDto);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long userId,
                       @PathVariable long eventId,
                       @PathVariable long commentId) {
        log.info("Получен запрос DELETE /users/{}/events/{}/comments/{}", userId, eventId, commentId);
        commentService.delete(userId, commentId);
    }
}
