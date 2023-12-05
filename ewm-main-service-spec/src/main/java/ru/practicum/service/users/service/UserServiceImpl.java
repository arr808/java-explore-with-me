package ru.practicum.service.users.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.service.users.dto.NewUserDto;
import ru.practicum.service.users.dto.UserDto;
import ru.practicum.service.users.model.User;
import ru.practicum.service.users.repository.UserRepository;
import ru.practicum.service.util.Mapper;
import ru.practicum.service.util.PaginationAndSortParams;
import ru.practicum.service.util.exceptions.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getAll(List<Long> ids, int from, int size) {
        Pageable pageRequest = PaginationAndSortParams.getPageable(from, size);

        if (ids.size() == 0) {
            return userRepository.findAll(pageRequest).stream()
                    .map(Mapper::toDto)
                    .collect(Collectors.toList());
        }

        return userRepository.findAllByIdIn(ids, pageRequest).stream()
                .map(Mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto add(NewUserDto newUserDto) {
        User user = userRepository.save(Mapper.fromDto(newUserDto));
        return Mapper.toDto(user);
    }

    @Override
    public void delete(long id) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new NotFoundException(String.format("User with id=%d was not found", id)));
        userRepository.delete(user);
    }
}
