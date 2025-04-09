package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;


@Data
@AllArgsConstructor
public class Film {

    private Long id;
    @NotBlank(message = "Название фильма не может быть пустым")
    private String name;
    @Size(max = 200, message = "Описание не может быть больше чем 200 символов")
    private String description;

    private LocalDate releaseDate;
    @Positive(message = "Продолжительность не может быть отрицательным числом")
    private Integer duration;

}

