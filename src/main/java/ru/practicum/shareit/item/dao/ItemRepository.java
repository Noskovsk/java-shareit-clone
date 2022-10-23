package ru.practicum.shareit.item.dao;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {
    Optional<Item> getItemById(long itemId);

    Optional<Item> addItem(User user, Item item);

    Item updateItem(Item itemPatch, Item item);

    List<Item> getItemByUserId(Long userId);

    List<Item> searchItem(String text);
}
