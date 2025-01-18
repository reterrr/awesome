# Weather Monitoring Microservices Project

This is a microservices-based application that allows users to add their favorite locations and monitor current weather conditions and forecasts. The application retrieves data from the OpenWeather API and is built using modern technologies such as Java Spring Boot, gRPC, PostgreSQL, and Redis. The architecture ensures scalability and modularity, making it easy to extend with future features such as notifications.

## Features
- **User Management**: Register, log in, and manage users.
- **Location Management**: Add, view, and delete locations for weather tracking.
- **Weather Monitoring**: Fetch real-time weather data and forecasts for user-selected locations.
- **Performance Optimization**: Caching weather data with Redis to reduce API calls and improve speed.
- **Secure Communication**: Validate requests using JWT tokens.

## Technologies Used
- **Backend Framework**: Java Spring Boot
- **Communication Protocol**: gRPC
- **Authentication**: JWT
- **Databases**: PostgreSQL for persistent data storage, Redis for caching
- **Containerization**: Docker

## Microservices Overview

### 1. API Gateway
- Acts as the entry point for all external requests.
- Validates JWT tokens using a shared key with the User Service.
- Routes HTTP 1.1 REST requests to the appropriate microservices.

### 2. User Service
- Manages user registration, login, and deletion.
- Generates JWT tokens for authenticated users.
- Uses PostgreSQL to store user data.

### 3. Location Service
- Manages metadata about supported locations.
- Stores location data in PostgreSQL.

### 4. User-Location Service
- Tracks associations between users and their favorite locations.
- Links user data with location information.

### 5. Weather Service
- Integrates with the OpenWeather API to fetch weather data and forecasts.
- Caches weather data using Redis to optimize API usage and reduce latency.

### 6. Notification Service (Planned)
- Will notify users about significant weather changes or alerts in their tracked locations.

## System Architecture
- **Clients** (e.g., web app, mobile app) interact with the API Gateway via REST.
- The API Gateway validates requests, forwards them to appropriate services, and aggregates responses.
- Microservices communicate internally using gRPC for efficient and fast messaging.
- Data is persisted in PostgreSQL, with Redis used for caching frequently accessed weather data.

## Deployment
The application is containerized using Docker. Each microservice runs in its own container, and services are orchestrated using Docker Compose.

### Prerequisites
- Docker and Docker Compose installed

### Steps
1. Clone the repository:
   ```bash
   git clone https://github.com/reterrr/awesome.git
   cd awesome
   ```
2. Build and start the containers:
   ```bash
   docker-compose up --build
   ```
3. Access the API Gateway at `http://localhost:8000`.

## Future Enhancements
- Add the Notification Service to send weather alerts to users.
- Implement advanced analytics for weather data.
- Extend the system to support more APIs for weather data redundancy.
