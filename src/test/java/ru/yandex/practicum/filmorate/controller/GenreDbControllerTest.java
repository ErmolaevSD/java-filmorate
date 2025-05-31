package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.mapper.GenreMapRower;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({GenreDbStorage.class, GenreMapRower.class})
class GenreDbControllerTest {
    private final GenreStorage genreStorage;

    @Test
    void findAllGenre() {
        List<Genre> genreList = genreStorage.findAllGenre();

        assertFalse(genreList.isEmpty(), "Список жанров не должен быть пустым");
        assertEquals(6, genreList.size());
    }

    @Test
    void findGenreById() {
        Genre genreById = genreStorage.findGenreById(1);

        Genre genreComedy = new Genre(1, "Комедия");
        assertEquals(genreComedy, genreById);
    }
}