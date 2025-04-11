package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
public class FilmService {

    private final Map<Long, Film> filmMap = new HashMap<>();

    public Collection<Film> findAll() {
        log.info("Получен запрос на получение списка фильмов");
        return Collections.unmodifiableCollection(filmMap.values());
    }

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

    public Film update(Film newFilm) {
        log.info("Получен запрос на обновление фильма: {}", newFilm);
        if (!filmMap.containsKey(newFilm.getId())) {
            throw new ValidationException("Указанного фильма не найдено. Обновление невозможно");
        }
        Film oldFilm = filmMap.get(newFilm.getId());
        oldFilm.setDescription(newFilm.getDescription());
        oldFilm.setName(newFilm.getName());
        oldFilm.setReleaseDate(newFilm.getReleaseDate());
        oldFilm.setDuration(newFilm.getDuration());
        log.info("Успешно обработан запрос на обновление фильма: {}", newFilm);
        return oldFilm;
    }

    private long getNextId() {
        long currentMaxId = filmMap.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
