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
    shape          geometry,
    taille_fichier int4,
    type           varchar(150),
    primary key (id)
);

alter table photo_oblique drop constraint if exists UK_a0qmi8htvty1idedoo8dlxf99;

alter table photo_oblique
    add constraint UK_a0qmi8htvty1idedoo8dlxf99 unique (id);


alter table photo_oblique drop column if exists objectId;
alter table photo_oblique drop column if exists format;

create view phototheque.v_photooblique_emprise
            (id, fichier, annee, date_, heure, comment, angle_deg, angle_grd, id_archiv, presta, proprio, telecharg,
             mention, commune, type, shape, taille_fichier)
as
SELECT photooblique_emprise.id,
       photooblique_emprise.fichier,
       photooblique_emprise.annee,
       photooblique_emprise.date_,
       photooblique_emprise.heure,
       photooblique_emprise.comment,
       photooblique_emprise.angle_deg,
       photooblique_emprise.angle_grd,
       photooblique_emprise.id_archiv,
       photooblique_emprise.presta,
       photooblique_emprise.proprio,
       photooblique_emprise.telecharg,
       photooblique_emprise.mention,
       photooblique_emprise.commune,
       photooblique_emprise.type,
       photooblique_emprise.shape AS shape,
       photooblique_emprise.taille_fichier
FROM phototheque.photooblique_emprise;

-- Permet d'accorder les droits de lecture sur la vue à l'utilisateur renseigné dans les properties
-- GRANT SELECT ON TABLE phototheque.v_photooblique_emprise TO <rôle_declaré_dans_fichier_properties. Exemple : consult> ;
