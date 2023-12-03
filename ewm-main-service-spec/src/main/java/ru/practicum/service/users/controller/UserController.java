package ru.practicum.service.users.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.service.users.dto.NewUserDto;
import ru.practicum.service.users.dto.UserDto;
import ru.practicum.service.users.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/admin/users")
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> get(@RequestParam(required = false, defaultValue = "") List<Long> ids,
                             @RequestParam(required = false, defaultValue = "0") int from,
                             @RequestParam(required = false, defaultValue = "10") int size) {
        log.info("Получен запрос GET /admin/users?ids={}from={}&size={}", ids, from, size);
        return userService.getAll(ids, from, size);
    }

    @PostMapping("/admin/users")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto add(@Valid @RequestBody NewUserDto newUserDto) {
        log.info("Получен запрос POST /admin/users");
        return userService.add(newUserDto);
    }

    @DeleteMapping("/admin/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long userId) {
        log.info("Получен запрос DELETE /admin/users/{}", userId);
        userService.delete(userId);
    }
}
