package ru.practicum.server.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.server.controller.ServerController;

@RestControllerAdvice(assignableTypes = {ServerController.class})
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handleBadRequest(final BadRequestException e) {
    }
}
