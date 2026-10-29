<div align="center">

<img src="./res/img/autoloc-logo.png" alt="AutoLoc logo — vehicle and location pin" width="260">

# AutoLoc

### Vehicle rental management REST API built with Spring Boot

![Java](https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.1.1-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Maven](https://img.shields.io/badge/Apache_Maven-Build-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)
![MariaDB](https://img.shields.io/badge/MariaDB-Database-003545?style=for-the-badge&logo=mariadb&logoColor=white)
![REST API](https://img.shields.io/badge/REST-API-2563EB?style=for-the-badge)
![License](https://img.shields.io/badge/License-GPL--3.0-008080?style=for-the-badge)

![Last commit](https://img.shields.io/github/last-commit/Gl3diator/AutoLoc?style=flat-square&logo=github)
![Repository size](https://img.shields.io/github/repo-size/Gl3diator/AutoLoc?style=flat-square&logo=github)
![Stars](https://img.shields.io/github/stars/Gl3diator/AutoLoc?style=flat-square&logo=github)

**Java 17 · Spring Boot 4.1.1 · Spring Data JPA · MariaDB · Maven · OpenAPI**

[Getting Started](#getting-started) · [API](#rest-api) · [Architecture](#architecture) · [Testing](#testing) · [Documentation](#documentation)

</div>

---

## About

**AutoLoc** is an educational backend application for managing vehicle rental operations. It models rental agencies, employees, vehicles, equipment, clients, reservations, contracts, payments and maintenance records.

The project is built as a layered Java REST API and demonstrates Spring dependency injection, JPA persistence, business validation, automated tests, and the **Strategy**, **Factory** and **Observer** design patterns.

> [!NOTE]
> AutoLoc is a learning project, not a production-hardened rental platform. Email and SMS notifications are **simulated through application logs**; they are not delivered to real recipients.

## Features

- REST endpoints for agencies, clients, vehicles, employees, equipment, reservations, contracts and maintenance.
- Read-only payment endpoints; payments are managed through their associated contracts.
- Business validation and HTTP error handling.
- Reservation tariff calculation with selectable pricing strategies.
- Reservation confirmation and cancellation notifications through observers.
- Swagger UI / OpenAPI documentation.
- Postman collection and automated Maven tests.

## Architecture

```text
HTTP clients / Swagger / Postman
             |
             v
    Spring MVC Controllers
             |
             v
        Services
             |
        +----+------------------------+
        |                             |
        v                             v
   Repositories                Business patterns
        |                   Strategy / Factory / Observer
        v
  Spring Data JPA
        |
        v
      MariaDB
```

**Patterns implemented in Atelier 6:**

| Pattern | Purpose | Implementation |
|---|---|---|
| Strategy | Calculate reservation prices | `TarifStandardStrategy`, `TarifWeekendStrategy`, `TarifLongueDureeStrategy` |
| Factory | Select pricing and notification implementations | `TarificationStrategyFactory`, `NotificationFactory` |
| Observer | React to reservation confirmation/cancellation | `ReservationSubject`, `EmailNotificationObserver`, `SmsNotificationObserver` |

Pricing selection in the current implementation: reservations of **7 days or more** use the long-duration rate (20% discount); otherwise reservations beginning on Saturday/Sunday use the weekend rate (10% discount); remaining reservations use the standard rate.

## Repository Layout

```text
AutoLoc/
├── AUTHORS
├── CHANGELOG.md
├── CODE_OF_CONDUCT.md
├── CONTRIBUTING.md
├── LICENSE
├── README.md
├── SECURITY.md
├── project/
│   ├── .mvn/
│   ├── mvnw
│   ├── mvnw.cmd
│   ├── pom.xml
│   ├── postman/
│   │   ├── AutoLoc.postman_collection.json
│   │   └── AutoLoc-local.postman_environment.json
│   └── src/
│       ├── main/
│       └── test/
└── res/
    ├── doc/
    │   ├── api-notes.md
    │   ├── atelier6-gof-solid.md
    │   ├── repository-notes.md
    │   └── service-notes.md
    └── img/
        └── autoloc-logo.png
```

## Getting Started

### Requirements

- **JDK 17**
- **Maven** (or the included Maven wrapper)
- **MariaDB** running locally or reachable over the network
- **Git**

### 1. Clone the repository

```bash
git clone https://github.com/Gl3diator/AutoLoc.git
cd AutoLoc
```

### 2. Prepare the database

The example configuration expects a MariaDB database called `autoloc_db` and a database user called `autoloc`. For a local development instance, create them using a MariaDB administrator account:

```sql
CREATE DATABASE IF NOT EXISTS autoloc_db;
CREATE USER IF NOT EXISTS 'autoloc'@'localhost' IDENTIFIED BY 'choose-a-local-password';
GRANT ALL PRIVILEGES ON autoloc_db.* TO 'autoloc'@'localhost';
```

Use your own password and appropriate permissions for your environment. If the database and user already exist, keep your existing configuration.

### 3. Configure the application

```bash
cp project/src/main/resources/application.example.properties \
   project/src/main/resources/application.properties

export AUTOLOC_DB_PASSWORD='your-local-database-password'
```

The template uses `jdbc:mariadb://localhost:3306/autoloc_db` and a default HTTP context path of `/api`. Update your local `application.properties` if your database differs. The local file is **Git-ignored** and must not be committed.

### 4. Run AutoLoc

```bash
cd project
mvn spring-boot:run
```

Alternatively, use `./mvnw spring-boot:run` if your Maven wrapper is available and executable. The commands in the following sections are run from `project/` unless otherwise noted.

Once running, explore the API at:

- **Swagger UI:** http://localhost:8080/api/swagger-ui.html
- **OpenAPI JSON:** http://localhost:8080/api/v3/api-docs

## REST API

The application uses `/api` as its servlet context path, so endpoint URLs below include that prefix.

| Resource | Base endpoint | Operations |
|---|---|---|
| Agencies | `/api/agences` | GET, POST, PUT, DELETE |
| Clients | `/api/clients` | GET, POST, PUT, DELETE |
| Vehicles | `/api/vehicules` | GET, POST, PUT, DELETE |
| Employees | `/api/employes` | GET, POST, PUT, DELETE |
| Equipment | `/api/equipements` | GET, POST, PUT, DELETE |
| Reservations | `/api/reservations` | GET, POST, PUT, DELETE |
| Contracts | `/api/contrats` | GET, POST, PUT, DELETE |
| Maintenance | `/api/maintenances` | GET, POST, PUT, DELETE |
| Payments | `/api/paiements` | GET only |

Individual resource requests use `/{id}`. Two additional reservation endpoints are available:

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/reservations/{id}/tarif` | Calculate the reservation price |
| `PATCH` | `/api/reservations/{id}/annuler` | Cancel a reservation |

Example read-only requests while the server is running:

```bash
curl -i http://localhost:8080/api/vehicules
curl -i http://localhost:8080/api/reservations
curl -i http://localhost:8080/api/v3/api-docs
```

See [API notes](./res/doc/api-notes.md) for the full controller matrix and expected HTTP responses.

## Testing

Run the complete test suite from `project/`:

```bash
mvn clean test
```

Run only the design-pattern tests:

```bash
mvn -Dtest=Atelier6PatternsTest test
mvn -Dtest=ReservationServiceTest test
```

**Last locally verified on 29 October 2026:** 21 tests executed, 0 failures, 0 errors, 0 skipped; `BUILD SUCCESS`. This is a recorded local result, **not** a live CI status badge.

### Postman / Newman

The collection and local environment are located in [`project/postman/`](./project/postman/). From the repository root, with the API running:

```bash
newman run project/postman/AutoLoc.postman_collection.json \
  -e project/postman/AutoLoc-local.postman_environment.json
```

On 27 October 2026, the collection completed **59 requests and 70 assertions with zero failures**. E2E scenarios may create and remove development data; use a dedicated test database when appropriate.

## Documentation

| Document | Contents |
|---|---|
| [Repository / JPA](./res/doc/repository-notes.md) | Persistence and repository exercises |
| [Service Layer](./res/doc/service-notes.md) | Service architecture and validation |
| [REST API, Postman & Swagger](./res/doc/api-notes.md) | Endpoints and HTTP behavior |
| [SOLID & GoF Patterns](./res/doc/atelier6-gof-solid.md) | Strategy, Factory, Observer, tests and lab demonstration |
| [Changelog](./CHANGELOG.md) | Development milestones |

## Contributing & Security

Contributions are welcome. Please read [CONTRIBUTING.md](./CONTRIBUTING.md), [CODE_OF_CONDUCT.md](./CODE_OF_CONDUCT.md) and [SECURITY.md](./SECURITY.md) before submitting changes or reporting vulnerabilities.

Do not commit application secrets or database credentials. The project has not undergone a production security audit.

## License

AutoLoc is distributed under the [GNU General Public License v3.0](./LICENSE). See [AUTHORS](./AUTHORS) for maintainer information.

---

<div align="center">

**AutoLoc — Vehicle rental management with Spring Boot**

[Repository](https://github.com/Gl3diator/AutoLoc) · [Issues](https://github.com/Gl3diator/AutoLoc/issues)

</div>
