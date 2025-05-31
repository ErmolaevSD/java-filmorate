package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.GenreMapRower;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;
    private final GenreMapRower genreMapRower;

    @Override
    public List<Genre> findAllGenre() {
        String query = "SELECT * FROM genres;";
        return jdbcTemplate.query(query, genreMapRower);
    }

    @Override
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
