package ru.yandex.practicum.filmorate.storage;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Slf4j
@Component
@Data
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> userMap = new HashMap<>();

    public User getUser(Long id) {
        return userMap.get(id);
    }

    public void addFriend(Long userId, Long friendId) {
        log.info("Получен запрос добавление {} в друзья к {}", userMap.get(userId), userMap.get(friendId));
        User user = userMap.get(userId);
        User friend = userMap.get(friendId);
        if (isNull(userMap.get(userId)) || isNull(userMap.get(friendId))) {
            throw new NotFoundException("Пользователь не найден.");
        }
        if (user.getUserFriends() != null && user.getUserFriends().contains(friendId)) {
            log.info("Ошибка обработки запроса на добавление в друзья. Пользователь уже в друзьях");
            throw new ValidationException("Пользователь " + friend.getName() + " уже в друзьях у " + user.getName());
        }
        userMap.get(userId).getUserFriends().add(friendId);
        userMap.get(friendId).getUserFriends().add(userId);
        log.info("Успешно обработан запрос на добавление {} в друзья к {}", friend, user);
    }

    public void removeFriend(Long userId, Long friendId) {
        if (isNull(userMap.values())) {
            throw new NotFoundException("Список пользователей пуст");
        }
        if (isNull(userMap.get(userId)) || isNull(userMap.get(friendId))) {
            throw new NotFoundException("Пользователя не существует");
        }
        log.info("Получен запрос удаление {} из друзей у {}", userMap.get(userId).getName(), userMap.get(friendId).getName());
        User user = userMap.get(userId);
        User friend = userMap.get(friendId);
        if (isNull(user.getUserFriends())) {
            throw new NotFoundException("У пользователя нет друзей. Удаление невозможно.");
        }
        if (!user.getUserFriends().contains(friendId)) {
            log.info("Запрос не обработан. Пользователя {} нет в друзьях у {}", friend.getName(), user.getName());
        }
        log.info("Успешно обработан запрос на удаление {} из друзей у {}", friend, user);
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

    public Set<User> findAllFriends(Long id) {
        if (isNull(userMap.get(id))) {
            throw new NotFoundException("Пользователь с id " + id + "не найден");
        }
        Set<Long> userId = userMap.get(id).getUserFriends();

        return userId.stream()
                .map(userMap::get)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<User> getCommonFriends(Long userID, Long friendID) {
        Set<Long> userFriends = userMap.get(userID).getUserFriends();
        Set<Long> friendFriends = userMap.get(friendID).getUserFriends();
        return userFriends.stream()
                .filter(friendFriends::contains)
                .map(userMap::get)
                .collect(Collectors.toSet());
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