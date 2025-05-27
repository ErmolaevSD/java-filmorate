package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.FilmMapRower;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.*;

@Component("newFilmStorage")
@Slf4j
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final FilmMapRower filmMapRower;

    @Override
    public Film update(Film newFilm) {
        Long id = newFilm.getId();
        String newName = newFilm.getName();
        String newDescription = newFilm.getDescription();
        LocalDate newFilmReleaseDate = newFilm.getReleaseDate();
        Integer duration = newFilm.getDuration();
        Integer mpa_id = newFilm.getMpaRating().getMpaId();
        String sql = "UPDATE films SET name = '" + newName + "', description = '" + newDescription + "', releaseDate = '" + newFilmReleaseDate + "', duration = " + duration + ", mpa_id = " + mpa_id + " WHERE id = " + id + ";";
        jdbcTemplate.execute(sql);
        return newFilm;
    }

    @Override
    public void removeFilm(Long id) {
        String query = "DELETE from films where id = " + id + ";";
        jdbcTemplate.execute(query);
    }

    @Override
    public Optional<Film> getFilm(Long id) {
        String query = "SELECT * FROM films WHERE id = ?;";
        Film result = jdbcTemplate.queryForObject(query, filmMapRower, id);
        return Optional.ofNullable(result);
    }

    @Override
    public Collection<Film> findAll() {
        String FIND_ALL_FILMS = "SELECT * FROM films;";
        return jdbcTemplate.query(FIND_ALL_FILMS, filmMapRower);
    }

    @Override
    public void addLikeFromFilm(Long filmId, Long userId) {
        String query = "INSERT INTO filmLike (film_id, user_id) VALUES ('" + filmId + "' , '" + userId + "');";
        jdbcTemplate.execute(query);
    }

    public void deleteLikeFromFilm(Long filmId, Long userId) {
        String query = "DELETE from filmLike where filmId = " + filmId + "and userId = " + userId + ";";
        jdbcTemplate.execute(query);
    }

    @Override
    public Film create(Film film) {
        String query = "INSERT INTO films (name, description, releaseDate, duration, mpa_id) VALUES (?,?,?,?,?);";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            ps.setObject(1, film.getName());
            ps.setObject(2, film.getDescription());
            ps.setObject(3, film.getReleaseDate());
            ps.setObject(4, film.getDuration());
            ps.setObject(5, film.getMpaRating().getMpaId());
            return ps;
        }, keyHolder);

        Long id = keyHolder.getKeyAs(Long.class);

        if (id != null) {
            film.setId(id);
        } else {
            throw new NotFoundException("Не удалось сохранить данные");
        }
        return film;
    }
}
