package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dao.ItemRepository;

@Service
@AllArgsConstructor
public class ItemService {
    @Autowired
    private final ItemRepository itemRepository;


}
