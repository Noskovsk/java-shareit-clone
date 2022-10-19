package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.user.model.User;

import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {
    @Autowired
    private final UserRepository userRepository;
    public User getUserById(long userId) {
        log.info("Получен запрос на поиск пользователя с id: {}", userId);
        Optional<User> userOptional = userRepository.getUserById(userId);
        if (userOptional.isEmpty()) {
            log.error("Ошибка при поиске пользователя с userId: {}", userId);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Ошибка при поиске пользователя!");
        } else {
            return userOptional.get();
        }
    }

    public User createUser(User user) {
        log.info("Получен запрос на создание пользователя: name = {}, email = {}",user.getName(), user.getEmail());
        Optional<User> userOptional = userRepository.createUser(user);
        if (userOptional.isEmpty()) {
            log.error("Ошибка при записи пользователя. Пользователь с таким email уже существует: {}", user.getEmail());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Ошибка. Пользователь с таким email уже существует: " + user.getEmail());
        } else {
            return userOptional.get();
        }
    }
}
