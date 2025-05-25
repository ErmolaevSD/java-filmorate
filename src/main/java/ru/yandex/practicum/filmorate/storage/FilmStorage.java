package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {

    Film create(Film film);

    Film update(Film newFilm);

    void removeFilm(Long id);

    Collection<Film> findAll();

    Film getFilmById(Long id);

    void addLikeFilm(Long filmId, Long userId);

    void deleteLikeFromFilm(Long filmId, Long userId);
}
