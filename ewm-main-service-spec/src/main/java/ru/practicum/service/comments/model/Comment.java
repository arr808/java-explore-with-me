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
    private long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", referencedColumnName = "id")
    private Event event;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private User author;
    @Column(length = 1200, nullable = false)
    private String text;
    @Column(nullable = false)
    private LocalDateTime created;
    @Column
    private LocalDateTime updated;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CommentStatus state;
}
