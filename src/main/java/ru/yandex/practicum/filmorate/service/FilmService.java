package ru.yandex.practicum.filmorate.service;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

import static java.util.Objects.isNull;

@Service
@Slf4j
@Data
@Qualifier("newFilmStorage")
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final MpaService mpaService;
    private final GenreService genreService;

    FilmService(@Qualifier("newFilmStorage") FilmStorage filmStorage, @Qualifier("newUserStorage") UserStorage userStorage, MpaService mpaService, GenreService genreService) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.mpaService = mpaService;
        this.genreService = genreService;
    }

    public Optional<Film> findFilmById(Long id) {
        return filmStorage.getFilm(id);
    }

    public Film createFilm(Film film) {
        try {
            mpaService.findMpaById(film.getMpa().getId());
            return filmStorage.create(film);
        } catch (NotFoundException notFoundException) {
            throw new NotFoundException("Mpa не найден");
        }
    }

    public Collection<Film> findAllFilm() {
        return filmStorage.findAll();
    }

    public Film updateFilm(Film film) {
        return filmStorage.update(film);
    }

    public void addLikeFromFilm(Long filmId, Long userId) {
        log.info("Получен запрос на добавление лайка к фильму {} от пользователя {}", filmStorage.getFilm(filmId), userStorage.getUser(userId));

        if (isNull(filmStorage.getFilm(filmId))) {
            throw new NotFoundException("Фильм с id " + filmId + "не найден");
        }
        if (isNull(userStorage.getUser(userId))) {
            throw new NotFoundException("Пользователь не найден");
        }
        filmStorage.addLikeFromFilm(filmId, userId);
    }

    public void deleteLikeFromFilm(Long filmId, Long userId) {
        filmStorage.deleteLikeFromFilm(filmId, userId);
    }

    public List<Film> favoriteFilm(Long x) {
        return filmStorage.favoriteFilm(x);
    }
}