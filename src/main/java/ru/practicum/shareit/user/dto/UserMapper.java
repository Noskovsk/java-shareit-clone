package ru.practicum.shareit.user.dto;

import org.modelmapper.ModelMapper;
import ru.practicum.shareit.user.model.User;


public class UserMapper {
    private static ModelMapper modelMapper = new ModelMapper();

    public static UserDto toUserDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }

    public static User toUser(UserDto userDto) {
        return modelMapper.map(userDto, User.class);
    }

    public static User patchUser(User patchUser, User userToBePatched) {
        modelMapper.getConfiguration().setSkipNullEnabled(true);
        modelMapper.map(patchUser, userToBePatched);
        return userToBePatched;
    }
}
