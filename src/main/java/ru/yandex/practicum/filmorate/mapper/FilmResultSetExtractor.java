package ru.yandex.practicum.filmorate.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            Mpa mpa = null;
            Genre genre = null;
            Integer mpa_id = rs.getInt("mpa_id");
            if (!rs.wasNull()) {
                mpa = mpaService.findMpaById(mpa_id);
            }
            Integer genre_id = rs.getInt("genre_id");
            if (!rs.wasNull()) {
                genre = genreService.findGenreById(genre_id);
            }
            if (films.containsKey(filmId)) {
                Film film = films.get(filmId);
                film.getGenres().add(genre);

            } else {
                Film newFilm = new Film(rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getObject("releaseDate", LocalDate.class),
                        rs.getInt("duration"), mpa);
                newFilm.getGenres().add(genre);
                films.put(newFilm.getId(), newFilm);
            }
        }
        return new ArrayList<>(films.values());
    }
}
