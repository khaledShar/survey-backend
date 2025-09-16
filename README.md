# survey-backend

This is the backend service for the Team Survey application.  
It provides a REST API for retrieving surveys, submitting anonymous responses, and fetching aggregated results.

## Tech Stack
- Java 17
- Spring Boot 3
- Spring Data JPA + Hibernate
- PostgreSQL
- Flyway (database migrations)
- Maven

---

## Prerequisites
- [Java 17+](https://adoptium.net/)
- [Maven 3.8+](https://maven.apache.org/)
- [PostgreSQL 15+](https://www.postgresql.org/)

Make sure PostgreSQL is installed and running locally.

### API Endpoints

GET /api/surveys/{id}
Fetch survey details (title, questions).

POST /api/surveys/{id}/responses
Submit responses (Likert scale 1–5).
Example payload:
{
  "surveyId": 1,
  "answers": [
    { "questionId": 1, "score": 4 },
    { "questionId": 2, "score": 5 }
  ]
}
GET /api/surveys/{id}/results
Fetch aggregated results (average + distribution per question).
---

## Setup Instructions

### 1. Database
Create a local database user and database:
```bash
psql -U postgres -c "CREATE USER admin WITH PASSWORD 'admin';"
psql -U postgres -c "CREATE DATABASE survey_db OWNER admin;"
```
### 2. Application configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/surveydb
spring.datasource.username=survey_user
spring.datasource.password=survey_pass

spring.jpa.hibernate.ddl-auto=validate
spring.flyway.enabled=true

### 3. Run the application
```bash
mvn spring-boot:run
```
The backend will start at http://localhost:8080