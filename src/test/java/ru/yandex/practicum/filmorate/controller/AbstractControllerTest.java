package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;

public abstract class AbstractControllerTest {

    protected Validator validator;
    protected FilmController filmController;
    protected UserController userController;

    @BeforeEach
    void setUp() {
        filmController = new FilmController();
        userController = new UserController();
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }
}
