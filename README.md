# ConfigSync - Backend (Spring Boot)

## Project Description
ConfigSync is a backend service that dynamically provides configuration data (in YAML or JSON format) to modify web pages based on predefined rules. The backend enforces authentication and authorization to ensure secure access to configuration data.

## Technologies Used
- **Java 21**
- **Spring Boot**
- **Spring Security (Basic Auth)**

## Authentication & Authorization
The application uses **In-Memory Authentication** with two predefined users:

| Username | Password  | Role  | Permissions |
|----------|----------|-------|-------------|
| `user`   | `password` | `USER`  | Can only perform `GET` requests |
| `admin`  | `admin` | `ADMIN` | Can perform all requests |

## Installation & Running the Application
### 1. Clone the repository:
   ```sh
   git clone https://github.com/kubilayckr/ConfigSync.git
   cd configsync-backend
  ```
### 2. Build and run the application using Maven:
  ```sh
  ./mvnw spring-boot:run
  ```
### 3. Access the API:
Once the application starts, it will be available at:
  ```
  http://localhost:8080/
  ```
## API Endpoints
The backend provides various endpoints to manage configurations.

For a complete list of available API endpoints, refer to the Postman collection located in:

```
postmancollections/ConfigSync.postman_collection.json
```
You can import this file into Postman to easily test the API.
