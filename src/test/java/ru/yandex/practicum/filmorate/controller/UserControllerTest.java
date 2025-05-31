package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest extends AbstractControllerTest {

    @Test
    void createValidUser() {
        User user = new User(null, "gangan1999@bk.ru", "weekend", "Sergey", LocalDate.of(1999, 2, 12));

        assertTrue(validator.validate(user).isEmpty());
    }

    @Test
    void unvalitedEmail() {
        User unvalitedEmail = new User(null, "gangan1999k.ru", "weekend", "Sergey", LocalDate.of(1999, 2, 12));
        User unvalitedEmail1 = new User(null, "gangan1999k.ru@", "weekend", "Sergey", LocalDate.of(1999, 2, 12));

        assertFalse(validator.validate(unvalitedEmail).isEmpty());
        assertFalse(validator.validate(unvalitedEmail1).isEmpty());
    }

    @Test
    void unvalitedLogin() {
        User unvalitedLogin = new User(null, "gangan1999@bk.ru", "", "Sergey", LocalDate.of(1999, 2, 12));
        User unvalitedLogin1 = new User(null, "gangan1999@bk.ru", "Week end", "Sergey", LocalDate.of(1999, 2, 12));
        User unvalitedLogin2 = new User(null, "gangan1999@bk.ru", null, "Sergey", LocalDate.of(1999, 2, 12));

        assertFalse(validator.validate(unvalitedLogin).isEmpty());
        assertFalse(validator.validate(unvalitedLogin1).isEmpty());
        assertFalse(validator.validate(unvalitedLogin2).isEmpty());
    }

    @Test
    void unvalitedBirthday() {
        User unvalitedLogin = new User(null, "gangan1999@bk.ru", "weekend", "Sergey", LocalDate.of(2026, 2, 12));

        assertFalse(validator.validate(unvalitedLogin).isEmpty());
    }

    @Test
    void findAllTest() {
        User user = new User(null, "gangan1999@bk.ru", "weekend", "Sergey", LocalDate.of(1999, 2, 12));
        User user2 = new User(null, "gangan1999@bk.ru", "weekend", "Sergey", LocalDate.of(1999, 2, 12));

        userStorage.create(user2);
        userStorage.create(user);

        assertEquals(2, userStorage.findAll().size());
    }

    @Test
    void unvalitedUpdateUserTest() {
        User user = new User(null, "gangan1999@bk.ru", "weekend", "Sergey", LocalDate.of(1999, 2, 12));
        userStorage.create(user);
        User updateUser = new User(2L, "gangan1999@bk.ru", "weekend", "Sergey", LocalDate.of(1999, 2, 12));

        assertThrows(NotFoundException.class, () -> userStorage.update(updateUser));
    }

    @Test
    void updateTest() {
        User user = new User(null, "gangan1999@bk.ru", "weekend", "Sergey", LocalDate.of(1999, 2, 12));
        User createUser = userStorage.create(user);
        User updateUser = new User(1L, "gangan1999@bk.ru", "Local", "Sergey", LocalDate.of(1999, 2, 12));
        userStorage.update(updateUser);
        String newLogin = "Local";

        assertEquals(newLogin, createUser.getLogin());
    }
}