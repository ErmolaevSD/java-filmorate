package ru.yandex.practicum.filmorate.enums;

import ru.yandex.practicum.filmorate.exception.NotFoundException;

public enum Genre {
    COMEDY(1),
    DRAMA(2),
    ANIMATION(3),
    THRILLER(4),
    DOCUMENTARY(5),
    ACTION(6);

    private final Integer dbValue;

    Genre(Integer dbValue) {
        this.dbValue = dbValue;
    }

    public static Genre fromDbValue(Integer dbValue) {
        for (Genre genre : values()) {
            if (genre.dbValue.equals(dbValue)) {
                return genre;
            }
        }
        throw new NotFoundException("Указанный жанр не найден");
    }
}