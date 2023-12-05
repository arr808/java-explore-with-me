package ru.practicum.service.categories.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.service.categories.dto.CategoryDto;
import ru.practicum.service.categories.dto.NewCategoryDto;
import ru.practicum.service.categories.service.CategoryService;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/admin/categories")
public class AdminCategoryController {

    private final CategoryService categoryService;

    @Autowired
    public AdminCategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto add(@Valid @RequestBody NewCategoryDto newCategoryDto) {
        log.info("Получен запрос POST /admin/categories/");
        return categoryService.add(newCategoryDto);
    }

    @PatchMapping("/{catId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto update(@Valid @RequestBody CategoryDto updateCategory,
                              @PathVariable long catId) {
        log.info("Получен запрос PATCH /admin/categories/{}", catId);
        updateCategory.setId(catId);
        return categoryService.update(updateCategory);
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long catId) {
        log.info("Получен запрос DELETE /admin/categories/{}", catId);
        categoryService.delete(catId);
    }
}
