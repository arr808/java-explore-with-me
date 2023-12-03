package ru.practicum.service.requests.service;

import ru.practicum.service.requests.dto.EventRequestStatusUpdateRequest;
import ru.practicum.service.requests.dto.EventRequestStatusUpdateResult;
import ru.practicum.service.requests.dto.ParticipationRequestDto;

import java.util.List;

public interface RequestService {

    List<ParticipationRequestDto> getAllByUserIdAndEventId(long userId, long eventId);

    List<ParticipationRequestDto> getAllByUserId(long userId);

    ParticipationRequestDto add(long userId, long eventId);

    ParticipationRequestDto cancelRequest(long userId, long requestId);

    EventRequestStatusUpdateResult setRequestApprove(long userId, long eventId, EventRequestStatusUpdateRequest updateRequest);
}
