//package ru.yandex.practicum.filmorate.controller;
//
//import jakarta.validation.Validation;
//import jakarta.validation.Validator;
//import org.junit.jupiter.api.BeforeEach;
//import ru.yandex.practicum.filmorate.service.UserService;
//import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
//import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
//
//public abstract class AbstractControllerTest {
//
//    protected Validator validator;
//    protected InMemoryFilmStorage filmStorage;
//    protected UserService userService;
//    protected InMemoryUserStorage userStorage;
//
//    @BeforeEach
//    void setUp() {
//
//        filmStorage = new InMemoryFilmStorage();
//        userStorage = new InMemoryUserStorage();
//        userService = new UserService(userStorage);
//        validator = Validation.buildDefaultValidatorFactory().getValidator();
//    }
//}
