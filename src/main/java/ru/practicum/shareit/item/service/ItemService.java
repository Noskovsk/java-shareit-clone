package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.dto.ItemOwnerDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    ItemOwnerDto getItemById(long userId, long itemId);

    Item addItem(Long userId, Item item);

    Item updateItem(Long userId, Long itemId, Item itemPatch);

    List<ItemOwnerDto> getItemByUserId(Long userId);

    List<Item> searchItems(String text);

    Comment addComment(Long userId, Long itemId, Comment comment);
}
