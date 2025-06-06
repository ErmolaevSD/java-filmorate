package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.UserMarRower;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        String findUser = "SELECT * from users where id = " + newUser.getId() + ";";
        List<User> userList = jdbcTemplate.query(findUser, userMarRower);
        if (userList.isEmpty()) {
            throw new NotFoundException("Обновление невозможно, указанного пользователя нет");
        }
        Long id = newUser.getId();
        String newName = newUser.getName();
        String newLogin = newUser.getLogin();
        String newEmail = newUser.getEmail();
        LocalDate newBirthdate = newUser.getBirthday();
        String sql = """
                UPDATE users SET email = '%s',
                login = '%s',
                name = '%s',
                birthdate = '%s'
                WHERE id = %d;
                """.formatted(newEmail, newLogin, newName, newBirthdate, id);
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
        String findAllUsers = "SELECT * from users";
        return jdbcTemplate.query(findAllUsers, userMarRower);
    }

    @Override
    public User getUser(Long id) {
        String query = "SELECT * FROM users WHERE id = ?;";
        return jdbcTemplate.queryForObject(query, userMarRower, id);
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        String findUser = "SELECT * from users where id = " + userId + ";";
        String findFriend = "SELECT * from users where id = " + friendId + ";";
        List<User> userList = jdbcTemplate.query(findUser, userMarRower);
        List<User> friendList = jdbcTemplate.query(findFriend, userMarRower);
        if (userList.isEmpty() || friendList.isEmpty()) {
            throw new NotFoundException("Обновление невозможно, указанного пользователя нет");
        }
        String query1 = "INSERT INTO friends (user_id, friend_id) VALUES (?, ?)";
        jdbcTemplate.update(query1, userId, friendId);
    }

    @Override
    public void removeFriend(Long userId, Long friendId) {
        String findUser = "SELECT * from users where id = " + userId + ";";
        String findFriend = "SELECT * from users where id = " + friendId + ";";
        List<User> userList = jdbcTemplate.query(findUser, userMarRower);
        List<User> friendList = jdbcTemplate.query(findFriend, userMarRower);
        if (userList.isEmpty() || friendList.isEmpty()) {
            throw new NotFoundException("Обновление невозможно, указанного пользователя нет");
        }
        String query = "DELETE from friends where user_id = " + userId + "and friend_id = " + friendId + ";";
        jdbcTemplate.execute(query);
    }

    @Override
    public Set<User> findAllFriends(Long id) {
        String findUser = "SELECT * from users where id = " + id + ";";
        List<User> userList = jdbcTemplate.query(findUser, userMarRower);
        if (userList.isEmpty()) {
            throw new NotFoundException("Обновление невозможно, указанного пользователя нет");
        }
        String query = "SELECT * from users JOIN friends on users.id = friends.friend_id where friends.user_id = ?;";
        return new HashSet<>(jdbcTemplate.query(query, userMarRower, id));
    }

    @Override
    public Set<User> getCommonFriends(Long userId, Long friendId) {
        String query = """
                SELECT *
                FROM users
                WHERE id IN (SELECT friends.friend_Id
                FROM friends
                WHERE friends.user_Id = ? OR friends.user_Id = ?
                GROUP BY friends.friend_Id
                HAVING COUNT(friends.user_Id) = 2);
                """;
        return new HashSet<>(jdbcTemplate.query(query, userMarRower, userId, friendId));
    }
}
