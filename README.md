Release Tracker API

A Spring Boot application to track software releases.

✅ Features
- Manage releases (Create, Read, Update, Delete)
- Filter releases by:
  - Status
  - Name 
  - Release Date 
  - Integrated with MySQL for persistence 
  - Runs locally or via Docker Compose 
  - Includes JUnit + MockMvc tests for controllers and services


Tech Stack:
- Java 17
- Spring Boot 
- Spring Data JPA 
- MySQL 
- Docker 
- Docker Compose 
- JUnit 
- MockMvc 

✅ Setup Instructions
1. Clone the Repository  
```
git clone <your-repo-url> 
cd release_tracker
```
2. Run Locally (with Local MySQL)
   1. Start MySQL locally
   ```
   docker run --name mysql_db -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=release_tracker_db -p 3307:3306 -d mysql:8.0
   ```
   2. Run app
   ```
   mvn spring-boot:run -Dspring-boot.run.profiles=local
   ```
3. Run with Docker Compose 
   1. Build the JAR:
   ```
    mvn clean package -DskipTests 
   ```
   2. Start services:
   ```
    docker-compose up --build -d
   ```
   This will run:
- MySQL (Port 3307 on your host)
- Spring Boot App (Port 8080)


API Endpoints

| Method     | Endpoint                           | Description            |
| ---------- | ---------------------------------- | ---------------------- |
| **POST**   | `/releases`                        | Create new release     |
| **GET**    | `/releases`                        | Get all releases       |
| **GET**    | `/releases?status=CREATED`         | Filter by status       |
| **GET**    | `/releases?name=Release%201`       | Filter by name         |
| **GET**    | `/releases?releaseDate=2025-07-10` | Filter by release date |
| **GET**    | `/releases/{id}`                   | Get release by ID      |
| **PUT**    | `/releases/{id}`                   | Update release         |
| **DELETE** | `/releases/{id}`                   | Delete release         |
