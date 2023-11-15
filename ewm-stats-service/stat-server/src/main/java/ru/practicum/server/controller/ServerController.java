package ru.practicum.server.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.ShortStatDto;
import ru.practicum.dto.StatDto;
import ru.practicum.server.service.ServerService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@RestController
@RequestMapping
public class ServerController {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final ServerService service;

    @Autowired
    public ServerController(ServerService service) {
        this.service = service;
    }

    @GetMapping("/stats")
    public List<ShortStatDto> get(@RequestParam(name = "start") String startStr,
                                  @RequestParam(name = "end") String endStr,
                                  @RequestParam(required = false) List<String> uris,
                                  @RequestParam(defaultValue = "false") Boolean unique) {
        log.info("Получен запрос GET /stats?start={}&end={}&uris={}&unique={}", startStr, endStr, uris, unique);
        LocalDateTime start = LocalDateTime.parse(startStr, FORMATTER);
        LocalDateTime end = LocalDateTime.parse(endStr, FORMATTER);
        return service.get(start, end, uris, unique);
    }

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public StatDto add(@Valid @RequestBody StatDto statDto) {
        log.info("Получен запрос POST /hit");
        return service.add(statDto);
    }
}
