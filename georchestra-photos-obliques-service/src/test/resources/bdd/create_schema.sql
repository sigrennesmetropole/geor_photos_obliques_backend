CREATE SCHEMA if not exists phototheque;
GRANT ALL ON SCHEMA phototheque TO consult;

CREATE ALIAS if not exists public.st_intersects AS $$
boolean st_intersects(org.locationtech.jts.geom.Geometry geom1, org.locationtech.jts.geom.Geometry geom2) throws java.sql.SQLException { try {return org.h2gis.functions.spatial.predicates.ST_Intersects.isIntersects(geom1, geom2);} catch (java.sql.SQLException ignored) {}; return false;}
$$;

CREATE ALIAS if not exists public.st_setSRID AS $$
org.locationtech.jts.geom.Geometry st_setSRID(org.locationtech.jts.geom.Geometry geom1, Integer srid) throws java.lang.IllegalArgumentException { return org.h2gis.functions.spatial.crs.ST_SetSRID.setSRID(geom1, srid);}
$$;


CREATE ALIAS if not exists public.st_geomfromtext AS $$
org.locationtech.jts.geom.Geometry st_geomfromtext(String wkt, int srid) throws java.sql.SQLException { return org.h2gis.functions.spatial.convert.ST_GeomFromText.toGeometry(wkt, srid);}
$$;


CREATE ALIAS if not exists public.st_area AS $$
Double st_area(org.locationtech.jts.geom.Geometry geometry) { return org.h2gis.functions.spatial.properties.ST_Area.getArea(geometry);}
$$;

CREATE ALIAS if not exists public.st_intersection AS $$
org.locationtech.jts.geom.Geometry st_intersection(org.locationtech.jts.geom.Geometry geometry1, org.locationtech.jts.geom.Geometry geometry2) throws java.sql.SQLException { return org.h2gis.functions.spatial.operators.ST_Intersection.intersection(geometry1, geometry2);}
$$;


CREATE TABLE if not exists phototheque.v_photooblique_emprise
(
    id             varchar(30) not null,
    fichier        varchar(50),
    annee          int4,
    date_           timestamp,
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

alter table phototheque.v_photooblique_emprise
    drop constraint if exists UK_a0qmi8htvty1idedoo8dlxf99;

alter table phototheque.v_photooblique_emprise
    add constraint UK_a0qmi8htvty1idedoo8dlxf99 unique (id);