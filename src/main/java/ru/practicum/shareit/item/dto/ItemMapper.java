package ru.practicum.shareit.item.dto;

import org.modelmapper.ModelMapper;
import ru.practicum.shareit.item.model.Item;

public class ItemMapper {
    private static final ModelMapper modelMapper = new ModelMapper();

    public static Item toItem(ItemDto itemDto) {
        return modelMapper.map(itemDto, Item.class);
    }

    public static Item patchItem(Item patchItem, Item itemToBePatched) {
        modelMapper.getConfiguration().setSkipNullEnabled(true);
        modelMapper.map(patchItem, itemToBePatched);
        return itemToBePatched;
    }
}
