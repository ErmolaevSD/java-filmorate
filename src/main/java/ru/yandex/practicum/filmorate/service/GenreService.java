package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.mapper.GenreMapRower;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class GenreService {

    private final JdbcTemplate jdbcTemplate;
    private final GenreMapRower genreMapRower;


    public List<Genre> findAllGenre() {
        String query = "SELECT * FROM genres;";
        return jdbcTemplate.query(query, genreMapRower);
    }

    public Genre findGenreById(Integer id) {
        String findGenre = "SELECT * from genres where genre_id = " + id + ";";
        List<Genre> genreList = jdbcTemplate.query(findGenre, genreMapRower);
        if (genreList.isEmpty()) {
            throw new NotFoundException("Обновление невозможно, указанного пользователя нет");
        }
        String query = "SELECT * from genres where genre_id = ?;";

        return jdbcTemplate.queryForObject(query, genreMapRower, id);
    }
}