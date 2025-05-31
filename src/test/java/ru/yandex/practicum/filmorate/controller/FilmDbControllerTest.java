package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.mapper.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.service.MpaService;
import ru.yandex.practicum.filmorate.storage.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({FilmDbStorage.class, MpaService.class, GenreService.class, GenreMapRower.class, GenreDbStorage.class, MpaDbStorage.class, MpaMapRower.class,FilmMapRower.class, FilmResultSetExtractor.class})
class FilmDbControllerTest {

    private final FilmDbStorage filmDbStorage;
    @Autowired
    private final JdbcTemplate jdbcTemplate;

    private Film newFilm;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("DELETE from films");
        jdbcTemplate.update("ALTER TABLE films ALTER COLUMN id RESTART WITH 1");
        newFilm = new Film();
        newFilm.setName("Test_Name");
        newFilm.setDescription("Test_Description");
        newFilm.setDuration(100);
        newFilm.setReleaseDate(LocalDate.of(2000, 1, 1));
        newFilm.setMpa(new Mpa(1, "G"));
    }

    @Test
    void create() {
        Film createdDbFilm = filmDbStorage.create(newFilm);
        Optional<Film> getCreatedUser = filmDbStorage.getFilm(createdDbFilm.getId());

        assertNotNull(createdDbFilm.getId(), "ID фильма не может быть null");
        assertEquals(1, createdDbFilm.getId(), "ID первого пользователя должен быть 1");
        assertNotNull(getCreatedUser);
    }

    @Test
    void update() {
        filmDbStorage.create(newFilm);
        Film updateUser = new Film();
        updateUser.setId(1L);
        updateUser.setName("Update");
        updateUser.setDescription("Test_Description");
        updateUser.setDuration(100);
        updateUser.setReleaseDate(LocalDate.of(2000, 1, 1));
        updateUser.setMpa(new Mpa(1, "G"));
        filmDbStorage.update(updateUser);

        assertEquals("Update", filmDbStorage.getFilm(1L).get().getName());
        assertEquals(1, filmDbStorage.findAll().size());
    }

    @Test
    void findFilm() {
        filmDbStorage.create(newFilm);
        Optional<Film> film = filmDbStorage.getFilm(newFilm.getId());

        assertEquals(Optional.of(newFilm), film);
    }

    @Test
    void findAll() {
        filmDbStorage.create(newFilm);
        Film updateUser = new Film();
        updateUser.setName("Update");
        updateUser.setDescription("Test_Description");
        updateUser.setDuration(100);
        updateUser.setReleaseDate(LocalDate.of(2000, 1, 1));
        updateUser.setMpa(new Mpa(1, "G"));
        filmDbStorage.create(updateUser);
        Collection<Film> allFilms = filmDbStorage.findAll();

        assertEquals(2, allFilms.size());
    }
}