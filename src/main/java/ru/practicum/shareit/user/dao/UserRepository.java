package ru.practicum.shareit.user.dao;

import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    Optional<User> getUserById(Long userId);

    boolean repositoryContainsUserWithEmail(User user);

    Optional<User> createUser(User user);

    List<User> getAll();

    boolean deleteUser(User user);

    User patchUser(User patchUser, User userById);
}
