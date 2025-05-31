package ru.yandex.practicum.filmorate.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@Component
public class FilmMapRower implements RowMapper<Film> {

    private final MpaService mpaService;

    public FilmMapRower(MpaService mpaService) {
        this.mpaService = mpaService;
    }

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Mpa mpa = null;
        Integer mpa_id = rs.getInt("mpa_id");
        if (!rs.wasNull()) {
            mpa = mpaService.findMpaById(mpa_id);
        }

        Film newFilm = new Film();

        newFilm.setId(rs.getLong("id"));
        newFilm.setName( rs.getString("name"));
        newFilm.setDescription(rs.getString("description"));
        newFilm.setReleaseDate(rs.getObject("releaseDate", LocalDate.class));
        newFilm.setDuration(rs.getInt("duration"));
        newFilm.setMpa(mpa);

        return newFilm;

    }
}
