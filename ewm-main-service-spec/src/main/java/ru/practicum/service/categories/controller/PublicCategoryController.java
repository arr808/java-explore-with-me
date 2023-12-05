package ru.practicum.service.categories.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.service.categories.dto.CategoryDto;
import ru.practicum.service.categories.service.CategoryService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/categories")
public class PublicCategoryController {

    private final CategoryService categoryService;

    @Autowired
    public PublicCategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CategoryDto> getAll(@PositiveOrZero @RequestParam(required = false, defaultValue = "0") int from,
                                    @Positive @RequestParam(required = false, defaultValue = "10") int size) {
        log.info("Получен запрос GET /categories?from={}&size={}", from, size);
        return categoryService.getAll(from, size);
    }

    @GetMapping("/{catId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto getById(@PathVariable long catId) {
        log.info("Получен запрос GET /categories/{}", catId);
        return categoryService.getById(catId);
    }
}
