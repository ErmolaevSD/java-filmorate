package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.mapper.FilmMapRower;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;

@Component("newFilmStorage")
@RequiredArgsConstructor
@Slf4j
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final FilmMapRower filmMapRower;

    private final String FIND_ALL_FILMS = "SELECT * FROM films;";

    @Override
    public Collection<Film> findAll() {
        log.info("Получен запрос на получение списка фильмов из БД");
        return jdbcTemplate.query(FIND_ALL_FILMS, filmMapRower);
    }

    @Override
    public Film getFilmById(Long id) {
        String query = "SELECT * FROM films WHERE id = ?;";
        return jdbcTemplate.queryForObject(query, filmMapRower, id);
    }

    @Override
    public void removeFilm(Long id) {
        String query = "DELETE from films where id = " + id + ";";
        jdbcTemplate.execute(query);
    }

    public void addLikeFilm(Long filmId, Long userId) {
        String query = "INSERT INTO filmLike (film_id, user_id) VALUES ('" + filmId + "' , '" + userId + "');";
        jdbcTemplate.execute(query);
    }

    @Override
    public void deleteLikeFromFilm(Long filmId, Long userId) {
        String query = "DELETE from filmLike where filmId = " + filmId + "and userId = " + userId + ";";
        jdbcTemplate.execute(query);
    }

    @Override
    public Film update(Film newFilm) {
        Long id = newFilm.getId();
        String newName = newFilm.getName();
        String newDescription = newFilm.getDescription();
        LocalDate newFilmReleaseDate = newFilm.getReleaseDate();
        Integer duration = newFilm.getDuration();
        Integer mpa_id = newFilm.getMpaRating().getMpaId();

        String sql = "UPDATE films SET name = " + newName + ", description = " +  newDescription + ", releaseDate = " + newFilmReleaseDate + ", duration = " + duration + ", mpa_id = " + mpa_id + " WHERE id = " + id + ";";
        jdbcTemplate.execute(sql);
        return newFilm;
    }

    @Override
    public Film create(Film film) {
//        String name = film.getName();
//        String desctiprion = film.getDescription();
//        LocalDate releaseDate = film.getReleaseDate();
//        Integer duration = film.getDuration();
//        Integer mpa_id = MpaRating.findMpa_id(film.getMpaRating());
//        String query = "INSERT INTO films (name, description, releaseDate, duration, mpa_id) VALUES (" + name + desctiprion + releaseDate + duration + mpa_id + ");" ;
//                log.info("Получен запрос на создание фильма: {}", film);
//        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
//            throw new ValidationException("Дата релиза должна быть позднее 28.12.1895");
//        }
//        log.info("Успешно обработан запрос на создание фильма: {}", film);
//
//        return jdbcTemplate.
        return null;
    }


}
