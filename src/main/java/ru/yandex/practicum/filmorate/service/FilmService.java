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

    public Film addLikeFromFilm(Long filmId, Long userId) {
        log.info("Получен запрос на добавление лайка к фильму {} от пользователя {}", filmStorage.getFilmMap().get(filmId), userStorage.getUserMap().get(userId));

        if (isNull(filmStorage.getFilmMap().get(filmId))) {
            throw new NotFoundException("Фильм с id " + filmId + "не найден");
        }
        if (filmStorage.getFilmMap().get(filmId).getLikeList().contains(userId)) {
            throw new ValidationException("Пользователем " + userStorage.getUserMap().get(userId).getName() + "лайки уже поставлен");
        }
        filmStorage.getFilmMap().get(filmId).getLikeList().add(userId);
        return filmStorage.getFilmMap().get(filmId);
    }

    public List<Film> favoriteFilm() {
        List<Film> sortedFilm = (List<Film>) filmStorage.getFilmMap().values();
        return sortedFilm.stream()
                .sorted(Comparator.comparingInt((film) -> filmStorage.getFilmMap().get(film).getLikeList().size()))
                .limit(10)
                .toList();
    }
}