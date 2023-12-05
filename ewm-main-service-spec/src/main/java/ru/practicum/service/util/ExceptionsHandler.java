package ru.practicum.service.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.service.categories.controller.AdminCategoryController;
import ru.practicum.service.categories.controller.PublicCategoryController;
import ru.practicum.service.compilations.controller.AdminCompilationController;
import ru.practicum.service.compilations.controller.PublicCompilationController;
import ru.practicum.service.events.controller.AdminEventController;
import ru.practicum.service.events.controller.PrivateEventController;
import ru.practicum.service.events.controller.PublicEventController;
import ru.practicum.service.requests.controller.PrivateRequestController;
import ru.practicum.service.users.controller.AdminUserController;
import ru.practicum.service.util.exceptions.ForbiddenException;
import ru.practicum.service.util.exceptions.IncorrectlyRequestException;
import ru.practicum.service.util.exceptions.NotFoundException;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice(assignableTypes = {AdminUserController.class,
        PrivateRequestController.class,
        AdminEventController.class,
        PrivateEventController.class,
        PublicEventController.class,
        AdminCompilationController.class,
        PublicCompilationController.class,
        AdminCategoryController.class,
        PublicCategoryController.class})
public class ExceptionsHandler {

    private static final String INCORRECTLY = "Incorrectly made request.";
    private static final String NOT_FOUND = "The required object was not found.";
    private static final String FORBIDDEN = "For the requested operation the conditions are not met.";

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleIncorrectlyRequest(final IncorrectlyRequestException e) {
        ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), INCORRECTLY,
                e.getMessage(), e.getTimestamp());
        log.debug("Некорректный запрос {}", response);
        return response;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(final NotFoundException e) {
        ErrorResponse response = new ErrorResponse(HttpStatus.NOT_FOUND.toString(), NOT_FOUND,
                e.getMessage(), e.getTimestamp());
        log.debug("Объект не найден {}", response);
        return response;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequest(final ForbiddenException e) {
        ErrorResponse response = new ErrorResponse(HttpStatus.FORBIDDEN.toString(), FORBIDDEN,
                e.getMessage(), e.getTimestamp());
        log.debug("Объект  не удовлетворяет правилам {}", response);
        return response;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleBadRequestException(RuntimeException e) {
        ErrorResponse response = new ErrorResponse(HttpStatus.CONFLICT.toString(), INCORRECTLY,
                e.getMessage(), LocalDateTime.now());
        log.debug("Объект  не удовлетворяет правилам {}", response);
        return response;
    }
}