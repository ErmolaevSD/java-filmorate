package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.mapper.GenreMapRower;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GenreService {

    private final JdbcTemplate jdbcTemplate;
    private final GenreMapRower genreMapRower;


    public List<Genre> findAllGenre() {
        String query = "SELECT * FROM genres;";
        return jdbcTemplate.query(query, genreMapRower);
    }

    public Optional<Genre> findGenreById(Integer id) {
        String query = "SELECT * from genres where genre_id = ?;";
        return Optional.ofNullable(jdbcTemplate.queryForObject(query, genreMapRower, id));
    }
}