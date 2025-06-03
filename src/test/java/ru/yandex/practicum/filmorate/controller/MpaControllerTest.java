package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.mapper.MpaMapRower;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaDbStorage;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({MpaDbStorage.class, MpaMapRower.class})
class MpaControllerTest {

    private final MpaDbStorage mpaDbStorage;

    @Test
    void findAllMpa() {
        List<Mpa> allMpa = mpaDbStorage.findAllMpa();

        assertFalse(allMpa.isEmpty(), "Список рейтинга не должен быть пустым");
        assertEquals(5, allMpa.size());
    }

    @Test
    void findMpaById() {
        Mpa mpaById = mpaDbStorage.findMpaById(4);
        Mpa mpaR = new Mpa(4, "R");

        assertEquals(mpaR, mpaById);
    }
}