-- create user photos_obliques if not exists;
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
                NOREPLICATION
                PASSWORD 'photos_obliques';
            EXCEPTION
                WHEN duplicate_object THEN
                    RAISE NOTICE 'Role "photos_obliques" was just created by a concurrent transaction. Skipping.';
            END;
        END IF;
    END
$do$;

DO
$do$
    BEGIN
        IF EXISTS (
                SELECT FROM pg_catalog.pg_roles
                WHERE  rolname = 'consult') THEN

            RAISE NOTICE 'Role "consult" already exists. Skipping.';
        ELSE
            BEGIN   -- nested block
            RAISE NOTICE 'Role "consult" does not exist. Creating it.';
            CREATE USER consult WITH
                LOGIN
                NOSUPERUSER
                INHERIT
                NOCREATEDB
                NOCREATEROLE
                NOREPLICATION
                PASSWORD 'consult';
            EXCEPTION
                WHEN duplicate_object THEN
                    RAISE NOTICE 'Role "consult" was just created by a concurrent transaction. Skipping.';
            END;
        END IF;
    END
$do$;

CREATE SCHEMA phototheque AUTHORIZATION photos_obliques;
CREATE EXTENSION IF NOT EXISTS postgis;
CREATE EXTENSION IF NOT EXISTS fuzzystrmatch;
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS postgis_topology;
CREATE EXTENSION IF NOT EXISTS postgis_tiger_geocoder;
CREATE EXTENSION IF NOT EXISTS unaccent;

GRANT ALL ON SCHEMA phototheque TO photos_obliques;

GRANT USAGE ON SCHEMA phototheque TO consult;
ALTER USER consult SET search_path TO photos_obliques, public;

