package ru.practicum.shareit.user.dao;

import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    public Optional<User> getUserById(Long userId);

    public boolean repositoryContainsUserWithEmail(User user);

    public Optional<User> createUser(User user);

    public List<User> getAll();

    public boolean deleteUser(User user);
}
