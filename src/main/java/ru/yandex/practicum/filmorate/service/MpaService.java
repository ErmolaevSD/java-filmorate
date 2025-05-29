package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.MpaMapRower;
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

    public Mpa findMpaById(Integer id) {
        String findMpa = "SELECT * from mpa where mpa_id = " + id + ";";
        List<Mpa> mpaList = jdbcTemplate.query(findMpa, mpaMapRower);
        if (mpaList.isEmpty()) {
            throw new NotFoundException("Обновление невозможно, указанного пользователя нет");
        }
        String query = "SELECT * from mpa where mpa_id = ?;";
            return jdbcTemplate.queryForObject(query, mpaMapRower, id);
    }
}