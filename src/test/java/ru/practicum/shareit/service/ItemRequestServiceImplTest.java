package ru.practicum.shareit.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class ItemRequestServiceImplTest {
    private final ItemRequestService itemRequestService;
    private final EntityManager manager;
    User user;

    @BeforeEach
    void createTestData() {
        user = new User();
        user.setName("user");
        user.setEmail("email@email.com");
        manager.persist(user);
        manager.flush();
    }

    @Test
    void shouldGetRequestById() {
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setDescription("description");
        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestDto);
        itemRequest.setRequestor(user);
        itemRequest.setCreated(LocalDateTime.now());
        manager.persist(itemRequest);
        manager.flush();
        itemRequestService.getRequestById(1L, 1L);

        assertEquals(1L,
                itemRequestService.getRequestById(1L, 1L).getId(),
                "Id запроса не совпадает с ожидаемым");
        assertEquals("description",
                itemRequestService.getRequestById(1L, 1L).getDescription(),
                "Описание завпроса вещи не совпадает с ожидаемым");
    }
}
