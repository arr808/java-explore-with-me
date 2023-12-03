package ru.practicum.service.compilations.service;

import ru.practicum.service.compilations.dto.CompilationDto;
import ru.practicum.service.compilations.dto.NewCompilationDto;
import ru.practicum.service.compilations.dto.UpdateCompilationRequest;

import java.util.List;

public interface CompilationService {

    List<CompilationDto> getAll(boolean pinned, int from, int size);

    CompilationDto getById(long id);

    CompilationDto add(NewCompilationDto newCompilationDto);

    CompilationDto update(long id, UpdateCompilationRequest updateCompilationRequest);

    void delete(long id);
}
