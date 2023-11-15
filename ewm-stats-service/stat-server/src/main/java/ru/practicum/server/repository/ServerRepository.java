package ru.practicum.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.dto.ShortStatDto;
import ru.practicum.server.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

public interface ServerRepository extends JpaRepository<EndpointHit, Long> {

    @Query(value = "select new ru.practicum.dto.ShortStatDto(h.app, h.uri, count(distinct h.ip)) " +
            "from EndpointHit as h " +
            "where " +
            "(h.timestamp between ?1 and ?2) and" +
            "(h.uri in ?3) " +
            "group by (h.app, h.uri)")
    List<ShortStatDto> findAllUniqueInUris(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query(value = "select new ru.practicum.dto.ShortStatDto(h.app, h.uri, count(h.ip)) " +
            "from EndpointHit as h " +
            "where " +
            "(h.timestamp between ?1 and ?2) and" +
            "(h.uri in ?3) " +
            "group by (h.app, h.uri)")
    List<ShortStatDto> findAllNotUniqueInUris(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query(value = "select new ru.practicum.dto.ShortStatDto(h.app, h.uri, count(distinct h.ip)) " +
            "from EndpointHit as h " +
            "where " +
            "(h.timestamp between ?1 and ?2)" +
            "group by (h.app, h.uri)")
    List<ShortStatDto> findAllUnique(LocalDateTime start, LocalDateTime end);

    @Query(value = "select new ru.practicum.dto.ShortStatDto(h.app, h.uri, count(h.ip)) " +
            "from EndpointHit as h " +
            "where " +
            "(h.timestamp between ?1 and ?2)" +
            "group by (h.app, h.uri)")
    List<ShortStatDto> findAllNotUnique(LocalDateTime start, LocalDateTime end);
}
