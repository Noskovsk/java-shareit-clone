package ru.practicum.shareit.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.exception.IncorrectStatusException;
import ru.practicum.shareit.item.dto.ItemOwnerDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;

public class BookingServiceImplMockTest {
    ItemService mockItemService;
    BookingRepository mockBookingRepository;
    UserService mockUserService;
    BookingService bookingService;
    Throwable throwable;

    @BeforeEach
    void prepareData() {
        mockItemService = Mockito.mock(ItemServiceImpl.class);
        mockBookingRepository = Mockito.mock((BookingRepository.class));
        mockUserService = Mockito.mock(UserServiceImpl.class);
        bookingService = new BookingServiceImpl(mockBookingRepository, mockItemService, mockUserService);
    }

    @Test
    void shouldThrowExceptionWhenItemNotAvailable() {
        ItemOwnerDto itemOwnerDto = new ItemOwnerDto();
        itemOwnerDto.setAvailable(false);

        Mockito.when(mockItemService.getItemById(anyLong(), eq(Long.valueOf(1)))).thenReturn(itemOwnerDto);

        BookingDto bookingDto = BookingDto.builder().itemId(1L).build();
        throwable = assertThrows(ResponseStatusException.class, () -> bookingService.createBooking(1L, bookingDto));
        assertTrue(throwable.getMessage().contains("Ошибка при бронировании вещи. Вещь не доступна, id вещи"));
    }

    @Test
    void shouldThrowExceptionWhenBoockedByOwner() {
        ItemOwnerDto itemOwnerDto = new ItemOwnerDto();
        itemOwnerDto.setAvailable(true);
        User booker = new User();
        booker.setId(1L);
        itemOwnerDto.setOwner(booker);

        Mockito.when(mockItemService.getItemById(anyLong(), eq(1L))).thenReturn(itemOwnerDto);
        Mockito.when(mockUserService.getUserById(1L)).thenReturn(booker);

        BookingDto bookingDto = BookingDto
                .builder()
                .itemId(1L)
                .start(LocalDateTime.now().minusDays(1))
                .end(LocalDateTime.now())
                .bookerId(1L).build();
        throwable = assertThrows(ResponseStatusException.class, () -> bookingService.createBooking(1L, bookingDto));
        assertTrue(throwable.getMessage().contains("Ошибка при бронировании вещи. Вещь не доступна для бронирования владельцем, id вещи"));
    }

    @Test
    void shouldThrowExceptionWhenBookingStartAfterEnd() {
        ItemOwnerDto itemOwnerDto = new ItemOwnerDto();
        itemOwnerDto.setAvailable(true);

        Mockito.when(mockItemService.getItemById(anyLong(), eq(1L))).thenReturn(itemOwnerDto);

        BookingDto bookingDto = BookingDto
                .builder()
                .itemId(1L)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now())
                .bookerId(1L).build();
        throwable = assertThrows(ResponseStatusException.class, () -> bookingService.createBooking(1L, bookingDto));
        assertTrue(throwable.getMessage().contains("раньше даты начала"));
    }

    @Test
    void shouldThrowExceptionWhenUnknownStateInBookingsOfUser() {
        throwable = assertThrows(IncorrectStatusException.class,
                () -> bookingService.getBookingsOfUser(1L, "UNKNOWN", null, null));
        assertTrue(throwable.getMessage().contains("UNKNOWN"));
    }

    @Test
    void shouldThrowExceptionWhenUnknownStateInBookingsOfOwn() {
        throwable = assertThrows(IncorrectStatusException.class,
                () -> bookingService.getBookingsOfOwner(1L, "UNKNOWN_OWN", null, null));
        assertTrue(throwable.getMessage().contains("UNKNOWN_OWN"));
    }
}
