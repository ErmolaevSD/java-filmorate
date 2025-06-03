package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class User {

    private Long id;

    @Email(message = "Введите корректную электронную почту")
    private String email;

    @NotBlank(message = "Логин не может быть пустым")
    @Pattern(regexp = "\\S+",message = "Логин не может содержать пробелы")
    private String login;

    private String name;

    @Past(message = "Дата рождения не может быть в будущем времени")
    private LocalDate birthday;

    private final Set<Long> userFriends = new HashSet<>();
}
