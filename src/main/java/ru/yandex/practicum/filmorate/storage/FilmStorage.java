package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import java.util.Collection;
import java.util.Optional;

public interface FilmStorage {

    Film create(Film film);

    Film update(Film newFilm);

    void removeFilm(Long id);

    Optional<Film> getFilm(Long id);

    Collection<Film> findAll();

    void addLikeFromFilm(Long filmId, Long userId);

    void deleteLikeFromFilm(Long filmId, Long userId);
}
