# 🚕 RideShare Backend API

A Spring Boot-based ride-sharing backend application with JWT authentication, MongoDB integration, and role-based access control.

## 📋 Table of Contents

- [Features](#features)
- [Technologies Used](#technologies-used)
- [Prerequisites](#prerequisites)
- [Project Structure](#project-structure)
- [Setup Instructions](#setup-instructions)
- [Running the Application](#running-the-application)
- [API Endpoints](#api-endpoints)
- [Testing](#testing)
- [Configuration](#configuration)

## ✨ Features

- **User Authentication**
  - User registration with role assignment (USER/DRIVER)
  - JWT-based login system
  - BCrypt password encryption

- **Ride Management**
  - Users can request rides
  - Drivers can view and accept pending ride requests
  - Users and drivers can complete rides
  - Users can view their ride history

- **Security**
  - JWT token authentication
  - Role-based authorization (ROLE_USER, ROLE_DRIVER)
  - Secure password storage with BCrypt

- **Validation & Error Handling**
  - Input validation using Jakarta Bean Validation
  - Global exception handling with standardized error responses

## 🛠 Technologies Used

- **Framework**: Spring Boot 4.0.0
- **Database**: MongoDB
- **Security**: Spring Security + JWT (JJWT 0.12.5)
- **Validation**: Jakarta Bean Validation
- **Build Tool**: Maven
- **Java Version**: 17

## 📦 Prerequisites

Before running this application, ensure you have the following installed:

1. **Java 17** or higher
   ```bash
   java -version
   ```

2. **Maven** (or use Maven Wrapper included in project)
   ```bash
   mvn -version
   ```

3. **MongoDB** (running on localhost:27017)
   - Install MongoDB: https://www.mongodb.com/try/download/community
   - Start MongoDB service:
     ```bash
     # macOS (Homebrew)
     brew services start mongodb-community
     
     # Or manually
     mongod
     ```

## 📁 Project Structure

```
src/
├── main/
│   ├── java/com/arnav/uber/
│   │   ├── config/          # Security, JWT, CORS configuration
│   │   ├── controller/       # REST API endpoints
│   │   ├── dto/              # Data Transfer Objects
│   │   ├── exception/        # Custom exceptions & global handler
│   │   ├── model/            # Entity classes (User, Ride)
│   │   ├── repository/       # MongoDB repositories
│   │   ├── service/          # Business logic
│   │   └── UberApplication.java
│   └── resources/
│       ├── application.yaml  # Application configuration
│       └── static/
│           └── test.html     # Interactive API testing page
└── test/                     # Test files
```

## 🚀 Setup Instructions

1. **Clone or download the project**
   ```bash
   cd /path/to/uber
   ```

2. **Ensure MongoDB is running**
   ```bash
   # Check MongoDB status
   mongosh --eval "db.adminCommand('ping')"
   ```

3. **Configure application (optional)**
   - Edit `src/main/resources/application.yaml` if needed
   - Default MongoDB URI: `mongodb://localhost:27017/rideshare`
   - Default server port: `8081`

## ▶️ Running the Application

### Option 1: Using Maven Wrapper (Recommended)
```bash
./mvnw spring-boot:run
```

### Option 2: Using Maven
```bash
mvn spring-boot:run
```

### Option 3: Build and Run JAR
```bash
./mvnw clean package
java -jar target/uber-0.0.1-SNAPSHOT.jar
```

### Option 4: Using IDE
- Open the project in IntelliJ IDEA or Eclipse
- Right-click on `UberApplication.java`
- Select "Run" or "Run 'UberApplication'"

**Expected Output:**
```
Started UberApplication in X.XXX seconds
```

The application will be available at: `http://localhost:8081`

## 📡 API Endpoints

### Authentication Endpoints

#### Register User
```http
POST /api/auth/register
Content-Type: application/json

{
  "username": "john",
  "password": "1234",
  "role": "ROLE_USER"
}
```

#### Login
```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "john",
  "password": "1234"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "username": "john",
  "role": "ROLE_USER"
}
```

### User Endpoints (Requires ROLE_USER)

#### Create Ride
```http
POST /api/v1/rides
Authorization: Bearer <token>
Content-Type: application/json

{
  "pickupLocation": "Koramangala",
  "dropLocation": "Indiranagar"
}
```

#### Get My Rides
```http
GET /api/v1/user/rides
Authorization: Bearer <token>
```

### Driver Endpoints (Requires ROLE_DRIVER)

#### View Pending Ride Requests
```http
GET /api/v1/driver/rides/requests
Authorization: Bearer <token>
```

#### Accept Ride
```http
POST /api/v1/driver/rides/{rideId}/accept
Authorization: Bearer <token>
```

### Common Endpoints

#### Complete Ride (USER or DRIVER)
```http
POST /api/v1/rides/{rideId}/complete
Authorization: Bearer <token>
```

## 🧪 Testing

### Using the Interactive Test Page

1. Start the application
2. Open your browser and navigate to:
   ```
   http://localhost:8081/test.html
   ```
3. The test page provides:
   - Registration and login forms
   - Token management (view all generated tokens)
   - Forms for all API endpoints
   - Real-time API testing

### Using cURL

#### Register a User
```bash
curl -X POST http://localhost:8081/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"john","password":"1234","role":"ROLE_USER"}'
```

#### Register a Driver
```bash
curl -X POST http://localhost:8081/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"driver1","password":"abcd","role":"ROLE_DRIVER"}'
```

#### Login
```bash
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"john","password":"1234"}'
```

#### Create Ride (Replace <token> with actual token)
```bash
curl -X POST http://localhost:8081/api/v1/rides \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{"pickupLocation":"A","dropLocation":"B"}'
```

## ⚙️ Configuration

### Application Configuration (`application.yaml`)

```yaml
spring:
  application:
    name: uber
  data:
    mongodb:
      uri: mongodb://localhost:27017/rideshare

app:
  jwt:
    secret: mySecretKeyForJWTTokenGenerationMustBeAtLeast256BitsLongForHS256Algorithm
    expiration-ms: 3600000  # 1 hour

server:
  port: 8081
```

### Environment Variables (Optional)

You can override configuration using environment variables:
- `MONGODB_URI`: MongoDB connection string
- `JWT_SECRET`: JWT signing secret
- `SERVER_PORT`: Server port

## 📊 Database Schema

### User Collection
```json
{
  "id": "String",
  "username": "String",
  "password": "String (BCrypt hashed)",
  "role": "ROLE_USER | ROLE_DRIVER"
}
```

### Ride Collection
```json
{
  "id": "String",
  "userId": "String",
  "driverId": "String (nullable)",
  "pickupLocation": "String",
  "dropLocation": "String",
  "status": "REQUESTED | ACCEPTED | COMPLETED",
  "createdAt": "Instant"
}
```

## 🔒 Security Features

- **JWT Authentication**: All protected endpoints require a valid JWT token
- **Role-Based Access**: Endpoints are protected based on user roles
- **Password Encryption**: Passwords are hashed using BCrypt
- **CORS Configuration**: Configured for local development

## 🐛 Troubleshooting

### MongoDB Connection Error
```
Error: Cannot connect to MongoDB
```
**Solution**: Ensure MongoDB is running on `localhost:27017`

### Port Already in Use
```
Error: Port 8081 is already in use
```
**Solution**: Change the port in `application.yaml` or stop the process using port 8081

### JWT Token Invalid
```
Error: Unauthorized
```
**Solution**: Make sure you're including the token in the Authorization header:
```
Authorization: Bearer <your-token>
```

## 📝 Notes

- The database and collections are created automatically on first use
- JWT tokens expire after 1 hour (configurable)
- All passwords are encrypted using BCrypt
- The test HTML page stores tokens in browser localStorage

## 👨‍💻 Author

Arnav

## 📄 License

This project is for educational purposes.

---

**Happy Coding! 🚀**

