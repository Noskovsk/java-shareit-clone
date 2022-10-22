package ru.practicum.shareit.item.dto;

import org.modelmapper.ModelMapper;
import ru.practicum.shareit.item.model.Item;

public class ItemMapper {
    private static ModelMapper modelMapper = new ModelMapper();

    public static ItemDto toItemDto(Item item) {
        return modelMapper.map(item, ItemDto.class);
    }

    public static Item toItem(ItemDto itemDto) {
        return modelMapper.map(itemDto, Item.class);
    }

    public static Item patchUser(Item patchItem, Item itemToBePatched) {
        modelMapper.getConfiguration().setSkipNullEnabled(true);
        modelMapper.map(patchItem, itemToBePatched);
        return itemToBePatched;
    }
}
