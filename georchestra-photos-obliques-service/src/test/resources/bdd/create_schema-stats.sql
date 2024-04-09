CREATE SCHEMA photos_obliques_stats;
GRANT ALL ON SCHEMA photos_obliques_stats TO consult;

CREATE TABLE IF NOT EXISTS photos_obliques_stats.statistiques
(
    id bigserial NOT NULL PRIMARY KEY,
    result varchar(50),
    url varchar,
    query varchar,
    when_ timestamp,
    who varchar(50),
    duration bigint,
    data varchar(100)
)