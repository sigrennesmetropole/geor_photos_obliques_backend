CREATE TABLE IF NOT EXISTS statistiques_photos_obliques
(
    id bigserial NOT NULL PRIMARY KEY,
    result varchar(50),
    url varchar,
    query varchar,
    when_ timestamp,
    who varchar(50),
    duration bigint,
    data varchar(100)
);
