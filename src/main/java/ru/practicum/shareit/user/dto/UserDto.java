package ru.practicum.shareit.user.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Data
public class UserDto {
    @Size(min = 1)
    private String name;
    @Size(min = 2)
    @Email
    private String email;

}
