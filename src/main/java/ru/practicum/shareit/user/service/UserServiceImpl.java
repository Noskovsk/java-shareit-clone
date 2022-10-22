package ru.practicum.shareit.user.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    @Autowired
    private final UserRepository userRepository;

    @Override
    public User getUserById(long userId) {
        log.info("Получен запрос на поиск пользователя с id: {}", userId);
        Optional<User> userOptional = userRepository.getUserById(userId);
        if (userOptional.isEmpty()) {
            log.error("Ошибка при поиске пользователя с userId: {}", userId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ошибка при поиске пользователя!");
        } else {
            return userOptional.get();
        }
    }

    @Override
    public User createUser(User user) {
        log.info("Получен запрос на создание пользователя: name = {}, email = {}", user.getName(), user.getEmail());
        Optional<User> userOptional = userRepository.createUser(user);
        if (userOptional.isEmpty()) {
            log.error("Ошибка при записи пользователя. Пользователь с таким email уже существует: {}", user.getEmail());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Ошибка. Пользователь с таким email уже существует: " + user.getEmail());
        } else {
            return userOptional.get();
        }
    }

    @Override
    public User updateUser(long userId, User patchUser) {
        log.info("Получен запрос на изменение данных пользователя с id {}", userId);
        if (patchUser.getEmail() != null && userRepository.repositoryContainsUserWithEmail(patchUser)) {
            log.error("Ошибка при изменении данных пользователя. Пользователь с таким email уже существует: {}", patchUser.getEmail());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Ошибка. Пользователь с таким email уже существует: " + patchUser.getEmail());
        }
        if (patchUser.getName()!=null && patchUser.getName().isBlank()) {
            log.error("Ошибка при изменении данных пользователя. Имя  не может быть пустым.");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Ошибка при изменении данных пользователя. Имя  не может быть пустым.");
        }
        return userRepository.patchUser(patchUser, getUserById(userId));
    }

    @Override
    public List<User> listUsers() {
        log.info("Получен запрос на получение списка пользователей");
        return userRepository.getAll();
    }

    @Override
    public void deleteUser(Long userId) {
        log.info("Получен запрос на удаление пользователя с id {}", userId);
        if (!userRepository.deleteUser(getUserById(userId))) {
            log.error("Ошибка при удалении пользователя с id = {}.", userId);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Ошибка при удалении пользователя с id = " + userId + ".");
        }
    }
}
