package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.model.User;

public class ItemDto {
    private long id;
    private String name;
    private String description;
    private boolean available;
    private User owner;
    private ItemRequest itemRequest;
}
