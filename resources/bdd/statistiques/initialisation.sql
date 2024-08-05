CREATE TABLE IF NOT EXISTS backend_stats.statistiques_photos_obliques
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


GRANT ALL ON SEQUENCE backend_stats.statistiques_id_seq TO stats;
GRANT ALL ON TABLE backend_stats.statistiques_photos_obliques TO stats;
