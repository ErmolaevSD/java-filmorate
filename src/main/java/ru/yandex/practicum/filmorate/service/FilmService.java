package ru.yandex.practicum.filmorate.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import static java.util.Objects.isNull;

@Service
@Slf4j
@RequiredArgsConstructor
@Data
public class FilmService {

    private final InMemoryFilmStorage filmStorage;
    private final InMemoryUserStorage userStorage;

    public Film findFilmById(Long id) {
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
        if (filmStorage.findLikeFromUser(filmId, userId)) {
            throw new ValidationException("Пользователем " + userStorage.getUser(userId).getName() + "лайки уже поставлен");
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
        if (!filmStorage.getFilmMap().get(filmId).getLikeList().contains(userId)) {
            throw new NotFoundException("Указанный пользователь лайк к фильму не ставил");
        }
        filmStorage.deleteLikeFromFilm(filmId, userId);
    }

    public List<Film> favoriteFilm(Long x) {
        return filmStorage.getFilmMap().values().stream()
                .sorted(Comparator.comparingInt(film -> film.getLikeList().size()))
                .limit(x)
                .toList().reversed();
    }
}