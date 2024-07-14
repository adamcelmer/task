#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
  CREATE USER "magnolia" WITH ENCRYPTED PASSWORD 'password';
	CREATE DATABASE "postit";
  GRANT ALL PRIVILEGES ON DATABASE "postit" TO "magnolia";
  \c "postit" postgres
  GRANT USAGE ON SCHEMA public TO "magnolia";
  GRANT ALL ON SCHEMA public TO "magnolia";
EOSQL