package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.mapper.GenreMapRower;
import ru.yandex.practicum.filmorate.mapper.MpaMapRower;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MpaService {

    private final JdbcTemplate jdbcTemplate;
    private final MpaMapRower mpaMapRower;

    public List<Mpa> findAllMpa() {
        String query = "SELECT * FROM mpa;";
        return jdbcTemplate.query(query, mpaMapRower);
    }

    public Optional<Mpa> findMpaById(Integer id) {
        String query = "SELECT * from mpa where mpa_id = ?;";
        return Optional.ofNullable(jdbcTemplate.queryForObject(query, mpaMapRower, id));
    }
}