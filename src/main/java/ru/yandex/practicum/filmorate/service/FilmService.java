package ru.yandex.practicum.filmorate.service;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;

@Service
@Slf4j
@Data
@Qualifier("newFilmStorage")
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    FilmService(@Qualifier("newFilmStorage") FilmStorage filmStorage, @Qualifier("newUserStorage") UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Optional<Film> findFilmById(Long id) {
        return filmStorage.getFilm(id);
    }

    public Film createFilm(Film film) {
        return filmStorage.create(film);
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
        if (isNull(filmStorage.getFilm(filmId))) {
            throw new NotFoundException("Указанного фильма не найдено");
        }
        if (isNull(userStorage.getUser(userId))) {
            throw new NotFoundException("Указанного пользователя не найдено");
        }
        if (!filmStorage.getFilm(filmId).get().getLikeList().contains(userId)) {
            throw new NotFoundException("Указанный пользователь лайк к фильму не ставил");
        }
        filmStorage.deleteLikeFromFilm(filmId, userId);
    }

    public List<Film> favoriteFilm(Long x) {
        return filmStorage.findAll().stream().sorted(Comparator.comparingInt(film -> film.getLikeList().size()))
                .limit(x)
                .toList().reversed();
    }
}