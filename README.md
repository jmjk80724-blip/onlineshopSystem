# Online Shop System
A Full-stack e-commerce applicaption build with spring boot, PostgreSQL, and Java - university courework project

## Tech Stack
    - Java , Spring Boot
    - PostgreSQL, Hibernate/JPA
    - Maven

## Getting Started
 1. Clon repo 
2.  Copy 'applocaption.properties.example'
3. Run: '..mvnw spring-boot:run'

## Progress Log

### 2026-08-12
- Setup Docker Compose for PostgreSQL database (containerized, with healthcheck)
- Fixed database connection issues (port conflict, database name mismatch)
- Created user package following Package-by-Feature structure
- Implemented Entity: User.java (with Lombok @Data, JPA annotations, unique constraints on username/email)
- Implemented UserRepository (Spring Data JPA interface with custom finder methods)
- Implemented UserNotFoundException (custom exception)
- Discussed and planned UserService (register, getById, getAll, update, delete) and UserController (REST endpoints)
## Author


![CI](https://github.com//jmjk80724-blip/onlineshopSystem/actions/workflows/ci.yml/badge.svg)
