package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item getItemById(long itemId);

    Item addItem(Long userId, Item item);

    Item updateItem(Long userId, Long itemId, Item itemPatch);

    List<Item> getItemByUserId(Long userId);

    List<Item> searchItems(String text);
}
