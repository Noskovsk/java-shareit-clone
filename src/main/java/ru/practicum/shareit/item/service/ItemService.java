package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;

import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class ItemService {
    @Autowired
    private final ItemRepository itemRepository;

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
}
