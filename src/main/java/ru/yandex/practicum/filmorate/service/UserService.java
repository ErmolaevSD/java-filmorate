package ru.yandex.practicum.filmorate.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.Objects.isNull;

@Slf4j
@Service
@Data
@RequiredArgsConstructor
public class UserService {

    private final InMemoryUserStorage userStorage;

    public Optional<User> findUserById(Long id) {
        return Optional.ofNullable(userStorage.getUserMap().get(id));
    }

    public List<Long> commonFriend(Long userID, Long friendID) {
        Set<Long> userFriends = userStorage.getUserMap().get(userID).getUserFriends();
        Set<Long> friendFriends = userStorage.getUserMap().get(friendID).getUserFriends();
        return userFriends.stream()
                .filter(friendFriends::contains)
                .toList();
    }

    public User addFriend(Long userId, Long friendId) {
        log.info("Получен запрос добавление {} в друзья к {}", userStorage.getUserMap().get(userId), userStorage.getUserMap().get(friendId));
        User user = userStorage.getUserMap().get(userId);
        User friend = userStorage.getUserMap().get(friendId);
        if (isNull(userStorage.getUserMap().get(userId)) || isNull(userStorage.getUserMap().get(friendId))) {
            throw new NotFoundException("Пользователь не найден.");
        }
        if (user.getUserFriends() != null && user.getUserFriends().contains(friendId)) {
            log.info("Ошибка обработки запроса на добавление в друзья. Пользователь уже в друзьях");
            throw new ValidationException("Пользователь " + friend.getName() + " уже в друзьях у " + user.getName());
        }
        if (user.getUserFriends() == null) {
            user.setUserFriends(new HashSet<>());
        }
        if (friend.getUserFriends() == null) {
            friend.setUserFriends(new HashSet<>());
        }
        user.getUserFriends().add(friendId);
        friend.getUserFriends().add(userId);
        log.info("Успешно обработан запрос на добавление {} в друзья к {}", friend, user);
        return friend;
    }

    public User deleteFriend(Long userId, Long friendId) {
        if (isNull(userStorage.getUserMap())) {
            throw new NotFoundException("Список пользователей пуст");
        }
        if (isNull(userStorage.getUserMap().get(userId)) || isNull(userStorage.getUserMap().get(friendId))) {
            throw new NotFoundException("Пользователя не существует");
        }
        log.info("Получен запрос удаление {} из друзей у {}", userStorage.getUserMap().get(userId).getName(), userStorage.getUserMap().get(friendId).getName());
        User user = userStorage.getUserMap().get(userId);
        User friend = userStorage.getUserMap().get(friendId);
        if (isNull(user.getUserFriends())) {
            throw new NotFoundException("У пользователя нет друзей. Удаление невозможно.");
        }
        if (!user.getUserFriends().contains(friendId)) {
            log.info("Запрос не обработан. Пользователя {} нет в друзьях у {}", friend.getName(), user.getName());
        }
        log.info("Успешно обработан запрос на удаление {} из друзей у {}", friend, user);
        user.getUserFriends().remove(friendId);
        friend.getUserFriends().remove(userId);
        return friend;
    }

    public Set<Long> findAllFriends(Long id) {
        if (isNull(userStorage.getUserMap().get(id))) {
            throw new NotFoundException("Пользователь с id " + id + "не найден");
        }
        return Set.copyOf(userStorage.getUserMap().get(id).getUserFriends());
    }
}