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
    public List<Film> favoriteFilm(@RequestParam(required = false, defaultValue = "10") Long count) {
        return filmService.favoriteFilm(count);
    }

    @GetMapping("/{id}")
    public Film findFilm(@PathVariable Long id) {
        if (filmService.findFilmById(id).isPresent()) {
            return filmService.findFilmById(id).get();
        }
        return null;
    }

    @GetMapping
    public Collection<Film> findAll() {
        return filmService.findAllFilm();
    }

    @PostMapping
    public Film create(@RequestBody @Valid Film film) {
        return filmService.createFilm(film);
    }

    @PutMapping
    public Film update(@RequestBody @Valid Film newFilm) {
        return filmService.updateFilm(newFilm);
    }
}
