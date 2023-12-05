package ru.practicum.service.requests.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.service.requests.dto.EventRequestStatusUpdateRequest;
import ru.practicum.service.requests.dto.EventRequestStatusUpdateResult;
import ru.practicum.service.requests.dto.ParticipationRequestDto;
import ru.practicum.service.requests.service.RequestService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users/{userId}")
public class PrivateRequestController {

    private final RequestService requestService;

    @Autowired
    public PrivateRequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @GetMapping("/events/{eventId}/requests")
    public List<ParticipationRequestDto> getAllByUserIdAndEventId(@PathVariable long userId,
                                                         @PathVariable long eventId) {
        log.info("Получен запрос GET /users/{}/events/{}/requests", userId, eventId);
        return requestService.getAllByUserIdAndEventId(userId, eventId);
    }

    @GetMapping("/requests")
    public List<ParticipationRequestDto> getAllByUserId(@PathVariable long userId) {
        log.info("Получен запрос GET /users/{}/requests", userId);
        return requestService.getAllByUserId(userId);
    }

    @PostMapping("/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto add(@PathVariable long userId,
                                       @RequestParam long eventId) {
        log.info("Получен запрос POST /users/{}/requests?eventId={}", userId, eventId);
        return requestService.add(userId, eventId);
    }

    @PatchMapping("/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelRequest(@PathVariable long userId,
                                                 @PathVariable long requestId) {
        log.info("Получен запрос PATCH /users/{}/requests/{}/cancel", userId, requestId);
        return requestService.cancelRequest(userId, requestId);
    }

    @PatchMapping("/events/{eventId}/requests")
    public EventRequestStatusUpdateResult setRequestApprove(@PathVariable long userId,
                                                            @PathVariable long eventId,
                                                            @RequestBody EventRequestStatusUpdateRequest updateRequest) {
        log.info("Получен запрос PATCH /users/{}/events/{}/requests", userId, eventId);
        return requestService.changeRequestStatus(userId, eventId, updateRequest);
    }
}
