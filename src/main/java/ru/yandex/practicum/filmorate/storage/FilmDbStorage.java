package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.FilmMapRower;
import ru.yandex.practicum.filmorate.mapper.FilmResultSetExtractor;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


@Component("newFilmStorage")
@Slf4j
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final FilmMapRower filmMapRower;
    private final GenreService genreService;
    private final FilmResultSetExtractor filmResultSetExtractor;

    @Override
    public Film update(Film newFilm) {

        Collection<Film> findAll = findAll();
        if (!findAll.contains(newFilm)) {
            throw new NotFoundException("Фильм не найден");
        }

        Long id = newFilm.getId();
        String newName = newFilm.getName();
        String newDescription = newFilm.getDescription();
        LocalDate newFilmReleaseDate = newFilm.getReleaseDate();
        Integer duration = newFilm.getDuration();

        Integer mpa_id = newFilm.getMpa().getId();

        String sql = "UPDATE films SET name = '" + newName + "', description = '" + newDescription + "', releaseDate = '" + newFilmReleaseDate + "', duration = " + duration + ", mpa_id = " + mpa_id + " WHERE id = " + id + ";";
        jdbcTemplate.execute(sql);
        return newFilm;
    }

    @Override
    public void removeFilm(Long id) {
        String query = "DELETE from films where id = " + id + ";";
        jdbcTemplate.execute(query);
    }

    @Override
    public Optional<Film> getFilm(Long id) {
        String quer = "SELECT f.id AS film_id, f.name, f.description, f.releaseDate, f.duration, f.mpa_id, m.mpa_id AS mpa_id, m.description AS mpa_description, g.genre_id AS genre_id,g.description AS genre_description " +
                "FROM films f " +
                "LEFT JOIN mpa m ON f.mpa_id = m.mpa_id LEFT JOIN film_genre fg ON f.id = fg.film_id LEFT JOIN genres g ON fg.genre_id = g.genre_id WHERE f.id = ? ORDER BY g.genre_id;";

        List<Film> films = jdbcTemplate.query(quer, filmResultSetExtractor, id);
        Film film = films.get(0);
        return Optional.of(film);
    }

    @Override
    public Collection<Film> findAll() {
        String FIND_ALL_FILMS = "SELECT * FROM films;";
        return jdbcTemplate.query(FIND_ALL_FILMS, filmMapRower);
    }

    @Override
    public void addLikeFromFilm(Long filmId, Long userId) {
        String query = "INSERT INTO filmLike (film_id, user_id) VALUES ('" + filmId + "' , '" + userId + "');";
        jdbcTemplate.execute(query);
    }

    @Override
    public void deleteLikeFromFilm(Long filmId, Long userId) {
        String query = "DELETE from filmLike where film_Id = " + filmId + "and user_Id = " + userId + ";";
        jdbcTemplate.execute(query);
    }

    @Override
    public List<Film> favoriteFilm(Long x) {
        String query = "SELECT films.* FROM films " +
                "JOIN filmLike ON films.id = filmLike.film_id " +
                "GROUP BY films.id " +
                "ORDER BY COUNT(filmLike.user_id) DESC " +
                "LIMIT ?;";
        return jdbcTemplate.query(query, filmMapRower, x);
    }

    @Override
    public Film create(Film film) {
        validateFilm(film);

        Long filmId = saveFilm(film);
        film.setId(filmId);

            // Сохраняем жанры, если они есть
    if (film.getGenres() != null && !film.getGenres().isEmpty()) {
        saveFilmGenres(film,filmId, film.getGenres());
    }

//        // Удаляем дубликаты жанров перед сохранением
//        if (film.getGenres() != null) {
//            List<Genre> uniqueGenres = film.getGenres().stream()
//                    .filter(Objects::nonNull)
//                    .distinct()
//                    .collect(Collectors.toList());
//            film.getGenres().addAll(uniqueGenres);
//            // Проверка существования жанров
//            if (!uniqueGenres.isEmpty()) {
//                String inSql = String.join(",", Collections.nCopies(uniqueGenres.size(), "?"));
//                List<Integer> genreIds = uniqueGenres.stream().map(Genre::getId).collect(Collectors.toList());
//
//                if (film.getGenres() != null && !film.getGenres().isEmpty()) {
//                    // Сортируем и удаляем дубликаты
//                    Set<Genre> uniqueSortedGenres = film.getGenres().stream()
//                            .filter(Objects::nonNull)
//                            .sorted(Comparator.comparingInt(Genre::getId))
//                            .collect(Collectors.toCollection(LinkedHashSet::new));
//
//                    film.getGenres().addAll(new ArrayList<>(uniqueSortedGenres));
//
//                    List<Integer> existingIds = jdbcTemplate.queryForList(
//                            "SELECT genre_id FROM genres WHERE genre_id IN (" + inSql + ")",
//                            Integer.class, genreIds.toArray());
//
//                    if (existingIds.size() != uniqueGenres.size()) {
//                        Set<Integer> missingIds = new HashSet<>(genreIds);
//                        missingIds.removeAll(existingIds);
//                        throw new NotFoundException("Жанры с ID " + missingIds + " не существуют");
//                    }
//                }
//
//            }
//
//            if (film.getGenres() != null && !film.getGenres().isEmpty()) {
//                String genreQuery = "INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?)";
//                //Set<Genre> uniqueGenres = new LinkedHashSet<>(film.getGenres());
//
//                jdbcTemplate.batchUpdate(genreQuery,
//                        uniqueGenres.stream().map(genre -> new Object[]{film.getId(), genre.getId()}).collect(Collectors.toList()),
//                        new int[]{Types.BIGINT, Types.INTEGER});
//            }
//        }
        return film;
    }

    private Long saveFilm(Film film) {
        String query = "INSERT INTO films (name, description, releaseDate, duration, mpa_id) VALUES (?,?,?,?,?);";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setObject(3, film.getReleaseDate());
            ps.setInt(4, film.getDuration());
            ps.setInt(5, film.getMpa().getId());
            return ps;
        }, keyHolder);

        return keyHolder.getKeyAs(Long.class);
    }

//@Override


//
//    return film;
//}
//
    private void validateFilm(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза должна быть позднее 28.12.1895");
        }

        if (film.getMpa() == null || film.getMpa().getId() == null) {
            throw new ValidationException("MPA обязательно должно быть указано");
        }
    }
//
    private void saveFilmGenres(Film film,Long filmId, Set<Genre> genres) {
        // Удаляем дубликаты и null-значения

        Set<Genre> uniqueGenres = genres.stream()
                .filter(Objects::nonNull)
                .filter(genre -> genre.getId() != null)
                .collect(Collectors.toCollection(
                        () -> new TreeSet<>(Comparator.comparing(Genre::getId))
                ));
        Set<Genre> sorted = new TreeSet<>(uniqueGenres);

//        Set<Genre> uniqueGenres = new TreeSet<>(Comparator.nullsLast(Comparator.comparing(Genre::getId,Comparator.nullsLast(Comparator.naturalOrder()))));
//                uniqueGenres.addAll(genres);

        // Проверяем существование жанров в БД
        validateGenresExist(uniqueGenres);

        // Сохраняем связи
        String genreQuery = "INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?)";
        film.getGenres().clear();
        film.getGenres().addAll(sorted);

        film.getGenres().stream().sorted();




        String sql = "INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?)";
        List<Object[]> batchArgs = film.getGenres().stream()
                .sorted(Comparator.comparing(Genre::getId))
                .map(g -> new Object[]{film.getId(), g.getId()})
                .collect(Collectors.toList());

        jdbcTemplate.batchUpdate(sql, batchArgs);

//
//        jdbcTemplate.batchUpdate(genreQuery,
//                uniqueGenres.stream()
//                        .map(genre -> new Object[]{filmId, genre.getId()})
//                        .collect(Collectors.toList()),
//                new int[]{Types.BIGINT, Types.INTEGER});





    }

    private void validateGenresExist(Set<Genre> genres) {
        if (genres.isEmpty()) return;

        List<Integer> genreIds = genres.stream()
                .map(Genre::getId)
                .collect(Collectors.toList());

        String inSql = String.join(",", Collections.nCopies(genreIds.size(), "?"));
        List<Integer> existingIds = jdbcTemplate.queryForList(
                "SELECT genre_id FROM genres WHERE genre_id IN (" + inSql + ")",
                Integer.class,
                genreIds.toArray());

        if (existingIds.size() != genreIds.size()) {
            Set<Integer> missingIds = new HashSet<>(genreIds);
            missingIds.removeAll(existingIds);
            throw new NotFoundException("Жанры с ID " + missingIds + " не существуют");
        }
    }
}

