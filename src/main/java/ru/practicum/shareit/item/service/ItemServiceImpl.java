package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;

    @Autowired
    private final UserService userService;

    @Override
    public Item getItemById(long itemId) {
        log.info("Получен запрос на поиск вещи с id: {}", itemId);
        Optional<Item> itemOptional = itemRepository.findById(itemId);
        if (itemOptional.isEmpty()) {
            log.error("Ошибка при поиске вещи с itemId: {}", itemId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ошибка при поиске вещи!");
        } else {
            return itemOptional.get();
        }
    }

    @Override
    public Item addItem(Long userId, Item item) {
        log.info("Получен запрос на добавление вещи пользователя с id: {}. Вещь: {}", userId, item.getName());
        User owner = userService.getUserById(userId);
        item.setOwner(owner);
        return itemRepository.save(item);
    }

    @Override
    public Item updateItem(Long userId, Long itemId, Item itemPatch) {
        log.info("Получен запрос на обновление данных вещи пользователя с id: {}. Вещь: {}", userId, itemId);
        if (!getItemById(itemId).getOwner().getId().equals(userId)) {
            log.error("Ошибка при обновлении вещи: {}. Вещь не относится к текущему пользователю id = {}.", itemId, userId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ошибка при обновлении вещи: " + itemId
                    + ". Вещь не относится к текущему пользователю id = " + userId + ".");
        }
        return itemRepository.save(ItemMapper.patchItem(itemPatch, getItemById(itemId)));
    }

    @Override
    public List<Item> getItemByUserId(Long userId) {
        log.info("Получен запрос на получение списка вещей пользователя с id: {}.", userId);
        return itemRepository.getItemsByOwner(userService.getUserById(userId));
    }


    @Override
    public List<Item> searchItems(String text) {
        log.info("Получен запрос на поиск вещей по фразе: {}.", text);
        if (text.isBlank()) {
            log.info("Получен запрос на поиск вещей по пустой фразе!");
            return Collections.emptyList();
        }
        return itemRepository.searchItemsByString(text.toLowerCase());
    }
}
