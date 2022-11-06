package ru.practicum.shareit.booking.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.exception.IncorrectStatusException;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Service
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemService itemService;
    private final UserService userService;

    @Override
    public Booking getBookingById(long userId, long bookingId) {
        log.info("Получен запрос на поиск бронирования с id: {}", bookingId);
        User requestUser = userService.getUserById(userId);
        Optional<Booking> bookingOptional = bookingRepository.findById(bookingId);
        if (bookingOptional.isEmpty()
                || (!requestUser.equals(bookingOptional.get().getBooker())
                && !requestUser.equals(bookingOptional.get().getItem().getOwner()))) {
            log.error("Ошибка при поиске бронирования с id: {}", bookingId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ошибка при поиске бронирования!");
        } else {
            return bookingOptional.get();
        }
    }

    @Override
    @Transactional
    public Booking createBooking(long userId, Booking booking) {
        log.info("Получен запрос на бронирование вещи {}", booking.toString());
        Item item = ItemMapper.toItem(itemService.getItemById(userId, booking.getItem().getId()));
        if (!item.getAvailable()) {
            log.error("Ошибка при бронировании вещи. Вещь не доступна, id вещи: " + item.getId());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ошибка при бронировании вещи. " +
                    "Вещь не доступна, id вещи: " + item.getId());
        }
        if (booking.getStart().isAfter(booking.getEnd())) {
            log.error("Ошибка при бронировании вещи. Дата завершения бронирования: " + booking.getEnd() +
                    ", раньше даты начала: " + booking.getStart());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ошибка при бронировании вещи. " +
                    "Дата завершения бронирования: " + booking.getEnd() + ", раньше даты начала: " + booking.getStart());
        }
        User booker = userService.getUserById(booking.getBooker().getId());
        if (booker.equals(item.getOwner())) {
            log.error("Ошибка при бронировании вещи. Вещь не доступна для бронирования владельцем, id вещи: " + item.getId());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ошибка при бронировании вещи. " +
                    "Вещь не доступна для бронирования владельцем, id вещи: " + item.getId());
        }
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStatus(BookingStatus.WAITING);
        log.info("Сохраняем бронирование вещи {}", booking.toString());
        return bookingRepository.save(booking);
    }

    @Override
    public Booking updateBooking(Long userId, Long bookingId, Boolean approved) {
        log.info("Получен запрос на изменение статуса бронирования вещи {}", bookingId);
        Booking booking = getBookingById(userId, bookingId);
        User acceptor = userService.getUserById(userId);
        User owner = booking.getItem().getOwner();
        if (!acceptor.equals(owner)) {
            log.error("Ошибка при акцепте бронирования. Акцепт возможен только владельцем. owner = {}, acceptor = {}",
                    owner.getEmail(), acceptor.getEmail());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ошибка при акцепте бронирования. " +
                    "Акцепт возможен только владельцем. owner = " + owner.getEmail() + ", acceptor = " + acceptor.getEmail());
        }
        if (!booking.getStatus().equals(BookingStatus.WAITING)) {
            log.error("Ошибка при акцепте бронирования. Акцепт возможен только из статуса WAITING. status = {}",
                    booking.getStatus());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ошибка при акцепте бронирования. " +
                    "Акцепт возможен только из статуса WAITING. status = " + booking.getStatus());
        }
        booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        return bookingRepository.save(booking);
    }

    @Override
    public List<Booking> getBookingsOfUser(Long userId, String state) {
        log.info("Получен запрос поиск бронирований. userId = {}, state = {}", userId, state);
        if (state == null || state.isEmpty() || state.equals("ALL")) {
            return bookingRepository.getBookingsByBookerOrderByStartDesc(userService.getUserById(userId));
        } else {
            switch (state) {
                case "CURRENT":
                    return bookingRepository.getBookingsCurrent(userService.getUserById(userId), LocalDateTime.now());
                case "PAST":
                    return bookingRepository.getBookingsInPast(userService.getUserById(userId), LocalDateTime.now());
                case "FUTURE":
                    return bookingRepository.getBookingsInFuture(userService.getUserById(userId), LocalDateTime.now());
                case "WAITING":
                    return bookingRepository.getBookingsByBookerAndStatusOrderByStartDesc(userService.getUserById(userId), BookingStatus.WAITING);
                case "REJECTED":
                    return bookingRepository.getBookingsByBookerAndStatusOrderByStartDesc(userService.getUserById(userId), BookingStatus.REJECTED);
                default:
                    log.error("Ошибка при поиске бронирований. Неизвестный статус: status = {}", state);
                    throw new IncorrectStatusException(state);
            }
        }
    }

    @Override
    public List<Booking> getBookingsOfOwner(Long userId, String state) {
        log.info("Получен запрос поиск бронирований владельца вещей. userId = {}, state = {}", userId, state);
        userService.getUserById(userId);
        if (state == null || state.isEmpty() || state.equals("ALL")) {
            return bookingRepository.getAllBookingsByOwner(userId);
        } else {
            switch (state) {
                case "CURRENT":
                    return bookingRepository.getAllBookingsByOwnerCurrent(userId, LocalDateTime.now());
                case "PAST":
                    return bookingRepository.getAllBookingsByOwnerInPast(userId, LocalDateTime.now());
                case "FUTURE":
                    return bookingRepository.getAllBookingsByOwnerInFuture(userId, LocalDateTime.now());
                case "WAITING":
                case "REJECTED":
                    return bookingRepository.getAllBookingsByOwnerAndStatus(userId, state);
                default:
                    log.error("Ошибка при поиске бронирований. Неизвестный статус: status = {}", state);
                    throw new IncorrectStatusException(state);
            }
        }
    }
}
