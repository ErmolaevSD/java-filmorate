package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;
import java.util.Set;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        userService.addFriend(id, friendId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Set<User> getCommonFriend(@PathVariable Long id, @PathVariable Long otherId) {
        return userService.commonFriend(id, otherId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable Long id, @PathVariable Long friendId) {
        userService.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public Set<User> friendByUser(@PathVariable Long id) {
        return userService.findAllFriends(id);
    }

    @GetMapping
    public Collection<User> findAll() {
        return userService.findAllUser();
    }

    @GetMapping("/{id}")
    public User findUser(@PathVariable @Valid Long id) {
        return userService.findUserById(id);

    }

    @PostMapping
    public User create(@RequestBody @Valid User user) {
        return userService.createUser(user);
    }

    @PutMapping
    public User update(@RequestBody @Valid User newUser) {
        return userService.updateUser(newUser);
    }
}
