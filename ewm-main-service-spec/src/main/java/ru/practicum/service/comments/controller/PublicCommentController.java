package ru.practicum.service.comments.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.service.comments.dto.CommentDto;
import ru.practicum.service.comments.service.CommentService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/events/{eventId}/comments")
public class PublicCommentController {

    private final CommentService commentService;

    @Autowired
    public PublicCommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping
    public List<CommentDto> getAll(@PathVariable long eventId) {
        log.info("Получен запрос GET /events/{}/comments", eventId);
        return commentService.getAllByEventId(eventId);
    }
}
