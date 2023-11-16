package ru.practicum.server.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.ShortStatDto;
import ru.practicum.dto.StatDto;
import ru.practicum.server.model.Stat;
import ru.practicum.server.repository.ServerRepository;
import ru.practicum.server.util.HitMapper;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@Transactional
public class ServerServiceImpl implements ServerService {

    private final ServerRepository repository;

    @Autowired
    public ServerServiceImpl(ServerRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShortStatDto> get(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        if (unique) return repository.findAllUniqueInUris(start, end, uris);
        return repository.findAllNotUniqueInUris(start, end, uris);
    }

    @Override
    public StatDto add(StatDto statDto) {
        Stat hit = HitMapper.fromDto(statDto);
        return HitMapper.toDto(repository.save(hit));
    }
}
