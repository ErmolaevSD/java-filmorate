package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;


@Component("newUserStorage")
public class UserDbStorage implements UserStorage {
    @Override
    public User create(User user) {
        return null;
    }

    @Override
    public User update(User newUser) {
        return null;
    }

    @Override
    public User removeUser(User user) {
        return null;
    }

    @Override
    public Collection<User> findAll() {
        return List.of();
    }
}
