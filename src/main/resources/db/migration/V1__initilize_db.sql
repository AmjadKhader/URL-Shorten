CREATE TABLE IF NOT EXISTS urls (
    id           SERIAL PRIMARY KEY,
    long_url     TEXT NOT NULL,
    short_url    TEXT NOT NULL
);