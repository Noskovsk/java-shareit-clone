package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class ItemServiceImplTest {
    private final ItemService itemService;
    private final EntityManager entityManager;

    @BeforeEach
    void createTestUserIntoDb() {
        List<User> userList = List.of(new User(), new User());
        int counter = 1;
        for (User user : userList) {
            user.setName("user" + counter);
            user.setEmail("email" + counter + "@email.com");
            counter++;
            entityManager.persist(user);
        }
        entityManager.flush();
    }

    @Test
    void shouldReturnOneItemOfUser() {
        List<ItemCreateDto> itemCreateDtos = List.of(new ItemCreateDto(), new ItemCreateDto());
        int counter = 1;
        for (ItemCreateDto itemCreate : itemCreateDtos) {
            itemCreate.setName("Item" + counter);
            itemCreate.setDescription("ItemDesc" + counter);
            itemCreate.setAvailable(true);
            itemService.addItem((long) counter, itemCreate);
            counter++;
        }
        assertEquals(1, itemService.getItemByUserId(2L, null, null).size(), "Количество вещей не совпадает");
        assertEquals(Long.valueOf(2), itemService.getItemByUserId(2L, null, null).get(0).getId(), "Id вещи не совпадает с ожидаемым");

    }
}
