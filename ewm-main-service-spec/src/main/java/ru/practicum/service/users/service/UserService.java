package ru.practicum.service.users.service;

import ru.practicum.service.users.dto.NewUserDto;
import ru.practicum.service.users.dto.UserDto;

import java.util.List;

public interface UserService {

    List<UserDto> getAll(List<Long> ids, int from, int size);

    UserDto add(NewUserDto newUserDto);

    void delete(long id);
}
