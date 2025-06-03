package ru.yandex.practicum.filmorate.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

@Component
@RequiredArgsConstructor
public class FilmResultSetExtractor implements ResultSetExtractor<List<Film>> {

    private final MpaService mpaService;
    private final GenreService genreService;

    @Override
    public List<Film> extractData(ResultSet rs) throws SQLException, DataAccessException {

        Map<Long, Film> films = new HashMap<>();
        while (rs.next()) {
            Long filmId = rs.getLong("id");
            Genre genre = null;
            Integer genreId = rs.getInt("genre_id");
            if (!rs.wasNull()) {
                genre = genreService.findGenreById(genreId);
            }
            if (films.containsKey(filmId)) {
                Film film = films.get(filmId);
                film.getGenres().add(genre);

            } else {
                Film newFilm = new Film();
                newFilm.setId(rs.getLong("id"));
                newFilm.setName(rs.getString("name"));
                newFilm.setDescription(rs.getString("description"));
                newFilm.setReleaseDate(rs.getObject("releaseDate", LocalDate.class));
                newFilm.setDuration(rs.getInt("duration"));
                newFilm.setMpa(mpaService.findMpaById(rs.getInt("mpa_id")));

                if (genre != null) {
                    newFilm.getGenres().add(genre);
                }
                films.put(newFilm.getId(), newFilm);
            }
        }
        return new ArrayList<>(films.values());
    }
}
