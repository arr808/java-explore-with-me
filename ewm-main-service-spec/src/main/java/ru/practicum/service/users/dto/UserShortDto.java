package ru.practicum.service.users.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class UserShortDto {
    @NotNull
    private long id;
    @NotBlank
    private String name;
}
