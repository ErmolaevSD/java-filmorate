package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Long, User> usersMap = new HashMap<>();

    @GetMapping
    public Collection<User> findAll() {
        log.info("Получен запрос на получение пользователей: {}", usersMap);
        log.info("Успешно обработан запрос на получение пользователей: {}", usersMap);
        return usersMap.values();
    }

    @PostMapping
    public User create(@RequestBody @Valid User user) {
        log.info("Получен запрос на создание пользователя: {}", user);
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        user.setId(getNextId());
        usersMap.put(user.getId(), user);
        log.info("Успешно обработан запрос на создание пользователя: {}", user);
        return user;
    }

    @PutMapping
    public User update(@RequestBody @Valid User newUser) {
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
