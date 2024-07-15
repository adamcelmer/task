# Magnolia CMS interview task
This repository was created solely to fulfill Magnolia's interview process.

# Build & run

## Quick build
```shell
./build-and-run.sh
```

## App build
The backend is a simple quarkus and gradle based app. \
To build, simply run `./gradlew build` in the `/backend` directory. `gradle` directory is commited to the repository to avoid version/installation issues.

## Docker image
Having the .jar file, we can build and run necessary docker images. \
To do so, run `docker compose up` in the root project directory.

# Postman
Postman collection can be found in the `/postman` directory. It contains a complete set of app endpoints together \
with before/after scripts to simplify authentication and subsequent requests. \
I.e. note_id is stored in a variable after creation, so next `GET /api/notes/{id}` will contain that particular note.

# Cloud deployment
The app is temporarily available at: http://35.216.208.247:8080.
