Template repository for Filmorate project.
![Wee (3)](https://github.com/user-attachments/assets/8f7acf1b-a55d-4388-8cee-9b1cd1b73b25)
Примеры SQL запросов:

1. Получение фильма по id
   SELECT * FROM FILMS WHERE id = ?;
2. Показ всех фильмов
   SELECT * FROM FILMS;
3. Показ топ фильмов
   SELECT films.name, COUNT(user_id) as countLike
   FROM films
   JOIN filmLike on film.id = filmLike.film_id
   GROUP BY film_id
   ORDER BY countLike DESC
   LIMIT ?;
4. Создание фильма
   INSERT INTO films (name, description, duration, releaseDate, duration, mpa_id) VALUES (?,?,?,?,?)
5. Обновление фильма
   UPDATE films SET name = '...', ...;
7. Добавление лайка
   INSERT INTO filmLike (film_id, user_id) VALUES (?,?);
8. Удаление лайка
   DELETE FROM filmLike WHERE film_id = ? and user_id = ?;

   С пользователями почти все аналогично.
