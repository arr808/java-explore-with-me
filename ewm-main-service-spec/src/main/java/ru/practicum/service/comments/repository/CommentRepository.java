package ru.practicum.service.comments.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.service.comments.model.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByEventId(long eventId);
}
