package ru.practicum.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.dto.ShortStatDto;
import ru.practicum.server.model.Stat;

import java.time.LocalDateTime;
import java.util.List;

public interface ServerRepository extends JpaRepository<Stat, Long> {

    @Query(value = "select new ru.practicum.dto.ShortStatDto(s.app, s.uri, count(distinct s.ip) as hits) " +
            "from Stat as s " +
            "where " +
            "(s.timestamp between :start and :end) and" +
            "(s.uri in :uris OR :uris = null) " +
            "group by (s.app, s.uri) " +
            "order by hits desc")
    List<ShortStatDto> findAllUniqueInUris(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query(value = "select new ru.practicum.dto.ShortStatDto(s.app, s.uri, count(s.ip) as hits) " +
            "from Stat as s " +
            "where " +
            "(s.timestamp between :start and :end) and" +
            "(s.uri in :uris OR :uris = null) " +
            "group by (s.app, s.uri) " +
            "order by hits desc")
    List<ShortStatDto> findAllNotUniqueInUris(LocalDateTime start, LocalDateTime end, List<String> uris);
}
