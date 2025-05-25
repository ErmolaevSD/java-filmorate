package ru.yandex.practicum.filmorate.service;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;

@Service
@Slf4j
@Data
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    FilmService(@Qualifier("newFilmStorage") FilmStorage filmStorage, @Qualifier("newUserStorage") UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film findFilmById(Long id) {
        return filmStorage.getFilmById(id);
    }

    public Collection<Film> findAllFilm() {
        return filmStorage.findAll();
    }

    public void removeFilm(Long id) {
        filmStorage.removeFilm(id);
    }

    public Film createFilm(Film film) {
        return filmStorage.create(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.update(film);
    }

    public void addLikeFromFilm(Long filmId, Long userId) {
        filmStorage.addLikeFilm(filmId, userId);
    }

    public void deleteLikeFromFilm(Long filmId, Long userId) {
    filmStorage.deleteLikeFromFilm(filmId, userId);
    }


//    public List<Film> favoriteFilm(Long x) {
//        return filmStorage.favoriteFilm(x);
//    }
}