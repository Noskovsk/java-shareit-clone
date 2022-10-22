package ru.practicum.shareit.item.dto;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class ItemDto {
    @Size(min = 1)
    private String name;
    @Size(min = 1)
    private String description;
    private Boolean available;
}
