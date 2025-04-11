package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest extends AbstractControllerTest {

    @Test
    void createValidFilm() {
        Film film = new Film(null, "Рембо", "Боевик", LocalDate.of(2021, 10, 10), 100);

        assertTrue(validator.validate(film).isEmpty());
    }

    @Test
    void createUnvalidatedName() {
        Film nullNameFilm = new Film(null, null, "Боевик", LocalDate.of(2021, 10, 10), 100);
        Film blankNameFilm = new Film(null, "", "Боевик", LocalDate.of(2021, 10, 10), 100);

        assertFalse(validator.validate(nullNameFilm).isEmpty());
        assertFalse(validator.validate(blankNameFilm).isEmpty());
    }

    @Test
    void createUnvalidatedDescription() {
        Film unvalidatedFilm = new Film(null, "Титаник", "a".repeat(201), LocalDate.of(2021, 10, 10), 100);

        assertFalse(validator.validate(unvalidatedFilm).isEmpty());
    }

    @Test
    void createUnvalidatedDuration() {
        Film unvalidatedFilm = new Film(null, "Титаник", "Мелодрамма", LocalDate.of(2021, 10, 10), -100);

        assertFalse(validator.validate(unvalidatedFilm).isEmpty());
    }

    @Test
    void createUnvalidatedReleasedDate() {
        Film unvalidatedFilm = new Film(null, "Титаник", "Мелодрамма", LocalDate.of(1500, 10, 10), 100);

        Film unvalidatedFilm1 = new Film(null, "Титаник", "Мелодрамма", LocalDate.of(3000, 10, 10), 100);

        assertThrows(ValidationException.class, () -> filmController.create(unvalidatedFilm));
        assertFalse(validator.validate(unvalidatedFilm1).isEmpty());
    }

    @Test
    void findAllTest() {
        Film film = new Film(null, "Рембо", "Боевик", LocalDate.of(2021, 10, 10), 100);
        Film film1 = new Film(null, "Рембо", "Боевик", LocalDate.of(2021, 10, 10), 100);
        filmController.create(film);
        filmController.create(film1);

        assertEquals(2, filmController.findAll().size());
    }

    @Test
    void unvalitedUpdateTest() {
        Film film = new Film(null, "Рембо", "Боевик", LocalDate.of(2021, 10, 10), 100);
        filmController.create(film);
        Film updateFilm = new Film(2L, "Рембо", "Боевик", LocalDate.of(2021, 10, 10), 100);

        assertThrows(ValidationException.class, () -> filmController.update(updateFilm));
    }

    @Test
    void updateTest() {
        Film film = new Film(null, "Рембо", "Боевик", LocalDate.of(2021, 10, 10), 100);
        Film created = filmController.create(film);
        Film updateFilm = new Film(1L, "Титаник", "Боевик", LocalDate.of(2021, 10, 10), 100);
        filmController.update(updateFilm);
        String newName = "Титаник";

        assertEquals(newName, created.getName());
    }
}