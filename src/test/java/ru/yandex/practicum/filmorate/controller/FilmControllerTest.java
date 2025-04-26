package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest extends AbstractControllerTest {

    @Test
    void createValidFilm() {
        Film film = new Film(null, "Рембо", "Боевик", LocalDate.of(2021, 10, 10), 100, new HashSet<>());

        assertTrue(validator.validate(film).isEmpty());
    }

    @Test
    void createUnvalidatedName() {
        Film nullNameFilm = new Film(null, null, "Боевик", LocalDate.of(2021, 10, 10), 100, new HashSet<>());
        Film blankNameFilm = new Film(null, "", "Боевик", LocalDate.of(2021, 10, 10), 100, new HashSet<>());

        assertFalse(validator.validate(nullNameFilm).isEmpty());
        assertFalse(validator.validate(blankNameFilm).isEmpty());
    }

    @Test
    void createUnvalidatedDescription() {
        Film unvalidatedFilm = new Film(null, "Титаник", "a".repeat(201), LocalDate.of(2021, 10, 10), 100, new HashSet<>());

        assertFalse(validator.validate(unvalidatedFilm).isEmpty());
    }

    @Test
    void createUnvalidatedDuration() {
        Film unvalidatedFilm = new Film(null, "Титаник", "Мелодрамма", LocalDate.of(2021, 10, 10), -100, new HashSet<>());

        assertFalse(validator.validate(unvalidatedFilm).isEmpty());
    }

    @Test
    void createUnvalidatedReleasedDate() {
        Film unvalidatedFilm = new Film(null, "Титаник", "Мелодрамма", LocalDate.of(1500, 10, 10), 100,new HashSet<>());

        Film unvalidatedFilm1 = new Film(null, "Титаник", "Мелодрамма", LocalDate.of(3000, 10, 10), 100, new HashSet<>());

        assertThrows(ValidationException.class, () -> filmStorage.create(unvalidatedFilm));
        assertFalse(validator.validate(unvalidatedFilm1).isEmpty());
    }

    @Test
    void findAllTest() {
        Film film = new Film(null, "Рембо", "Боевик", LocalDate.of(2021, 10, 10), 100, new HashSet<>());
        Film film1 = new Film(null, "Рембо", "Боевик", LocalDate.of(2021, 10, 10), 100, new HashSet<>());
        filmStorage.create(film);
        filmStorage.create(film1);

        assertEquals(2, filmStorage.findAll().size());
    }

    @Test
    void unvalitedUpdateTest() {
        Film film = new Film(null, "Рембо", "Боевик", LocalDate.of(2021, 10, 10), 100, new HashSet<>());
        filmStorage.create(film);
        Film updateFilm = new Film(2L, "Рембо", "Боевик", LocalDate.of(2021, 10, 10), 100, new HashSet<>());

        assertThrows(NotFoundException.class, () -> filmStorage.update(updateFilm));
    }

    @Test
    void updateTest() {
        Film film = new Film(null, "Рембо", "Боевик", LocalDate.of(2021, 10, 10), 100, new HashSet<>());
        Film created = filmStorage.create(film);
        Film updateFilm = new Film(1L, "Титаник", "Боевик", LocalDate.of(2021, 10, 10), 100, new HashSet<>());
        filmStorage.update(updateFilm);
        String newName = "Титаник";

        assertEquals(newName, created.getName());
    }
}