# Magnolia CMS interview task

This repository was created solely to fulfill MagnolaCMS's interview process.

## Simplifications
Due to a limited time resources and only general specification, measures were taken to simplify 
app security and functionality to avoid getting too deep into the implementation.
Only `dev` profile configuration exists at the moment. No prod profile is provided as particular configuration may vary
and hash to be agreed upfront.

### Database configuration
- Simple user&password authentication is used, which are stored in the code base. On a real production environment, 
all credentials should be provided from an external vault and additional security practices should be used 
i.e. certificate based authentication, ip white listening for clients.
- Default user created in the docker-compose uses a weak password and has very wide range of permissions.

### JWT authentication
- JWT tokens were used instead of a simple basic auth to increase app security. However, signature keys are stored
in the repository and token generation logic is naive. On prod, we could use external identity provider, setup more claims,
tune token expiration time etc.

### Test coverage
Not all corner cases are covered by tests. Existing tests serve as an example and cover only most popular positive scenarios.

## Build & run

### Requirements
- linux/mac based operating system
- Java 17+
- docker
- gradle 8.8+

### Build

Quarkus app uses Gradle as a dependency manager. It can be build using:
```shell
cd backend
./gradlew build
```
`gradle` directory is commited to the repository to avoid Gradle requirement.

### Quick build & run

This script builds the Gradle project, and then runs `docker-compose up -d` command building & running both database and app images.
```shell
./build-and-run.sh
```

## Postman
Postman collection can be found in the `/postman` directory. It contains a complete set of requests app endpoints together
with before/after scripts to simplify authentication and subsequent requests. 
I.e. note_id is stored in a variable after creation, so next `GET /api/notes/{id}` will contain that particular note.
