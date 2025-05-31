package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;

@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;

    @GetMapping
    public List<Genre> findAllGenre() {
        return genreService.findAllGenre();
    }

    @GetMapping("/{id}")
    public Genre findGenreById(@RequestBody @PathVariable Integer id) {
        return genreService.findGenreById(id);
    }
}
