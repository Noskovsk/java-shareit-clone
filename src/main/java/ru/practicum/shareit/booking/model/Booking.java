package ru.practicum.shareit.booking.model;

import lombok.*;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDateTime;


@Entity(name = "bookings")
@ToString
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Setter
@Getter
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @FutureOrPresent
    @Column(name = "start_booking", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private LocalDateTime start;
    @FutureOrPresent
    @Column(name = "end_of_booking", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private LocalDateTime end;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booker_id")
    private User booker;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private BookingStatus status;
}
