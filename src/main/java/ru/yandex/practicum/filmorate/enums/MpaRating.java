package ru.yandex.practicum.filmorate.enums;

import lombok.Getter;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

@Getter
public enum MpaRating {
    G(1),
    PG(2),
    PG_13(3),
    R(4),
    NC_17(5);

    private final Integer dbValue;

    MpaRating(Integer dbValue) {
        this.dbValue = dbValue;
    }

    public static MpaRating fromDbValue(Integer dbValue) {
        for (MpaRating mpaRating : values()) {
            if (mpaRating.dbValue.equals(dbValue)) {
                return mpaRating;
            }
        }
        throw new NotFoundException("Указанный рейтинг не найден");
    }

    public Integer getMpaId() {
        return dbValue;
    }
}