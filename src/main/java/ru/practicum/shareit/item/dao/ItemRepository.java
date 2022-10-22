package ru.practicum.shareit.item.dao;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class ItemRepository {
    private final Map<Long, Item> itemHashMap = new HashMap<>();
    public Optional<Item> getItemById(long itemId) {
        return Optional.ofNullable(itemHashMap.get(itemId));
    }
}
