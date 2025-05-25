package ru.yandex.practicum.filmorate.storage;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Slf4j
@Component("oldUserStorage")
@Data
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> userMap = new HashMap<>();

    public User getUser(Long id) {
        return
                userMap.get(id);
    }

    public void addFriend(Long userId, Long friendId) {
        userMap.get(userId).getUserFriends().add(friendId);
        userMap.get(friendId).getUserFriends().add(userId);
    }

    public void removeFriend(Long userId, Long friendId) {
        userMap.get(userId).getUserFriends().remove(friendId);
        userMap.get(friendId).getUserFriends().remove(userId);
    }

    @Override
    public User create(User user) {
        log.info("Получен запрос на создание пользователя: {}", user);
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        user.setId(getNextId());
        userMap.put(user.getId(), user);
        log.info("Успешно обработан запрос на создание пользователя: {}", user);
        return user;
    }

    @Override
    public User update(User newUser) {
        log.info("Получен запрос на обновление пользователя: {}", newUser);
        if (!userMap.containsKey(newUser.getId())) {
            throw new NotFoundException("Указанного пользователя не найдено. Обновление невозможно");
        }
        User oldUser = userMap.get(newUser.getId());
        oldUser.setBirthday(newUser.getBirthday());
        oldUser.setLogin(newUser.getLogin());
        oldUser.setEmail(newUser.getEmail());
        oldUser.setName(newUser.getName());
        log.info("Успешно обработан запрос на обновление пользователя: {}", newUser);
        return oldUser;
    }

    @Override
    public User removeUser(User user) {
        log.info("Получен запрос на удаление пользователя.");
        if (!userMap.containsKey(user.getId())) {
            log.info("Запрос на удаление пользователя не обработан. Причина: {} не найден.", user);
            throw new NotFoundException("Указанного пользователя не найдено. Удаление невозможно");
        }
        log.info("Запрос на пользователя фильма {} успешно обработан.", user);
        return userMap.remove(user.getId());
    }

    @Override
    public Collection<User> findAll() {
        log.info("Получен запрос на получение пользователей");
        return Collections.unmodifiableCollection(userMap.values());
    }

    private long getNextId() {
        long currentMaxId = userMap.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}