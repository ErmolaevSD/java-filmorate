package ru.yandex.practicum.filmorate.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Slf4j
@Service
@Data
@RequiredArgsConstructor
public class UserService {

    private final InMemoryUserStorage userStorage;

    public User findUserById(Long id) {
        return userStorage.getUser(id);
    }

    public Set<User> commonFriend(Long userID, Long friendID) {
        Set<Long> userFriends = userStorage.getUser(userID).getUserFriends();
        Set<Long> friendFriends = userStorage.getUser(friendID).getUserFriends();
        return userFriends.stream()
                .filter(friendFriends::contains)
                .map(userStorage.getUserMap()::get)
                .collect(Collectors.toSet());
    }

    public User addFriend(Long userId, Long friendId) {
        log.info("Получен запрос добавление {} в друзья к {}", userStorage.getUser(userId), userStorage.getUser(friendId));
        User user = userStorage.getUser(userId);
        User friend = userStorage.getUser(friendId);
        if (isNull(userStorage.getUser(userId)) || isNull(userStorage.getUser(friendId))) {
            throw new NotFoundException("Пользователь не найден.");
        }
        if (user.getUserFriends() != null && user.getUserFriends().contains(friendId)) {
            log.info("Ошибка обработки запроса на добавление в друзья. Пользователь уже в друзьях");
            throw new ValidationException("Пользователь " + friend.getName() + " уже в друзьях у " + user.getName());
        }
        userStorage.addFriend(userId, friendId);
        log.info("Успешно обработан запрос на добавление {} в друзья к {}", friend, user);
        return user;
    }

    public void deleteFriend(Long userId, Long friendId) {
        if (isNull(userStorage.getUserMap())) {
            throw new NotFoundException("Список пользователей пуст");
        }
        if (isNull(userStorage.getUser(userId)) || isNull(userStorage.getUser(friendId))) {
            throw new NotFoundException("Пользователя не существует");
        }
        log.info("Получен запрос удаление {} из друзей у {}", userStorage.getUser(userId).getName(), userStorage.getUser(friendId).getName());
        User user = userStorage.getUser(userId);
        User friend = userStorage.getUser(friendId);
        if (isNull(user.getUserFriends())) {
            throw new NotFoundException("У пользователя нет друзей. Удаление невозможно.");
        }
        if (!user.getUserFriends().contains(friendId)) {
            log.info("Запрос не обработан. Пользователя {} нет в друзьях у {}", friend.getName(), user.getName());
        }
        log.info("Успешно обработан запрос на удаление {} из друзей у {}", friend, user);
        userStorage.removeFriend(userId, friendId);
    }

    public User createUser(User user) {
        return userStorage.create(user);
    }

    public Collection<User> findAllUser() {
        return userStorage.findAll();
    }

    public User updateUser(User user) {
        return userStorage.update(user);
    }

    public Set<User> findAllFriends(Long id) {
        if (isNull(userStorage.getUser(id))) {
            throw new NotFoundException("Пользователь с id " + id + "не найден");
        }
        Set<Long> userId = userStorage.getUser(id).getUserFriends();

        return userId.stream()
                .map(userStorage.getUserMap()::get)
                .collect(Collectors.toSet());
    }
}