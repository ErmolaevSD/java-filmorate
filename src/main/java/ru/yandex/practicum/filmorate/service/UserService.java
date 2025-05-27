package ru.yandex.practicum.filmorate.service;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.Set;

@Slf4j
@Service
@Data

public class UserService {

    private final UserStorage userStorage;

    UserService(@Qualifier("newUserStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User findUserById(Long id) {
        return userStorage.getUser(id);
    }

    public Set<User> commonFriend(Long userID, Long friendID) {
        return userStorage.getCommonFriends(userID, friendID);
    }

    public void addFriend(Long userId, Long friendId) {
        userStorage.addFriend(userId, friendId);
    }

    public void deleteFriend(Long userId, Long friendId) {
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
        return userStorage.findAllFriends(id);
    }
}