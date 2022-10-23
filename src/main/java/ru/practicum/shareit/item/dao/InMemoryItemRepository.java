package ru.practicum.shareit.item.dao;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class InMemoryItemRepository implements ItemRepository {
    private final Map<Long, Item> itemHashMap = new HashMap<>();
    private long counter = 0;
    @Override
    public Optional<Item> getItemById(long itemId) {
        return Optional.ofNullable(itemHashMap.get(itemId));
    }

    @Override
    public Optional<Item> addItem(User user, Item item) {
        if (itemHashMap.containsValue(item)) {
            return Optional.empty();
        }
        item.setId(++counter);
        item.setOwner(user);
        itemHashMap.put(item.getId(), item);
        return Optional.of(itemHashMap.get(item.getId()));
    }

    @Override
    public Item updateItem(Item itemPatch, Item item) {
        return ItemMapper.patchItem(itemPatch, item);
    }

    @Override
    public List<Item> getItemByUserId(Long userId) {
        return itemHashMap.values().stream().filter(i -> i.getOwner().getId().equals(userId)).collect(Collectors.toList());
    }

    @Override
    public List<Item> searchItem(String text) {
        return itemHashMap.values()
                .stream()
                .filter(Item::getAvailable)
                .filter(i -> (i.getName().toLowerCase().contains(text) || i.getDescription().toLowerCase().contains(text)))
                .collect(Collectors.toList());
    }
}
