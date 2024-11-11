# Url Shortener Service

Url Shortener Service is a RESTful service for creating short URLs and retrieving the original URLs from the short ones.

## Tech Stack

- **Backend**: Java, Spring Boot
- **Database**: PostgreSQL, Redis
- **Build & Deployment**: Docker, Gradle
- **ORM**: Hibernate, Spring Data JPA
- **Testing**: JUnit, Mockito
- **Documentation**: Swagger
- **Code Coverage**: JaCoCo

## Features

- Generate short URLs for any given URL
- Retrieve original URLs from short URLs
- Persistent storage using PostgreSQL
- High-performance caching with Redis
- Dockerized for easy deployment

## Setup

1. Clone the repository.
2. Run `./gradlew build` to build the project.
3. Start the application with Docker by running `docker-compose up`.

## API Documentation

Swagger is available at `http://localhost:8080/swagger-ui/index.html` once the application is running.

---

### License

This project is licensed under the MIT License.