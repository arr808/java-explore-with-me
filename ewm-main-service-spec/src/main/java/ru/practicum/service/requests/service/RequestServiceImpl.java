package ru.practicum.service.requests.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.service.events.model.Event;
import ru.practicum.service.events.model.EventStatus;
import ru.practicum.service.events.repository.EventRepository;
import ru.practicum.service.requests.dto.EventRequestStatusUpdateRequest;
import ru.practicum.service.requests.dto.EventRequestStatusUpdateResult;
import ru.practicum.service.requests.dto.ParticipationRequestDto;
import ru.practicum.service.requests.model.ParticipationRequest;
import ru.practicum.service.requests.model.RequestStatus;
import ru.practicum.service.requests.repository.RequestRepository;
import ru.practicum.service.users.model.User;
import ru.practicum.service.users.repository.UserRepository;
import ru.practicum.service.util.Mapper;
import ru.practicum.service.util.exceptions.IncorrectlyRequestException;
import ru.practicum.service.util.exceptions.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Autowired
    public RequestServiceImpl(RequestRepository requestRepository, EventRepository eventRepository, UserRepository userRepository) {
        this.requestRepository = requestRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> getAllByUserIdAndEventId(long userId, long eventId) {
        return requestRepository.findAllByEventInitiatorIdAndEventId(userId, eventId).stream()
                .map(Mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> getAllByUserId(long userId) {
        return requestRepository.findAllByRequesterId(userId).stream()
                .map(Mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ParticipationRequestDto add(long userId, long eventId) {
        if (requestRepository.findAllByRequesterIdAndEventId(userId, eventId).size() > 0) {
            throw new IncorrectlyRequestException(String.format("User with id=%d already participates", userId));
        }
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException(String.format("Event with id=%d was not found", eventId)));

        if (userId == event.getInitiator().getId()) {
            throw new IncorrectlyRequestException(String.format("User with id=%d initiator", userId));
        }

        if (!event.getState().equals(EventStatus.PUBLISHED)) {
            throw new IncorrectlyRequestException("Event not published.");
        }
        if (event.getParticipantLimit() != 0 &&
                event.getParticipantLimit() <= requestRepository.countByEventIdAndStatus(eventId, RequestStatus.CONFIRMED)) {
            throw new IncorrectlyRequestException(String.format("Event %d has maximum confirmed requests", eventId));
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("There are no available seats"));

        RequestStatus status;
        if (event.getParticipantLimit() == 0) {
            status = RequestStatus.CONFIRMED;
        } else if (event.isRequestModeration()) {
            status = RequestStatus.PENDING;
        } else status = RequestStatus.CONFIRMED;

        ParticipationRequest request = ParticipationRequest.builder()
                .created(LocalDateTime.now())
                .event(event)
                .requester(user)
                .status(status)
                .build();
        return Mapper.toDto(requestRepository.save(request));
    }

    @Override
    public ParticipationRequestDto cancelRequest(long userId, long requestId) {
        ParticipationRequest request = requestRepository.findByIdAndRequesterId(requestId, userId)
                .orElseThrow(() -> new NotFoundException(String.format("Request with id=%d was not found", requestId)));
        request.setStatus(RequestStatus.CANCELED);
        return Mapper.toDto(requestRepository.save(request));
    }

    @Override
    public EventRequestStatusUpdateResult setRequestApprove(long userId, long eventId,
                                                            EventRequestStatusUpdateRequest updateRequest) {
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException(String.format("Event with id=%d was not found", eventId)));

        if (userId != event.getInitiator().getId()) {
            throw new IncorrectlyRequestException(String.format("User with id=%d is not initiator", userId));
        }

        if (!event.isRequestModeration() || event.getParticipantLimit() == 0) {
            throw new IncorrectlyRequestException("There are no available seats");
        }

        long limit = event.getParticipantLimit() - event.getConfirmedRequests();
        if (limit == 0 && updateRequest.getStatus().equals(RequestStatus.CONFIRMED)) {
            throw new IncorrectlyRequestException("There are no available seats");
        }

        List<ParticipationRequest> requests = requestRepository.findAllByIdInAndStatus(
                updateRequest.getRequestIds(), RequestStatus.PENDING);

        if (requests.size() == 0) throw new IncorrectlyRequestException("There are no request to approve");

        for (ParticipationRequest request : requests) {
            request.setStatus(updateRequest.getStatus());

            if (updateRequest.getStatus().equals(RequestStatus.CONFIRMED)) {
                event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                eventRepository.save(event);
            }
            requestRepository.save(request);
        }

        EventRequestStatusUpdateResult result = new EventRequestStatusUpdateResult();
        if (updateRequest.getStatus().equals(RequestStatus.CONFIRMED)) {
            result.setConfirmedRequests(requests.stream().map(Mapper::toDto).collect(Collectors.toList()));
        } else {
            result.setRejectedRequests(requests.stream().map(Mapper::toDto).collect(Collectors.toList()));
        }
        return result;
    }
}
