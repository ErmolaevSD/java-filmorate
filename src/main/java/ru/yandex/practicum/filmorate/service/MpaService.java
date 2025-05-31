package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaDbStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MpaService {

    private final MpaDbStorage mpaDbStorage;

    public List<Mpa> findAllMpa() {
        return mpaDbStorage.findAllMpa();
    }

    public Mpa findMpaById(Integer id) {
        return mpaDbStorage.findMpaById(id);
    }
}