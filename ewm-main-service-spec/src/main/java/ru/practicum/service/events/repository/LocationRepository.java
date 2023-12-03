package ru.practicum.service.events.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.service.events.model.Location;

public interface LocationRepository extends JpaRepository<Location, Long> {
}
