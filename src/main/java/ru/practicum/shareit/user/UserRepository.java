package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class UserRepository {
    private final Map<Long, User> userHashMap = new HashMap<>();
    private long counter = 0;

    public Optional<User> getUserById(long userId) {
        return Optional.ofNullable(userHashMap.get(userId));
    }

    public Optional<User> createUser(User user) {
        if (userHashMap.containsValue(user)) {
            return Optional.empty();
        }
        user.setId(++counter);
        userHashMap.put(user.getId(), user);
        return Optional.of(userHashMap.get(user.getId()));
    }
}
