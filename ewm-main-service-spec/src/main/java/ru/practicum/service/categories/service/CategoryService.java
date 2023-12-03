package ru.practicum.service.categories.service;

import ru.practicum.service.categories.dto.CategoryDto;
import ru.practicum.service.categories.dto.NewCategoryDto;

import java.util.List;

public interface CategoryService {

    List<CategoryDto> getAll(int from, int size);

    CategoryDto getById(long id);

    CategoryDto add(NewCategoryDto newCategoryDto);

    CategoryDto update(NewCategoryDto updateCategory, long id);

    void delete(long id);
}
