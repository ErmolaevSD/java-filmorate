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
    public void addLike(@PathVariable Long id, @PathVariable Long userId) {
        filmService.addLikeFromFilm(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@RequestBody @PathVariable Long id, @PathVariable Long userId) {
        filmService.deleteLikeFromFilm(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> favoriteFilm(@RequestParam(required = false) Long count) {
        if (count == null) {
            return filmService.favoriteFilm(10L);
        } else return filmService.favoriteFilm(count);
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
