package ru.yandex.practicum.filmorate.storage;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Data
@Component("oldFilmStorage")
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> filmMap = new HashMap<>();

    @Override
    public Collection<Film> findAll() {
        log.info("Получен запрос на получение списка фильмов");
        return Collections.unmodifiableCollection(filmMap.values());
    }

    public Film getFilmById(Long id) {
        return filmMap.get(id);
    }

    @Override
    public Film create(Film film) {
        log.info("Получен запрос на создание фильма: {}", film);
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза должна быть позднее 28.12.1895");
        }
        film.setId(getNextId());
        filmMap.put(film.getId(), film);
        log.info("Успешно обработан запрос на создание фильма: {}", film);
        return film;
    }

    @Override
    public Film update(Film newFilm) {
        log.info("Получен запрос на обновление фильма: {}", newFilm);
        if (!filmMap.containsKey(newFilm.getId())) {
            throw new NotFoundException("Указанного фильма не найдено. Обновление невозможно");
        }
        Film oldFilm = filmMap.get(newFilm.getId());
        oldFilm.setDescription(newFilm.getDescription());
        oldFilm.setName(newFilm.getName());
        oldFilm.setReleaseDate(newFilm.getReleaseDate());
        oldFilm.setDuration(newFilm.getDuration());
        log.info("Успешно обработан запрос на обновление фильма: {}", newFilm);
        return oldFilm;
    }

    @Override
    public void removeFilm(Long id) {
        log.info("Получен запрос на удаление фильма.");
        if (!filmMap.containsKey(id)) {
            log.info("Запрос на удаление фильма не обработан. Причина: фильм с id {} не найден.", id);
            throw new NotFoundException("Указанного фильма не найдено. Удаление невозможно");
        }
        log.info("Запрос на удаление фильма успешно обработан.");
    }

    public void deleteLikeFromFilm(Long filmId, Long userId) {
        filmMap.get(filmId).getLikeList().remove(userId);
    }

    public boolean findLikeFromUser(Long filmId, Long userId) {
        return filmMap.get(filmId).getLikeList().contains(userId);
    }

    private long getNextId() {
        long currentMaxId = filmMap.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    public void addLikeFilm(Long filmId, Long userId) {
        //        log.info("Получен запрос на добавление лайка к фильму {} от пользователя {}", filmStorage.getFilm(filmId), userStorage.getUser(userId));
//
//        if (isNull(filmStorage.getFilm(filmId))) {
//            throw new NotFoundException("Фильм с id " + filmId + "не найден");
//        }
//        if (isNull(userStorage.getUser(userId))) {
//            throw new NotFoundException("Пользователь не найден");
//        }
//        if (filmStorage.findLikeFromUser(filmId, userId)) {
//            throw new ValidationException("Пользователем " + userStorage.getUser(userId).getName() + "лайки уже поставлен");
//        }
//        filmStorage.addLikeFromFilm(filmId, userId);




        filmMap.get(filmId).getLikeList().add(userId);
    }

}


//    public void addLikeFromFilm(Long filmId, Long userId) {

//    }

//    public void deleteLikeFromFilm(Long filmId, Long userId) {
//        if (isNull(filmStorage.getFilm(filmId))) {
//            throw new NotFoundException("Указанного фильма не найдено");
//        }
//        if (isNull(userStorage.getUser(userId))) {
//            throw new NotFoundException("Указанного пользователя не найдено");
//        }
//        if (!filmStorage.getFilmMap().get(filmId).getLikeList().contains(userId)) {
//            throw new NotFoundException("Указанный пользователь лайк к фильму не ставил");
//        }
//        filmStorage.deleteLikeFromFilm(filmId, userId);
//    }