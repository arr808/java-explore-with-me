package ru.practicum.service.categories.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.service.categories.model.Category;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findAllByIdIn(List<Long> ids);
}
