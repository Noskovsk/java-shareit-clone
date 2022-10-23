package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/items")
@AllArgsConstructor
public class ItemController {
    @Autowired
    private final ItemService itemService;

    @GetMapping("/{itemId}")
    public Item getItemById(@PathVariable long itemId) {
        return itemService.getItemById(itemId);
    }

    @GetMapping
    public List<Item> getItemByUserId(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.getItemByUserId(userId);
    }

    @PostMapping
    public Item addItem(@RequestHeader("X-Sharer-User-Id") Long userId, @Valid @RequestBody Item item) {
        return itemService.addItem(userId, item);
    }

    @PatchMapping("/{itemId}")
    public Item updateItem(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long itemId, @Valid @RequestBody ItemDto itemDto) {
        return itemService.updateItem(userId, itemId, ItemMapper.toItem(itemDto));
    }

    @GetMapping("/search")
    public List<Item> searchItems(@RequestParam String text) {
        return itemService.searchItems(text);
    }

}
