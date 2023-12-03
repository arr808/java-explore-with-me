package ru.practicum.service.util;

import lombok.experimental.UtilityClass;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@UtilityClass
public class PaginationAndSortParams {
    public static Pageable getPageable(int from, int size) {
        return PageRequest.of(from / size, size);
    }

    public static Pageable getPageableAsc(int from, int size, String sortBy) {
        if (sortBy == null) sortBy = "id";
        else if (sortBy.equals("EVENT_DATE")) sortBy = "eventDate";
        else if (sortBy.equals("VIEWS")) sortBy = "views";
        return PageRequest.of(from / size, size, Sort.by(sortBy));
    }
}
