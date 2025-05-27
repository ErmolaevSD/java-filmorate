package ru.yandex.practicum.filmorate;

import java.util.Arrays;

public enum MpaRating {
    G(1, "G"),
    PG(2, "PG"),
    PG_13(3, "PG-13"),
    R(4, "R"),
    NC_17(5, "NC-17");

    private final int id;
    private final String name;

    MpaRating(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getMpaId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static MpaRating fromId(int id) {
        return Arrays.stream(values())
                .filter(rating -> rating.id == id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown MPA rating ID: " + id));
    }
}