DO
$do$
    BEGIN
        IF EXISTS (
                SELECT FROM pg_catalog.pg_roles
                WHERE  rolname = 'stats') THEN

            RAISE NOTICE 'Role "stats" already exists. Skipping.';
        ELSE
            BEGIN   -- nested block
            RAISE NOTICE 'Role "stats" does not exist. Creating it.';
            CREATE USER stats WITH
                LOGIN
                NOSUPERUSER
                INHERIT
                NOCREATEDB
                NOCREATEROLE
                NOREPLICATION
                PASSWORD 'stats';
            EXCEPTION
                WHEN duplicate_object THEN
                    RAISE NOTICE 'Role "stats" was just created by a concurrent transaction. Skipping.';
            END;
        END IF;
    END
$do$;

CREATE SCHEMA photos_obliques_stats AUTHORIZATION georchestra;
GRANT ALL ON SCHEMA photos_obliques_stats TO stats;
ALTER USER stats SET search_path TO photos_obliques_stats, public;
