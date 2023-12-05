package ru.practicum.service.requests.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.service.requests.model.ParticipationRequest;
import ru.practicum.service.requests.model.RequestStatus;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<ParticipationRequest, Long> {

    Long countByRequesterIdAndEventId(long userId, long eventId);

    Long countByEventIdAndStatus(Long eventId, RequestStatus requestStatus);

    Optional<ParticipationRequest> findByIdAndRequesterId(long requestId, long userId);

    List<ParticipationRequest> findAllByRequesterId(long userId);

    List<ParticipationRequest> findAllByEventInitiatorIdAndEventId(long userId, long eventId);

    List<ParticipationRequest> findAllByIdInAndStatus(List<Long> requestIds, RequestStatus requestStatus);
}
