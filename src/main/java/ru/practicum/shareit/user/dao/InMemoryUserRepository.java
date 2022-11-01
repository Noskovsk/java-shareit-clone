package ru.practicum.shareit.user.dao;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Repository
public class InMemoryUserRepository implements UserRepository {
    private final Map<Long, User> userHashMap = new HashMap<>();
    private long counter = 0;

    @Override
    public Optional<User> getUserById(Long userId) {
        return Optional.ofNullable(userHashMap.get(userId));
    }

    @Override
    public boolean repositoryContainsUserWithEmail(User user) {
        return userHashMap.containsValue(user);
    }

    @Override
    public Optional<User> createUser(User user) {
        if (userHashMap.containsValue(user)) {
            return Optional.empty();
        }
        user.setId(++counter);
        userHashMap.put(user.getId(), user);
        return Optional.of(userHashMap.get(user.getId()));
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(userHashMap.values());
    }

    @Override
    public boolean deleteUser(User user) {
        return userHashMap.remove(user.getId(), user);
    }

    @Override
    public User patchUser(User patchUser, User userById) {
        return UserMapper.patchUser(patchUser, userById);
    }
}
