package ru.practicum.service.categories.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.service.categories.dto.CategoryDto;
import ru.practicum.service.categories.dto.NewCategoryDto;
import ru.practicum.service.categories.service.CategoryService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/admin/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto add(@Valid @RequestBody NewCategoryDto newCategoryDto) {
        log.info("Получен запрос POST /admin/categories/");
        return categoryService.add(newCategoryDto);
    }

    @PatchMapping("/admin/categories/{catId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto update(@Valid @RequestBody NewCategoryDto updateCategory,
                              @PathVariable long catId) {
        log.info("Получен запрос PATCH /admin/categories/{}", catId);
        return categoryService.update(updateCategory, catId);
    }

    @DeleteMapping("/admin/categories/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long catId) {
        log.info("Получен запрос DELETE /admin/categories/{}", catId);
        categoryService.delete(catId);
    }

    @GetMapping("/categories")
    @ResponseStatus(HttpStatus.OK)
    public List<CategoryDto> getAll(@RequestParam(required = false, defaultValue = "0") int from,
                                    @RequestParam(required = false, defaultValue = "10") int size) {
        log.info("Получен запрос GET /categories?from={}&size={}", from, size);
        return categoryService.getAll(from, size);
    }

    @GetMapping("/categories/{catId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto getById(@PathVariable long catId) {
        log.info("Получен запрос GET /categories/{}", catId);
        return categoryService.getById(catId);
    }
}
