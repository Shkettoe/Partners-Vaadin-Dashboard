# Partners Management System

A partner and contact management application built with Vaadin 24 and Spring Boot.

## Quick Start

```bash
./mvnw spring-boot:run
```

Open http://localhost:8080

## Tech Stack

- Java 21
- Spring Boot 3.5.7
- Vaadin 24.9.5
- SQLite
- Google libphonenumber

## Features

- Partners CRUD
- Contacts CRUD
- Bulk operations (edit/delete multiple partners)
- International phone number validation
- Auto-generated sample data on first run

## Project Structure

```
src/main/java/com/example/base/
├── data/           # Models, repositories, services
├── ui/             # Views and components
└── Application.java
```

## Development

**Run**: `./mvnw` or `./mvnw spring-boot:run`

**Build**: `./mvnw -Pproduction package`

**Test**: `./mvnw test`

**Database**: `partners.db` (SQLite, auto-created)

## Configuration

Edit `src/main/resources/application.properties`:

```properties
server.port=8080
spring.datasource.url=jdbc:sqlite:partners.db
```

## Troubleshooting

**Port in use:**

```bash
pkill -f "java.*partners"
```

**Reset database:**

```bash
rm partners.db && ./mvnw spring-boot:run
```

## Resources

- [Vaadin Docs](https://vaadin.com/docs/latest)
- [Spring Boot Docs](https://docs.spring.io/spring-boot/)
