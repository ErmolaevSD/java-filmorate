package ru.yandex.practicum.filmorate.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.enums.MpaRating;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@Component
public class FilmMapRower implements RowMapper<Film> {

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {

        Integer mpa_id = rs.getInt("mpa_id");
        MpaRating mpaRating = MpaRating.fromDbValue(mpa_id);

        return new Film(rs.getLong("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getObject("releaseDate", LocalDate.class),
                rs.getInt("duration"), mpaRating);
    }
}