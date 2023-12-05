package ru.practicum.service.compilations.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.service.events.dto.EventShortDto;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompilationDto {
    private Long id;
    private List<EventShortDto> events;
    private boolean pinned;
    @NotBlank
    private String title;
}
