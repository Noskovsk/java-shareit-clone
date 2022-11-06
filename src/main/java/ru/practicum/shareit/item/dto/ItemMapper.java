package ru.practicum.shareit.item.dto;

import org.modelmapper.ModelMapper;
import ru.practicum.shareit.item.model.Item;

public class ItemMapper {
    private static final ModelMapper MODEL_MAPPER = new ModelMapper();

    public static Item toItem(ItemDto itemDto) {
        return MODEL_MAPPER.map(itemDto, Item.class);
    }

    public static Item toItem(ItemOwnerDto itemOwnerDto) {
        return MODEL_MAPPER.map(itemOwnerDto, Item.class);
    }

    public static ItemOwnerDto toItemOwnerDto(Item item) {
        return MODEL_MAPPER.map(item, ItemOwnerDto.class);
    }

    public static Item patchItem(Item patchItem, Item itemToBePatched) {
        MODEL_MAPPER.getConfiguration().setSkipNullEnabled(true);
        MODEL_MAPPER.map(patchItem, itemToBePatched);
        return itemToBePatched;
    }
}
