FROM postgres:15.1

COPY ./init.sh /docker-entrypoint-initdb.d/

ENV POSTGRES_PASSWORD=password
EXPOSE 5432
