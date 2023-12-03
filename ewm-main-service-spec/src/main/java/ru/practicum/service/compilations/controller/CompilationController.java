package ru.practicum.service.compilations.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.service.compilations.dto.CompilationDto;
import ru.practicum.service.compilations.dto.NewCompilationDto;
import ru.practicum.service.compilations.dto.UpdateCompilationRequest;
import ru.practicum.service.compilations.service.CompilationService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
public class CompilationController {

    private final CompilationService compilationService;

    @Autowired
    public CompilationController(CompilationService compilationService) {
        this.compilationService = compilationService;
    }

    @GetMapping("/compilations")
    public List<CompilationDto> getAll(@RequestParam(required = false) boolean pinned,
                                       @RequestParam(defaultValue = "0") int from,
                                       @RequestParam(defaultValue = "10") int size) {
        log.info("Получен запрос GET /compilations?pinned={}&from={}&size={}", pinned, from, size);
        return compilationService.getAll(pinned, from, size);
    }

    @GetMapping("/compilations/{compId}")
    public CompilationDto getById(@PathVariable long compId) {
        log.info("Получен запрос GET /compilations/{}", compId);
        return compilationService.getById(compId);
    }

    @PostMapping("/admin/compilations")
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto add(@Valid @RequestBody NewCompilationDto newCompilationDto) {
        log.info("Получен запрос POST /admin/compilations");
        return compilationService.add(newCompilationDto);
    }

    @PatchMapping("/admin/compilations/{compId}")
    public CompilationDto update(@PathVariable(name = "compId") Long compId,
                                 @Valid @RequestBody UpdateCompilationRequest updateCompilationRequest) {
        log.info("Получен запрос PATCH /admin/compilations/{}", compId);
        return compilationService.update(compId, updateCompilationRequest);
    }

    @DeleteMapping("/admin/compilations/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long compId) {
        log.info("Получен запрос DELETE /admin/compilations/{}", compId);
        compilationService.delete(compId);
    }
}
