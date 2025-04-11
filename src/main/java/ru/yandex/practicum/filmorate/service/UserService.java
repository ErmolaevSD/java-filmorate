package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class UserService {

    private final Map<Long, User> usersMap = new HashMap<>();

    public Collection<User> findAll() {
        log.info("Получен запрос на получение пользователей");
        return Collections.unmodifiableCollection(usersMap.values());
    }

    public User create(User user) {
        log.info("Получен запрос на создание пользователя: {}", user);
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        user.setId(getNextId());
        usersMap.put(user.getId(), user);
        log.info("Успешно обработан запрос на создание пользователя: {}", user);
        return user;
    }

    public User update(User newUser) {
        log.info("Получен запрос на обновление пользователя: {}", newUser);
        if (!usersMap.containsKey(newUser.getId())) {
            throw new ValidationException("Указанного пользователя не найдено. Обновление невозможно");
        }
        User oldUser = usersMap.get(newUser.getId());
        oldUser.setBirthday(newUser.getBirthday());
        oldUser.setLogin(newUser.getLogin());
        oldUser.setEmail(newUser.getEmail());
        oldUser.setName(newUser.getName());
        log.info("Успешно обработан запрос на обновление пользователя: {}", newUser);
        return oldUser;
    }

    private long getNextId() {
        long currentMaxId = usersMap.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
