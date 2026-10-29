# AutoLoc — Security Policy

## Project Status

AutoLoc is an educational vehicle rental management
application built with Spring Boot and MariaDB.

The project is intended for learning and development.
It has not undergone a formal production security audit.

## Supported Versions

The latest version on the main branch is the primary
development target.

Older versions are not independently maintained.

## Reporting a Vulnerability

Please report security vulnerabilities privately.

Contact:
ghaithbensalah@proton.me

Include:

- A description of the vulnerability.
- Steps to reproduce the issue.
- Affected components or API endpoints.
- Potential security impact.
- Relevant sanitized logs or proof of concept.

Do not disclose exploitable vulnerabilities or
sensitive information in public GitHub issues.

## Security Guidelines

### Database Security

- Never commit database credentials.
- Use environment variables for sensitive configuration.
- Avoid exposing MariaDB directly to the internet.
- Apply least-privilege database permissions.

### Input Validation

- Validate incoming REST requests.
- Reject invalid dates, amounts and identifiers.
- Handle exceptions without exposing sensitive details.

### Authentication and Authorization

Authentication and authorization controls must be
reviewed before public or production deployment.

### Dependency Management

- Review Java and Spring Boot dependency advisories.
- Keep dependencies updated.
- Test changes before upgrading production systems.

### Sensitive Data

- Never commit passwords, tokens or private keys.
- Avoid logging sensitive application information.
- Keep local configuration files outside version control.

## Responsible Disclosure

Please allow reasonable time for investigation
and remediation before publicly disclosing a vulnerability.

No guaranteed security response or patching timeframe
is currently offered.

## References

OWASP:
https://owasp.org/

Spring Security:
https://spring.io/projects/spring-security
