package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PutMapping("/{id}/friends/{friendId}")
    public Optional<User> addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        return Optional.ofNullable(userService.addFriend(id, friendId));
    }

    // GET - запрос на общих друзей двух пользователей

    @DeleteMapping("/{id}/friends/{friendId}")
    public Optional<User> deleteFriend(@PathVariable Long id, @PathVariable Long friendId) {
        return Optional.ofNullable(userService.deleteFriend(id, friendId));
    }

    @GetMapping("/{id}/friends")
    public Set<Long> friendByUser(@PathVariable Long id) {
        return userService.findAllFriends(id);
    }

    @GetMapping
    public Collection<User> findAll() {
        return userService.getUserStorage().findAll();
    }

    @GetMapping("/{id}")
    public Optional<User> findUser(@PathVariable @Valid Long id) {
        return userService.findUserById(id);

    }

    @PostMapping
    public User create(@RequestBody @Valid User user) {
        return userService.getUserStorage().create(user);
    }

    @PutMapping
    public User update(@RequestBody @Valid User newUser) {
        return userService.getUserStorage().update(newUser);
    }
}
