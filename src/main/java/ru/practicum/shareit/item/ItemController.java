package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserMapper;

@RestController
@RequestMapping("/items")
@AllArgsConstructor
public class ItemController {
    @Autowired
    private final ItemService itemService;

    @GetMapping("/{userId}")
    public ItemDto getItemById(@PathVariable long itemId) {
        return ItemMapper.toItemDto(itemService.getItemById(itemId));
    }

}
