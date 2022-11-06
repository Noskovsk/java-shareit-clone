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

    @Query("SELECT b " +
            "FROM bookings b " +
            "WHERE b.booker = :booker " +
            "AND b.start <= :today " +
            "AND b.end >= :today  " +
            "ORDER BY b.start DESC")
    List<Booking> getBookingsCurrent(User booker, LocalDateTime today);

    @Query("SELECT b " +
            "FROM bookings b " +
            "WHERE b.booker = :booker " +
            "AND b.status = 'APPROVED' " +
            "AND b.end <= :today " +
            "ORDER BY b.start DESC")
    List<Booking> getBookingsInPast(User booker, LocalDateTime today);

    @Query("SELECT b " +
            "FROM bookings b " +
            "WHERE b.booker = :booker " +
            "AND b.start >= :today " +
            "ORDER BY b.start DESC")
    List<Booking> getBookingsInFuture(User booker, LocalDateTime today);

    @Query(value = "SELECT * " +
            "FROM bookings b " +
            "INNER JOIN items i " +
            "ON i.id = b.item_id " +
            "WHERE i.owner_id = ?1 " +
            "ORDER BY start_booking DESC", nativeQuery = true)
    List<Booking> getAllBookingsByOwner(Long owner_id);

    @Query(value = "SELECT * " +
            "FROM bookings b " +
            "INNER JOIN items i " +
            "ON i.id = b.item_id " +
            "WHERE i.owner_id = ?1 " +
            "AND b.status = ?2 " +
            "ORDER BY start_booking DESC", nativeQuery = true)
    List<Booking> getAllBookingsByOwnerAndStatus(Long owner_id, String status);

    @Query(value = "SELECT * " +
            "FROM bookings b " +
            "INNER JOIN items i " +
            "ON i.id = b.item_id " +
            "WHERE i.owner_id = ?1 " +
            "AND b.start_booking <= ?2 " +
            "AND b.end_of_booking >= ?2 " +
            "ORDER BY start_booking DESC", nativeQuery = true)
    List<Booking> getAllBookingsByOwnerCurrent(Long owner_id, LocalDateTime today);

    @Query(value = "SELECT * " +
            "FROM bookings b " +
            "INNER JOIN items i " +
            "ON i.id = b.item_id " +
            "WHERE i.owner_id = ?1 " +
            "AND b.start_booking >= ?2 " +
            "ORDER BY start_booking DESC", nativeQuery = true)
    List<Booking> getAllBookingsByOwnerInFuture(Long owner_id, LocalDateTime today);

    @Query(value = "SELECT * " +
            "FROM bookings b " +
            "INNER JOIN items i " +
            "ON i.id = b.item_id " +
            "WHERE i.owner_id = ?1 " +
            "AND b.end_of_booking <= ?2 " +
            "ORDER BY start_booking DESC", nativeQuery = true)
    List<Booking> getAllBookingsByOwnerInPast(Long owner_id, LocalDateTime today);

    @Query(value = "SELECT * " +
            "FROM bookings b " +
            "WHERE b.item_id = ?1 " +
            "AND b.end_of_booking <= ?2 " +
            "ORDER BY end_of_booking DESC " +
            "LIMIT 1",
            nativeQuery = true)
    Optional<Booking> getLastBookingOfItem(Long itemId, LocalDateTime today);

    @Query("SELECT b FROM bookings b WHERE b.item = :item AND b.start >= :today ORDER BY b.start ASC")
    Optional<Booking> getNextBookingOfItem(Item item, LocalDateTime today);

    Optional<Booking> getBookingByBookerAndItemAndStatusEqualsAndEndBefore(User booker, Item item, BookingStatus status, LocalDateTime today);
}
