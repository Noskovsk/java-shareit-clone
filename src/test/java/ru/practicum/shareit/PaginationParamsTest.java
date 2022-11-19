package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.pagination.PaginationParams;

import static org.junit.jupiter.api.Assertions.*;

public class PaginationParamsTest {
    @Test
    void shouldThrowExceptionWithNegativeParams() {
        Integer fromNegative = -1;
        Integer sizeNegative = -1;

        Throwable throwable = assertThrows(ResponseStatusException.class, () -> PaginationParams.createPageRequest(fromNegative, 50));
        assertNotNull(throwable.getMessage());

        throwable = assertThrows(ResponseStatusException.class, () -> PaginationParams.createPageRequest(0, sizeNegative));
        assertNotNull(throwable.getMessage());
    }

    @Test
    void shouldThrowExceptionWithZeroSize() {
        Integer sizeZero = 0;

        Throwable throwable = assertThrows(ResponseStatusException.class, () -> PaginationParams.createPageRequest(0, sizeZero));
        assertNotNull(throwable.getMessage());
    }

    @Test
    void shouldCreateCorrectPageRequestWithNullSize() {
        PageRequest pageRequest = PaginationParams.createPageRequest(5, null);
        assertEquals(0, pageRequest.getPageNumber(), "Первая страница задана неверно.");
        assertEquals(Integer.MAX_VALUE, pageRequest.getPageSize(), "Развер задан неверно.");
    }

    @Test
    void shouldCreateCorrectPageRequestWithNullFrom() {
        PageRequest pageRequest = PaginationParams.createPageRequest(null, 50);
        assertEquals(0, pageRequest.getPageNumber(), "Первая страница задана неверно.");
    }

    @Test
    void shouldCreateCorrectPageRequestWithoutOrder() {
        PageRequest pageRequest = PaginationParams.createPageRequest(5, 5);
        assertEquals(1, pageRequest.getPageNumber(), "Первая страница задана неверно.");
        assertEquals(5, pageRequest.getPageSize(), "Развер задан неверно.");
    }

    @Test
    void shouldCreateCorrectPageRequestWithOrder() {
        PageRequest pageRequest = PaginationParams.createPageRequest(0, 10, "id");
        assertEquals(Sort.by("id").descending(), pageRequest.getSort(), "Неверно задана сортировка");
    }
}
