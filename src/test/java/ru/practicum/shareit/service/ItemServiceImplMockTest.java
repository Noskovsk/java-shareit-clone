package ru.practicum.shareit.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.item.comment.dao.CommentRepository;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ItemServiceImplMockTest {

    @Test
    void shouldThrowExceptionWhenAddCommentByIncorrectUser() {
        Comment comment = new Comment();
        comment.setText("some text");

        User user = new User();
        user.setEmail("email@email.com");

        Item item = new Item();
        item.setOwner(user);

        UserService userMockService = Mockito.mock(UserServiceImpl.class);
        ItemRepository itemMockRepository = Mockito.mock(ItemRepository.class);
        BookingRepository bookingMockRepository = Mockito.mock(BookingRepository.class);
        CommentRepository commentMockRepository = Mockito.mock(CommentRepository.class);
        ItemService itemService = new ItemServiceImpl(itemMockRepository, bookingMockRepository, userMockService, commentMockRepository);

        when(userMockService.getUserById(1L)).thenReturn(user);

        when(itemMockRepository.findById(any())).thenReturn(Optional.of(item));

        Throwable throwable = assertThrows(ResponseStatusException.class,
                () -> itemService.addComment(1L, 1L, comment));
        assertTrue(throwable.getMessage().contains("400"));
    }
}
