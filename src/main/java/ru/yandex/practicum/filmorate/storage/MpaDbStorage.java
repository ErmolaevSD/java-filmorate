package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.MpaMapRower;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Component
@Repository
@RequiredArgsConstructor
public class MpaDbStorage implements MpaStorage {

    private final JdbcTemplate jdbcTemplate;
    private final MpaMapRower mpaMapRower;


    @Override
    public List<Mpa> findAllMpa() {
        String query = "SELECT * FROM mpa;";
        return jdbcTemplate.query(query, mpaMapRower);
    }

    @Override
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