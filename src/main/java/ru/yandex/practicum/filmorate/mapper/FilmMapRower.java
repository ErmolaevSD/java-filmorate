package ru.yandex.practicum.filmorate.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class FilmMapRower implements RowMapper<Film> {

    private final MpaService mpaService;

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Film newFilm = new Film();
        newFilm.setId(rs.getLong("id"));
        newFilm.setName(rs.getString("name"));
        newFilm.setDescription(rs.getString("description"));
        newFilm.setReleaseDate(rs.getObject("releaseDate", LocalDate.class));
        newFilm.setDuration(rs.getInt("duration"));
        newFilm.setMpa(mpaService.findMpaById(rs.getInt("mpa_id")));
        return newFilm;
    }
}
