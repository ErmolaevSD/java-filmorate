package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Set;

public interface UserStorage {

    User create(User user);

    User update(User newUser);

    User removeUser(User user);

    Collection<User> findAll();

    User getUser(Long id);

    void addFriend(Long userId, Long friendId);

    void removeFriend(Long userId, Long friendId);

    Set<User> findAllFriends(Long id);

    Set<User> getCommonFriends(Long userId, Long friendId);

}