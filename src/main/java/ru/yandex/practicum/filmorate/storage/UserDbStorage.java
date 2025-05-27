package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.UserMarRower;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Collection;

@RequiredArgsConstructor
@Component("newUserStorage")
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;
    private final UserMarRower userMarRower;


    @Override
    public User create(User user) {
        String query = "INSERT INTO users (email, login, name, birthdate) VALUES (?,?,?,?);";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, user.getEmail());
            ps.setObject(2, user.getLogin());
            ps.setObject(3, user.getName());
            ps.setObject(4, user.getBirthday());
            return ps;
        }, keyHolder);

        Long id = keyHolder.getKeyAs(Long.class);
        if (id != null) {
            user.setId(id);
        } else {
            throw new NotFoundException("Не удалось сохранить данные");
        }
        return user;
    }

    @Override
    public User update(User newUser) {
        Long id = newUser.getId();
        String newName = newUser.getName();
        String newLogin = newUser.getLogin();
        String newEmail = newUser.getEmail();
        LocalDate newBirthdate = newUser.getBirthday();
        String sql = "UPDATE users SET email = '" + newEmail + "', login = '" + newLogin + "', name = '" + newName + "', birthdate = '" + newBirthdate + "' WHERE id = " + id + ";";
        jdbcTemplate.execute(sql);
        return newUser;
    }

    @Override
    public User removeUser(User user) {
        String query = "DELETE from users where id = " + user.getId() + ";";
        jdbcTemplate.execute(query);
        return user;
    }

    @Override
    public Collection<User> findAll() {
        String FIND_ALL_USERS = "SELECT * from users";
        return jdbcTemplate.query(FIND_ALL_USERS, userMarRower);
    }

    @Override
    public User getUser(Long id) {
        String query = "SELECT * FROM users WHERE id = ?;";
        User user = jdbcTemplate.queryForObject(query, userMarRower, id);
        return user;
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        String friendsStatus = "Дружба";
        String query = "INSERT INTO friends (user_id, friend_id, status) VALUES (?, ?, ?)";
        jdbcTemplate.update(query, userId, friendId, friendsStatus);
    }

    @Override
    public void removeFriend(Long userId, Long friendId) {
        String query = "DELETE from friends where user_id = " + userId + "and friend_id = " + friendId + ";";
        jdbcTemplate.execute(query);
    }
}
