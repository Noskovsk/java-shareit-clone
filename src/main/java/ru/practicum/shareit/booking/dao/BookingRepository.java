package ru.practicum.shareit.booking.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> getBookingsByBookerOrderByStartDesc(User booker);

    List<Booking> getBookingsByBookerAndStatusOrderByStartDesc(User booker, BookingStatus status);

    Optional<Booking> getBookingByItemAndEndBeforeOrderByEndDesc(Item item, LocalDateTime today);

    Optional<Booking> getBookingByItemAndStartAfterOrderByStartAsc(Item item, LocalDateTime today);

    Optional<Booking> getBookingByBookerAndItemAndStatusEqualsAndEndBefore(User booker, Item item, BookingStatus status, LocalDateTime today);

    List<Booking> getBookingsByBookerAndEndBeforeOrderByStartDesc(User booker, LocalDateTime today);

    List<Booking> getBookingsByBookerAndStartAfterOrderByStartDesc(User booker, LocalDateTime today);

    @Query("SELECT b " +
            "FROM bookings b " +
            "WHERE b.booker = :booker " +
            "AND :today BETWEEN  b.start AND b.end " +
            "ORDER BY b.start DESC")
    List<Booking> getBookingsCurrent(User booker, LocalDateTime today);

    @Query(value = "SELECT * " +
            "FROM bookings b " +
            "INNER JOIN items i " +
            "ON i.id = b.item_id " +
            "WHERE i.owner_id = ?1 " +
            "ORDER BY b.start_booking DESC", nativeQuery = true)
    List<Booking> getAllBookingsByOwner(Long ownerId);

    @Query(value = "SELECT * " +
            "FROM bookings b " +
            "INNER JOIN items i " +
            "ON i.id = b.item_id " +
            "WHERE i.owner_id = ?1 " +
            "AND b.status = ?2 " +
            "ORDER BY b.start_booking DESC", nativeQuery = true)
    List<Booking> getAllBookingsByOwnerAndStatus(Long ownerId, String status);

    @Query(value = "SELECT * " +
            "FROM bookings b " +
            "INNER JOIN items i " +
            "ON i.id = b.item_id " +
            "WHERE i.owner_id = ?1 " +
            "AND ?2 BETWEEN b.start_booking AND b.end_of_booking " +
            "ORDER BY b.start_booking DESC", nativeQuery = true)
    List<Booking> getAllBookingsByOwnerCurrent(Long ownerId, LocalDateTime today);

    @Query(value = "SELECT * " +
            "FROM bookings b " +
            "INNER JOIN items i " +
            "ON i.id = b.item_id " +
            "WHERE i.owner_id = ?1 " +
            "AND b.start_booking >= ?2 " +
            "ORDER BY b.start_booking DESC", nativeQuery = true)
    List<Booking> getAllBookingsByOwnerInFuture(Long ownerId, LocalDateTime today);

    @Query(value = "SELECT * " +
            "FROM bookings b " +
            "INNER JOIN items i " +
            "ON i.id = b.item_id " +
            "WHERE i.owner_id = ?1 " +
            "AND b.end_of_booking <= ?2 " +
            "ORDER BY b.start_booking DESC", nativeQuery = true)
    List<Booking> getAllBookingsByOwnerInPast(Long ownerId, LocalDateTime today);
}
