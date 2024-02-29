-- create user search if not exists;
DO
$do$
    BEGIN
        IF EXISTS (
            SELECT FROM pg_catalog.pg_roles
            WHERE  rolname = 'photos_obliques') THEN

            RAISE NOTICE 'Role "photos_obliques" already exists. Skipping.';
        ELSE
            BEGIN   -- nested block
            RAISE NOTICE 'Role "photos_obliques" does not exist. Creating it.';
            CREATE USER photos_obliques WITH
                LOGIN
                NOSUPERUSER
                INHERIT
                NOCREATEDB
                NOCREATEROLE
                NOREPLICATION;
            EXCEPTION
                WHEN duplicate_object THEN
                    RAISE NOTICE 'Role "photos_obliques" was just created by a concurrent transaction. Skipping.';
            END;
        END IF;
    END
$do$;

CREATE SCHEMA photos_obliques AUTHORIZATION photos_obliques;
GRANT ALL ON SCHEMA photos_obliques TO photos_obliques;
ALTER USER photos_obliques SET search_path TO photos_obliques, public;

CREATE EXTENSION IF NOT EXISTS postgis;-- SCHEMA gaspar_geo;
CREATE EXTENSION IF NOT EXISTS fuzzystrmatch;-- SCHEMA gaspar_geo;
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";-- SCHEMA gaspar_geo;
CREATE EXTENSION IF NOT EXISTS postgis_topology;-- SCHEMA gaspar_geo;
CREATE EXTENSION IF NOT EXISTS postgis_tiger_geocoder;-- SCHEMA gaspar_geo;

-- Gestion des accents niveau BDD
CREATE extension IF NOT EXISTS unaccent;
