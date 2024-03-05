create table if not exists photo_oblique
(
    id             varchar(30) not null,
    objectId       int         not null,
    fichier        varchar(50),
    annee          int4,
    date           date,
    comment        varchar(1000),
    presta         varchar(220),
    proprio        varchar(220),
    telecharg      boolean,
    mention        varchar(500),
    commune        varchar(500),
    shape          public.geometry,
    taille_fichier int4,
    primary key (id)
);

alter table photo_oblique
    drop constraint if exists UK_a0qmi8htvty1idedoo8dlxf99;

alter table photo_oblique
    add constraint UK_a0qmi8htvty1idedoo8dlxf99 unique (id);
