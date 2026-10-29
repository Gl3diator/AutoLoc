# Changelog

All notable changes to AutoLoc are documented here.

This changelog follows the Keep a Changelog convention:
https://keepachangelog.com/en/1.1.0/

AutoLoc is an educational Spring Boot project.
Development milestones are organized by atelier rather
than formal production releases.

## [Unreleased]

### Planned

- Standardize repository structure.
- Improve project README and setup documentation.
- Introduce repository governance files.

## 2026-10-29 — Atelier 6: GoF Patterns and SOLID

### Added

- SOLID analysis of the legacy AutoLocManager.
- Strategy pattern for reservation pricing.
- Standard, weekend and long-duration pricing strategies.
- TarificationStrategyFactory for automatic strategy selection.
- NotificationFactory for notification channel selection.
- Observer pattern for reservation notifications.
- EmailNotificationObserver and SmsNotificationObserver.
- Reservation confirmation and cancellation events.
- REST endpoint for reservation tariff calculation.
- REST endpoint for reservation cancellation.
- Unit and integration tests for design patterns.
- Instructor demonstration documentation.

### Verified

- Maven test suite: 21 tests passed.
- REST pricing calculations.
- Reservation confirmation and cancellation notifications.
- Cleanup of temporary test data.

## 2026-10-27 — Atelier 5: REST API and Testing

### Added

- REST controllers for AutoLoc business entities.
- HTTP request validation and error handling.
- Swagger / OpenAPI documentation.
- Postman collection and local environment.
- End-to-end API testing scenarios.

### Verified

- Newman: 59 requests executed.
- Newman: 70 assertions passed.
- Zero failed assertions.

## 2026-10-27 — Atelier 4: Service Layer

### Added

- Service interfaces and implementations.
- Spring dependency injection.
- Business validation.
- Service-layer CRUD operations.
- Integration tests for service operations.

### Verified

- Maven suite: 13 tests passed.

## Earlier Development — Persistence and Domain Model

### Added

- Spring Boot project initialization.
- Maven build configuration.
- JPA entity mappings and relationships.
- Spring Data JPA repositories.
- MariaDB persistence configuration.
- Repository CRUD tests.

## References

Project repository:
https://github.com/Gl3diator/AutoLoc

For implementation details, see the documentation
under docs/ and the Git commit history.
