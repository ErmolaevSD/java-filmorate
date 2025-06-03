package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.mapper.UserMarRower;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({UserDbStorage.class, UserMarRower.class})
class UserDbControllerTest {

    private final UserDbStorage userDbStorage;
    @Autowired
    private final JdbcTemplate jdbcTemplate;

    private User newUser;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("DELETE from users");
        jdbcTemplate.update("ALTER TABLE users ALTER COLUMN id RESTART WITH 1");
        newUser = new User();
        newUser.setName("Test_Name");
        newUser.setLogin("Test_Login");
        newUser.setEmail("test@example.com");
        newUser.setBirthday(LocalDate.of(1999, 2, 16));
    }

    @Test
    void create() {
        User createdDbUser = userDbStorage.create(newUser);
        User getCreatedUser = userDbStorage.getUser(createdDbUser.getId());

        assertNotNull(createdDbUser.getId(), "ID пользователя не может быть null");
        assertEquals(1, createdDbUser.getId(), "ID первого пользователя должен быть 1");
        assertNotNull(getCreatedUser);
    }

    @Test
    void update() {
        userDbStorage.create(newUser);
        User updateUser = new User();
        updateUser.setId(1L);
        updateUser.setName("Update");
        updateUser.setLogin("Update");
        updateUser.setEmail("test@example.com");
        updateUser.setBirthday(LocalDate.of(1999, 2, 16));
        userDbStorage.update(updateUser);

        assertEquals("Update", userDbStorage.getUser(1L).getName());
        assertEquals("Update", userDbStorage.getUser(1L).getLogin());
        assertEquals(1, userDbStorage.findAll().size());
    }

    @Test
    void findAll() {
        userDbStorage.create(newUser);
        User updateUser = new User();
        updateUser.setName("Update");
        updateUser.setLogin("Update");
        updateUser.setEmail("test@example.com");
        updateUser.setBirthday(LocalDate.of(1999, 2, 16));
        userDbStorage.create(updateUser);
        Collection<User> all = userDbStorage.findAll();

        assertEquals(2, all.size());
    }

    @Test
    void findUser() {
        userDbStorage.create(newUser);
        User user = userDbStorage.getUser(newUser.getId());

        assertEquals(newUser, user);
    }
}