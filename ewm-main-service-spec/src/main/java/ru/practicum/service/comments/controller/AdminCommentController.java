package ru.practicum.service.comments.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.service.comments.dto.CommentFullDto;
import ru.practicum.service.comments.model.CommentStateAction;
import ru.practicum.service.comments.service.CommentService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin/events/{eventId}/comments")
public class AdminCommentController {

    private final CommentService commentService;

    @Autowired
    public AdminCommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping
    public List<CommentFullDto> getAll(@PathVariable long eventId) {
        log.info("Получен запрос GET /admin/events/{}/comments", eventId);
        return commentService.getAll(eventId);
    }

    @PatchMapping("/{commentId}")
    public CommentFullDto changeCommentStatus(@PathVariable long eventId,
                                              @PathVariable long commentId,
                                              @RequestParam(name = "state") CommentStateAction state) {
        log.info("Получен запрос PATCH /admin/events/{}/comments/{}?state={}", eventId, commentId, state);
        return commentService.changeCommentStatus(eventId, commentId, state);
    }

    @DeleteMapping("/{commentId}")
    public void delete(@PathVariable long eventId,
                       @PathVariable long commentId) {
        log.info("Получен запрос DELETE /admin/events/{}/comments/{}", eventId, commentId);
        commentService.delete(commentId);
    }
}
