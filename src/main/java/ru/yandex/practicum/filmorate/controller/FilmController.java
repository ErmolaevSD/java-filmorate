package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.*;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;

    @PutMapping("/{id}/like/{userId}")
    public Optional<Film> addLike(@PathVariable Long id, @PathVariable Long userId) {
        return Optional.ofNullable(filmService.addLikeFromFilm(id, userId));
    }



    @GetMapping("/{id}")
    public Optional<Film> findFilm(@RequestBody @PathVariable Long id) {
        return filmService.findFilmById(id);
    }

    @GetMapping
    public Collection<Film> findAll() {
        return filmService.getFilmStorage().findAll();
    }

    @PostMapping
    public Film create(@RequestBody @Valid Film film) {
        return filmService.getFilmStorage().create(film);
    }

    @PutMapping
    public Film update(@RequestBody @Valid Film newFilm) {
        return filmService.getFilmStorage().update(newFilm);
    }
}
