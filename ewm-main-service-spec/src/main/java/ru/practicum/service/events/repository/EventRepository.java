package ru.practicum.service.events.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.service.events.model.Event;
import ru.practicum.service.events.model.EventStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Query(value = "SELECT e " +
            "FROM Event AS e " +
            "WHERE " +
            "(e.initiator.id IN :users OR :users is null) AND " +
            "(e.state IN :states OR :states is null) AND " +
            "(e.category.id IN :categories OR :categories is null) AND " +
            "(e.eventDate BETWEEN :rangeStart AND :rangeEnd)")
    List<Event> getAll(List<Long> users, List<EventStatus> states, List<Long> categories,
                              LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);

    List<Event> findAllByInitiatorId(long userId, Pageable pageable);

    @Query(value = "SELECT e " +
            "FROM Event AS e " +
            "WHERE " +
            "(e.state = 'PUBLISHED') AND " +
            "(:text is null or (LOWER(e.annotation) LIKE LOWER(CONCAT('%', :text, '%')) OR " +
            "LOWER(e.description) LIKE LOWER(CONCAT('%', :text, '%')))) AND " +
            "(e.category.id IN :categories OR :categories is null) AND " +
            "(e.paid = :paid OR :paid is null) AND " +
            "(e.eventDate BETWEEN :rangeStart AND :rangeEnd) AND " +
            "(:onlyAvailable = false OR (e.confirmedRequests < e.participantLimit))")
    List<Event> getAll(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                               LocalDateTime rangeEnd, boolean onlyAvailable, Pageable pageable);

    Optional<Event> findByIdAndInitiatorId(long eventId, long initiatorId);

    Optional<Event> findByIdAndState(long eventId, EventStatus state);

    Long countByCategoryId(long categoryId);

    List<Event> findAllByIdIn(List<Long> eventsId);

    Optional<Event> findByInitiatorId(long initiatorId);
}
