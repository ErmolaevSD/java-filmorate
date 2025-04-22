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

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;

@Service
@Slf4j
@RequiredArgsConstructor
@Data
public class FilmService {

    private final InMemoryFilmStorage filmStorage;
    private final InMemoryUserStorage userStorage;

    public Optional<Film> findFilmById(Long id) {
        return Optional.ofNullable(filmStorage.getFilmMap().get(id));
    }

    public void addLikeFromFilm(Long filmId, Long userId) {
        log.info("Получен запрос на добавление лайка к фильму {} от пользователя {}", filmStorage.getFilmMap().get(filmId), userStorage.getUserMap().get(userId));

        if (isNull(filmStorage.getFilmMap().get(filmId))) {
            throw new NotFoundException("Фильм с id " + filmId + "не найден");
        }
        if (filmStorage.getFilmMap().get(filmId).getLikeList().contains(userId)) {
            throw new ValidationException("Пользователем " + userStorage.getUserMap().get(userId).getName() + "лайки уже поставлен");
        }
        filmStorage.getFilmMap().get(filmId).getLikeList().add(userId);
    }

    public void deleteLikeFromFilm(Long filmId, Long userId) {
        if (isNull(filmStorage.getFilmMap().get(filmId))) {
            throw new NotFoundException("Указанного фильма не найдено");
        }
        if (isNull(userStorage.getUserMap().get(userId))) {
            throw new NotFoundException("Указанного пользователя не найдено");
        }
        if (!filmStorage.getFilmMap().get(filmId).getLikeList().contains(userId)) {
            throw new NotFoundException("Указанный пользователь лайк к фильму не ставил");
        }
        filmStorage.getFilmMap().get(filmId).getLikeList().remove(userId);
    }


    public List<Film> favoriteFilm(Long x) {
        return filmStorage.getFilmMap().values().stream()
                .sorted(Comparator.comparingInt(film -> film.getLikeList().size()))
                .limit(x)
                .toList();
    }
}