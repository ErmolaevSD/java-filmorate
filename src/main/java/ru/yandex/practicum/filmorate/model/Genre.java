package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Objects;


@Getter
@RequiredArgsConstructor
public class Genre implements Comparable<Genre> {

    private final Integer id;

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Genre genre = (Genre) object;
        return Objects.equals(id, genre.id) && Objects.equals(name, genre.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @NotNull
    private final String name;

    @Override
    public int compareTo(Genre other) {
        if (other == null) {
            return 1; // null всегда считаем меньше
        }
        if (this.id == null && other.id == null) {
            return 0;
        }
        if (this.id == null) {
            return -1;
        }
        if (other.id == null) {
            return 1;
        }
        return this.id.compareTo(other.id);
    }
}
