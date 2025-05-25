CREATE TABLE IF NOT EXISTS genres (
    genre_id integer NOT NULL PRIMARY KEY,
    description varchar(255)
);

CREATE TABLE IF NOT EXISTS mpa (
    mpa_id integer PRIMARY KEY,
    description varchar(255)
);

CREATE TABLE IF NOT EXISTS films (
    id integer GENERATED ALWAYS as identity PRIMARY KEY,
    name varchar(40) NOT NULL,
    description varchar(255),
    releaseDate DATE NOT NULL,
    duration integer NOT NULL,
    mpa_id integer NOT NULL REFERENCES mpa(mpa_id)
);

CREATE TABLE IF NOT EXISTS users (
    id integer GENERATED ALWAYS AS identity PRIMARY KEY,
    email varchar(255) NOT NULL,
    login varchar(40) NOT NULL,
    name varchar(40) NOT NULL,
    birthdate DATE NOT NULL
);

CREATE TABLE IF NOT EXISTS film_genre (
    id integer GENERATED ALWAYS as identity PRIMARY KEY,
    film_id integer NOT NULL REFERENCES films (id),
    genre_id integer NOT NULL REFERENCES genres (genre_id)
);

CREATE TABLE IF NOT EXISTS filmLike (
    film_id integer NOT NULL REFERENCES films (id),
    user_id integer NOT NULL REFERENCES users (id),
    PRIMARY KEY (film_id, user_id)
);

CREATE TABLE IF NOT EXISTS friends (
    id integer GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id integer NOT NULL REFERENCES users (id),
    friend_id integer NOT NULL REFERENCES users (id),
    status varchar(40) NOT NULL,
    UNIQUE (user_id, friend_id)
);
