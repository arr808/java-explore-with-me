package ru.practicum.service.categories.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.service.categories.dto.CategoryDto;
import ru.practicum.service.categories.dto.NewCategoryDto;
import ru.practicum.service.categories.model.Category;
import ru.practicum.service.categories.repository.CategoryRepository;
import ru.practicum.service.events.repository.EventRepository;
import ru.practicum.service.util.Mapper;
import ru.practicum.service.util.PaginationAndSortParams;
import ru.practicum.service.util.exceptions.IncorrectlyRequestException;
import ru.practicum.service.util.exceptions.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, EventRepository eventRepository) {
        this.categoryRepository = categoryRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public List<CategoryDto> getAll(int from, int size) {
        Pageable pageRequest = PaginationAndSortParams.getPageable(from, size);
        return categoryRepository.findAll(pageRequest).stream()
                .map(Mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getById(long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() ->
                new NotFoundException(String.format("Category with id=%d was not found", id)));
        return Mapper.toDto(category);
    }

    @Override
    public CategoryDto add(NewCategoryDto newCategoryDto) {
        Category category = categoryRepository.save(Mapper.fromDto(newCategoryDto));
        return Mapper.toDto(category);
    }

    @Override
    public CategoryDto update(NewCategoryDto updateCategory, long id) {
        getById(id);
        Category category = categoryRepository.save(Mapper.fromDto(updateCategory, id));
        return Mapper.toDto(category);
    }

    @Override
    public void delete(long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() ->
                new NotFoundException(String.format("Category with id=%d was not found", id)));
        if (eventRepository.findAllByCategoryId(id).size() > 0) throw new IncorrectlyRequestException("Event exist");
        categoryRepository.delete(category);
    }
}
