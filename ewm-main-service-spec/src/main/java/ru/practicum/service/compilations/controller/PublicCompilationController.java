package ru.practicum.service.compilations.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.service.compilations.dto.CompilationDto;
import ru.practicum.service.compilations.service.CompilationService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/compilations")
public class PublicCompilationController {

    private final CompilationService compilationService;

    @Autowired
    public PublicCompilationController(CompilationService compilationService) {
        this.compilationService = compilationService;
    }

    @GetMapping
    public List<CompilationDto> getAll(@RequestParam(required = false) boolean pinned,
                                       @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                       @Positive @RequestParam(defaultValue = "10") int size) {
        log.info("Получен запрос GET /compilations?pinned={}&from={}&size={}", pinned, from, size);
        return compilationService.getAll(pinned, from, size);
    }

    @GetMapping("/{compId}")
    public CompilationDto getById(@PathVariable long compId) {
        log.info("Получен запрос GET /compilations/{}", compId);
        return compilationService.getById(compId);
    }
}
