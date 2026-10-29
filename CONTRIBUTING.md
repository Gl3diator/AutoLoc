# Contributing to AutoLoc

Thank you for your interest in contributing to AutoLoc!

AutoLoc is an educational vehicle rental management application
built with Java, Spring Boot, Spring Data JPA, Maven and MariaDB.

## Getting Started

1. Fork the repository on GitHub.
2. Clone your fork.
3. Create a branch for your changes.
4. Implement and test your changes.
5. Submit a pull request.

Repository:
https://github.com/Gl3diator/AutoLoc

## Development Requirements

- Java 17
- Maven
- MariaDB
- Git

## Local Setup

Clone the repository and enter the project directory.

Configure your local database using the template at
src/main/resources/application.example.properties.

Never commit credentials, secrets or local database passwords.

Start the application from the directory containing pom.xml:

    ./mvnw spring-boot:run

Run tests:

    ./mvnw clean test

## Code Guidelines

- Follow established Java naming conventions.
- Respect the Controller -> Service -> Repository architecture.
- Keep business logic inside the service layer.
- Use constructor-based dependency injection.
- Apply SOLID principles where appropriate.
- Write tests for new or modified functionality.
- Avoid unrelated changes in the same pull request.
- Update documentation when behavior changes.

## Git Workflow

Create a feature branch:

    git switch -c feature/short-description

Use descriptive commit messages.

For coursework:

    Atelier 6: implement Strategy pattern
    Atelier 6: add reservation tests

For general development:

    feat: add reservation filtering
    fix: correct rental price calculation
    docs: improve installation instructions

## Pull Requests

Before opening a pull request:

1. Ensure your branch is up to date.
2. Verify that Maven tests pass.
3. Review your changes for credentials or private data.
4. Describe what changed and why.
5. Include relevant testing results.
6. Reference related issues when applicable.

Submit pull requests against the main branch.

## Reporting Issues

Report bugs at:
https://github.com/Gl3diator/AutoLoc/issues

Include the steps to reproduce, expected behavior,
actual behavior, and relevant sanitized logs.

For security vulnerabilities, follow SECURITY.md
instead of opening a public issue.

## Code of Conduct

All contributors are expected to follow
CODE_OF_CONDUCT.md.

## License

Contributions are distributed under GNU GPL-3.0.
See LICENSE for details.
