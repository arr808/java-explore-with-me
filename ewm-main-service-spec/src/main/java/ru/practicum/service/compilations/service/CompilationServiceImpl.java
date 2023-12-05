package ru.practicum.service.compilations.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.service.compilations.dto.CompilationDto;
import ru.practicum.service.compilations.dto.NewCompilationDto;
import ru.practicum.service.compilations.dto.UpdateCompilationRequest;
import ru.practicum.service.compilations.model.Compilation;
import ru.practicum.service.compilations.repository.CompilationRepository;
import ru.practicum.service.events.repository.EventRepository;
import ru.practicum.service.util.Mapper;
import ru.practicum.service.util.PaginationAndSortParams;
import ru.practicum.service.util.exceptions.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Autowired
    public CompilationServiceImpl(CompilationRepository compilationRepository, EventRepository eventRepository) {
        this.compilationRepository = compilationRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompilationDto> getAll(boolean pinned, int from, int size) {
        Pageable pageRequest = PaginationAndSortParams.getPageable(from, size);
        if (pinned) {
            return compilationRepository.findAllByPinned(pinned, pageRequest).stream()
                    .map(Mapper::toDto)
                    .collect(Collectors.toList());
        } else {
            return compilationRepository.findAll(pageRequest).stream()
                    .map(Mapper::toDto)
                    .collect(Collectors.toList());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public CompilationDto getById(long id) {
        return Mapper.toDto(compilationRepository.findById(id).orElseThrow(() ->
                new NotFoundException(String.format("Compilation with id=%d was not found", id))));
    }

    @Override
    public CompilationDto add(NewCompilationDto newCompilationDto) {
        Compilation compilation = Mapper.fromDto(newCompilationDto);

        List<Long> events = newCompilationDto.getEvents();
        if (events != null && events.size() > 0) {
            compilation.setEvents(eventRepository.findAllByIdIn(newCompilationDto.getEvents()));
        }
        return Mapper.toDto(compilationRepository.save(compilation));
    }

    @Override
    public CompilationDto update(long id, UpdateCompilationRequest updateCompilationRequest) {
        Compilation compilation = compilationRepository.findById(id).orElseThrow(() ->
                new NotFoundException(String.format("Compilation with id=%d was not found", id)));

        List<Long> eventsId = updateCompilationRequest.getEvents();
        if (eventsId != null) compilation.setEvents(eventRepository.findAllByIdIn(eventsId));

        if (updateCompilationRequest.getPinned() != null) compilation.setPinned(updateCompilationRequest.getPinned());

        if (updateCompilationRequest.getTitle() != null) compilation.setTitle(updateCompilationRequest.getTitle());

        return Mapper.toDto(compilationRepository.save(compilation));
    }

    @Override
    public void delete(long id) {
        compilationRepository.findById(id).orElseThrow(() ->
                new NotFoundException(String.format("Compilation with id=%d was not found", id)));
        compilationRepository.deleteById(id);
    }
}
