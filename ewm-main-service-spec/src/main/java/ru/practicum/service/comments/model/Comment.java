package ru.practicum.service.comments.model;

import lombok.*;
import ru.practicum.service.events.model.Event;
import ru.practicum.service.users.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", referencedColumnName = "id")
    Event event;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    User author;
    @Column(length = 1200, nullable = false)
    String text;
    @Column(nullable = false)
    LocalDateTime created;
    @Column
    LocalDateTime updated;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    CommentStatus status;
}
