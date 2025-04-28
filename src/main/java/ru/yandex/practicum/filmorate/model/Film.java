package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@Data
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Film {

    private Long id;
    @NotBlank(message = "Название фильма не может быть пустым")
    @NotNull(message = "Название фильма не может быть пустым")
    private String name;
    @Size(max = 200, message = "Описание не может быть больше чем 200 символов")
    private String description;

    @Past(message = "Дата релиза не может быть в будущем времени")
    private LocalDate releaseDate;
    @Positive(message = "Продолжительность не может быть отрицательным числом")
    private Integer duration;

    private final Set<Long> likeList = new HashSet<>();
}

