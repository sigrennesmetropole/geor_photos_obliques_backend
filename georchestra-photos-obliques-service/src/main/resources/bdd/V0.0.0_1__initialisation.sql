create table if not exists photo_oblique
(
    id             varchar(30) not null,
    fichier        varchar(50),
    annee          int4,
    date           timestamp,
    heure          varchar(8),
    comment        varchar(1000),
    angle_deg      numeric(18, 3),
    angle_grd      numeric(18, 3),
    id_archiv      varchar(50),
    presta         varchar(220),
    proprio        varchar(220),
    telecharg      int2,
    mention        varchar(500),
    commune        varchar(500),
    shape          public.geometry,
    objectId       int         not null,
    format         varchar(15),
    taille_fichier int4,
    type           varchar(150),
    primary key (id)
);

alter table photo_oblique
    drop constraint if exists UK_a0qmi8htvty1idedoo8dlxf99;

alter table photo_oblique
    add constraint UK_a0qmi8htvty1idedoo8dlxf99 unique (id);
