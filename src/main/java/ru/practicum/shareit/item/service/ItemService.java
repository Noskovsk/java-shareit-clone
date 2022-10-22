package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class ItemService {
    @Autowired
    private final ItemRepository itemRepository;

    @Autowired
    private final UserService userService;

    public Item getItemById(long itemId) {
        log.info("Получен запрос на поиск вещи с id: {}", itemId);
        Optional<Item> itemOptional = itemRepository.getItemById(itemId);
        if (itemOptional.isEmpty()) {
            log.error("Ошибка при поиске вещи с itemId: {}", itemId);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Ошибка при поиске вещи!");
        } else {
            return itemOptional.get();
        }
    }

    public Item addItem(Long userId, Item item) {
        log.info("Получен запрос на добавление вещи пользователя с id: {}. Вещь: {}", userId, item.getName());
        Optional<Item> itemOptional = itemRepository.addItem(userService.getUserById(userId), item);
        if (itemOptional.isEmpty()) {
            log.error("Ошибка при добавлении вещи: {}", item.getName());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Ошибка при добавлении вещи: " + item.getName());
        } else {
            return itemOptional.get();
        }
    }

    public Item updateItem(Long userId, Long itemId, Item itemPatch) {
        log.info("Получен запрос на обновление данных вещи пользователя с id: {}. Вещь: {}", userId, itemId);
        if (!getItemById(itemId).getOwner().getId().equals(userId)) {
            log.error("Ошибка при обновлении вещи: {}. Вещь не относится к текущему пользователю id = {}.", itemId, userId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ошибка при обновлении вещи: " + itemId
                    + ". Вещь не относится к текущему пользователю id = " + userId + ".");
        }
        return itemRepository.updateItem(itemPatch, getItemById(itemId));
    }

    public List<Item> getItemByUserId(Long userId) {
        log.info("Получен запрос на получение списка вещей пользователя с id: {}.", userId);
        return itemRepository.getItemByUserId(userId);
    }


}
