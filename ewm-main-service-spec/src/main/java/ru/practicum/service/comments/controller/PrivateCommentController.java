package ru.practicum.service.comments.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.service.comments.dto.CommentDto;
import ru.practicum.service.comments.dto.CommentFullDto;
import ru.practicum.service.comments.dto.NewCommentDto;
import ru.practicum.service.comments.dto.UpdateCommentDto;
import ru.practicum.service.comments.service.CommentService;

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
    List<CommentFullDto> getAll(@PathVariable long userId,
                                @PathVariable long eventId) {
        log.info("Получен запрос GET /users/{}/events/{}/comments", userId, eventId);
        return commentService.getAll(eventId, userId);
    }

    @PostMapping
    public CommentDto add(@PathVariable long userId,
                          @PathVariable long eventId,
                          @RequestBody NewCommentDto newCommentDto) {
        log.info("Получен запрос POST /users/{}/events/{}/comments", userId, eventId);
        return commentService.add(eventId, userId, newCommentDto);
    }

    @PatchMapping("/{commentId}")
    public CommentDto update(@PathVariable long userId,
                             @PathVariable long eventId,
                             @PathVariable long commentId,
                             @RequestBody UpdateCommentDto updateCommentDto) {
        log.info("Получен запрос PATCH /users/{}/events/{}/comments/{}", userId, eventId, commentId);
        updateCommentDto.setId(commentId);
        return commentService.update(eventId, userId, updateCommentDto);
    }
}
